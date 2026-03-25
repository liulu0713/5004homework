package student;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Controller component of the MVC pattern for the BGArena Planner GUI.
 *
 * <p>This class mediates between the {@link GUIView} (View) and the
 * {@link Planner} / {@link GameList} (Model). It contains <em>all</em>
 * application logic triggered by user interaction. Neither the view nor the
 * model communicate directly with each other.</p>
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Translate view events (button clicks) into model calls</li>
 *   <li>Query the model and push results back to the view</li>
 *   <li>Handle errors and display status/error messages via the view</li>
 * </ul>
 * </p>
 */
public class GUIController {

    // -----------------------------------------------------------------------
    // Model references
    // -----------------------------------------------------------------------
    /** The planner model used for filtering and sorting. */
    private final IPlanner planner;
    /** The game-list model used for managing the user's curated list. */
    private final IGameList gameList;

    // -----------------------------------------------------------------------
    // View reference
    // -----------------------------------------------------------------------
    /** The view this controller drives. */
    private final GUIView view;

    // -----------------------------------------------------------------------
    // Local state
    // -----------------------------------------------------------------------
    /**
     * Cached copy of the most recent filter result so that double-click and
     * "add selected rows" operations can reference the same ordered list that
     * is currently displayed in the results table.
     */
    private List<BoardGame> lastFilteredGames = new ArrayList<>();

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    /**
     * Constructs the controller, wires the view, and runs an initial empty
     * filter to populate the results table with all games.
     *
     * @param planner  the planner model (non-null)
     * @param gameList the game-list model (non-null)
     * @param view     the view (non-null)
     * @throws IllegalArgumentException if any argument is null
     */
    public GUIController(IPlanner planner, IGameList gameList, GUIView view) {
        if (planner == null)  throw new IllegalArgumentException("planner is null");
        if (gameList == null) throw new IllegalArgumentException("gameList is null");
        if (view == null)     throw new IllegalArgumentException("view is null");

        this.planner  = planner;
        this.gameList = gameList;
        this.view     = view;

        view.setController(this);
        // Show all games on startup
        runFilter("", GameData.NAME, true);
        view.setStatus("Loaded collection — " + lastFilteredGames.size() + " games available.");
    }

    // -----------------------------------------------------------------------
    // Handler methods (called by GUIView listeners)
    // -----------------------------------------------------------------------

    /**
     * Handles the "Apply Filter" button / Enter key in the filter field.
     * Reads the filter expression, sort column, and direction from the view,
     * applies them via the planner, and refreshes the results table.
     */
    public void handleApplyFilter() {



        GameData sortCol  = view.getSelectedSortColumn();
        boolean ascending = view.isAscending();

        String field  = view.getFilterField();
        String op     = view.getOperator();
        String value  = view.getFilterValue();

        String rawFilter = field + op + value;
        // Strip placeholder text - if it still contains 'e.g.' it is the hint text
        String filter = rawFilter.startsWith("e.g.") ? "" : rawFilter;
        // Remove whitespace for the planner (matches ConsoleApp behavior)
        filter = filter.replaceAll("\\s", "").toLowerCase();

        try {
            runFilter(filter, sortCol, ascending);
            int count = lastFilteredGames.size();
            view.setStatus("Filter applied — " + count + " game" + (count == 1 ? "" : "s") + " found.");
        } catch (IllegalArgumentException ex) {
            view.showError("Invalid filter: " + ex.getMessage());
            view.setStatus("Filter error — check your expression.");
        }
    }

    /**
     * Handles the "Reset" button.
     * Clears all planner filters and shows the full collection.
     */
    public void handleReset() {
        planner.reset();
        runFilter("", GameData.NAME, true);
        view.setStatus("Filters reset — showing all " + lastFilteredGames.size() + " games.");
    }

    /**
     * Handles the "Add" button in the My List panel.
     * Reads the add-field text and delegates to {@link IGameList#addToList}.
     * Uses the last filtered result as the source stream.
     */
    public void handleAddToList() {
        String input = view.getAddFieldText();
        if (input.isEmpty() || input.startsWith("e.g.")) {
            view.setStatus("Enter an index, range, game name, or \"all\" in the Add field.");
            return;
        }
        try {
            Stream<BoardGame> stream = lastFilteredGames.stream();
            gameList.addToList(input, stream);
            view.clearAddField();
            refreshMyList();
            view.setStatus("Added: " + input + "  ·  List now has " + gameList.count() + " game(s).");
        } catch (IllegalArgumentException ex) {
            view.showError("Could not add: " + ex.getMessage());
        }
    }

    /**
     * Handles the "Remove" button in the My List panel.
     * Reads the remove-field text and delegates to {@link IGameList#removeFromList}.
     */
    public void handleRemoveFromList() {
        String input = view.getRemoveFieldText();
        if (input.isEmpty() || input.startsWith("e.g.")) {
            view.setStatus("Enter an index, range, or game name in the Remove field.");
            return;
        }
        try {
            gameList.removeFromList(input);
            view.clearRemoveField();
            refreshMyList();
            view.setStatus("Removed: " + input + "  ·  List now has " + gameList.count() + " game(s).");
        } catch (IllegalArgumentException ex) {
            view.showError("Could not remove: " + ex.getMessage());
        }
    }

    /**
     * Handles the "Clear All" button. Clears the entire game list.
     */
    public void handleClearList() {
        gameList.clear();
        refreshMyList();
        view.setStatus("Game list cleared.");
    }

    /**
     * Handles a double-click on a row in the results table.
     * Adds the double-clicked game to My List by its 1-based index.
     */
    public void handleDoubleClickAdd() {
        int[] rows = view.getSelectedTableRows();
        if (rows.length == 0) return;
        // Use the first selected row (double-click selects one row)
        int displayIndex = rows[0] + 1; // convert 0-based view index → 1-based planner index
        try {
            gameList.addToList(String.valueOf(displayIndex), lastFilteredGames.stream());
            refreshMyList();
            String name = lastFilteredGames.get(rows[0]).getName();
            view.setStatus("Quick-added: " + name + "  ·  List has " + gameList.count() + " game(s).");
        } catch (IllegalArgumentException ex) {
            view.setStatus("Already in list or invalid: " + ex.getMessage());
        }
    }

    /**
     * Handles the "Add Selected Rows from Table" button.
     * Adds all currently selected rows from the results table to My List.
     */
    public void handleAddSelected() {
        int[] rows = view.getSelectedTableRows();
        if (rows.length == 0) {
            view.setStatus("Select one or more rows in the results table first.");
            return;
        }
        int added = 0;
        for (int row : rows) {
            if (row < lastFilteredGames.size()) {
                try {
                    gameList.addToList(String.valueOf(row + 1), lastFilteredGames.stream());
                    added++;
                } catch (IllegalArgumentException ignored) {
                    // already in list — skip silently
                }
            }
        }
        refreshMyList();
        view.setStatus("Added " + added + " game(s)  ·  List has " + gameList.count() + " game(s).");
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    /**
     * Applies the given filter/sort to the planner, caches the result, and
     * updates the results table in the view.
     *
     * @param filter    filter expression (may be empty)
     * @param sortCol   column to sort on
     * @param ascending true for ascending order
     * @throws IllegalArgumentException if the planner rejects the filter
     */
    private void runFilter(String filter, GameData sortCol, boolean ascending) {
        Stream<BoardGame> stream = planner.filter(filter, sortCol, ascending);
        lastFilteredGames = stream.collect(Collectors.toList());
        view.updateResultsTable(lastFilteredGames, sortCol);
    }

    /**
     * Pushes the current game list contents to the view.
     */
    private void refreshMyList() {
        view.updateMyList(gameList.getGameNames());
    }
}