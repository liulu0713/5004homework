package student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GUIController}.
 *
 * <p>Because the controller depends on both a View and Model we use
 * lightweight stub/fake implementations rather than a full Swing environment.
 * This keeps tests fast, deterministic, and independent of the GUI toolkit.</p>
 *
 * <p>Tests follow the TDD pattern described in the assignment:
 * one test written first, code implemented to pass it, then repeated.</p>
 */
class GUIControllerTest {

    // -----------------------------------------------------------------------
    // Test fixtures
    // -----------------------------------------------------------------------

    /** Small set of games used as the test collection. */
    private Set<BoardGame> testGames;
    /** Planner backed by testGames. */
    private IPlanner planner;
    /** Game list model. */
    private IGameList gameList;
    /** Stub view that captures what the controller sends it. */
    private StubView view;
    /** The controller under test. */
    private GUIController controller;

    @BeforeEach
    void setUp() {
        testGames = new HashSet<>();
        //         name         id  minP maxP minT maxT  diff  rank  rating year
        testGames.add(new BoardGame("Catan",       1, 3, 4,  60,  90, 2.3,  200, 7.2, 1995));
        testGames.add(new BoardGame("Pandemic",    2, 2, 4,  45,  60, 2.4,  100, 7.6, 2008));
        testGames.add(new BoardGame("Terraforming Mars", 3, 1, 5, 120, 180, 3.2, 50, 8.4, 2016));
        testGames.add(new BoardGame("Ticket to Ride", 4, 2, 5, 45, 75, 1.9, 150, 7.5, 2004));
        testGames.add(new BoardGame("Wingspan",    5, 1, 5,  40,  70, 2.4,  80,  8.1, 2019));

        planner    = new Planner(testGames);
        gameList   = new GameList();
        view       = new StubView();
        controller = new GUIController(planner, gameList, view);
    }

    // -----------------------------------------------------------------------
    // Constructor / startup tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Constructor — initial filter shows all games")
    void testConstructorLoadsAllGames() {
        // After construction, all 5 games should be in the results table
        assertEquals(5, view.lastResultsCount,
                "Initial load should display all 5 games");
    }

    @Test
    @DisplayName("Constructor — status message is set on startup")
    void testConstructorSetsStatus() {
        assertNotNull(view.lastStatus, "Status should be set after construction");
        assertTrue(view.lastStatus.contains("5"),
                "Status should mention the number of games");
    }

    @Test
    @DisplayName("Constructor — null planner throws IllegalArgumentException")
    void testConstructorNullPlanner() {
        assertThrows(IllegalArgumentException.class,
                () -> new GUIController(null, gameList, view));
    }

    @Test
    @DisplayName("Constructor — null gameList throws IllegalArgumentException")
    void testConstructorNullGameList() {
        assertThrows(IllegalArgumentException.class,
                () -> new GUIController(planner, null, view));
    }

    @Test
    @DisplayName("Constructor — null view throws IllegalArgumentException")
    void testConstructorNullView() {
        assertThrows(IllegalArgumentException.class,
                () -> new GUIController(planner, gameList, null));
    }

    // -----------------------------------------------------------------------
    // Filter tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("handleApplyFilter — filter by rating reduces results")
    void testApplyFilterReducesResults() {
        view.filterText  = "rating>=8.0";
        view.sortCol     = GameData.RATING;
        view.ascending   = false;

        controller.handleApplyFilter();

        // Wingspan (8.1) and Terraforming Mars (8.4) should pass
        assertEquals(2, view.lastResultsCount,
                "Should return 2 games with rating >= 8.0");
    }

    @Test
    @DisplayName("handleApplyFilter — name contains filter")
    void testApplyFilterNameContains() {
        view.filterText = "name~=pan";
        controller.handleApplyFilter();
        assertEquals(1, view.lastResultsCount, "Should match only Pandemic");
    }

    @Test
    @DisplayName("handleApplyFilter — empty filter shows all games")
    void testApplyFilterEmpty() {
        // First narrow results
        view.filterText = "rating>=8.0";
        controller.handleApplyFilter();
        assertEquals(2, view.lastResultsCount);

        // Empty filter should NOT reset planner — it just re-sorts current set
        view.filterText = "";
        controller.handleApplyFilter();
        // Still 2 because planner is progressive
        assertEquals(2, view.lastResultsCount);
    }

    @Test
    @DisplayName("handleReset — restores all games after filtering")
    void testHandleReset() {
        view.filterText = "rating>=8.0";
        controller.handleApplyFilter();
        assertEquals(2, view.lastResultsCount);

        controller.handleReset();
        assertEquals(5, view.lastResultsCount,
                "After reset all 5 games should appear");
    }

    @Test
    @DisplayName("handleApplyFilter — sorting by name ascending is default order")
    void testApplySortByName() {
        view.filterText = "";
        view.sortCol    = GameData.NAME;
        view.ascending  = true;
        controller.handleApplyFilter();

        List<BoardGame> results = view.lastResults;
        assertNotNull(results);
        // First entry should be "Catan" (alphabetically first among our 5)
        assertEquals("Catan", results.get(0).getName());
    }

    // -----------------------------------------------------------------------
    // Add / remove list tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("handleAddToList — add by index")
    void testHandleAddByIndex() {
        // Show all games sorted by name; index 1 = Catan
        view.filterText = "";
        view.sortCol    = GameData.NAME;
        view.ascending  = true;
        controller.handleApplyFilter();

        view.addFieldText = "1";
        controller.handleAddToList();

        assertEquals(1, gameList.count(), "One game should be in the list");
        assertEquals("Catan", gameList.getGameNames().get(0));
    }

    @Test
    @DisplayName("handleAddToList — add all games")
    void testHandleAddAll() {
        view.addFieldText = "all";
        controller.handleAddToList();
        assertEquals(5, gameList.count(), "All 5 games should be in the list");
    }

    @Test
    @DisplayName("handleAddToList — add by range")
    void testHandleAddRange() {
        view.filterText = "";
        view.sortCol    = GameData.NAME;
        view.ascending  = true;
        controller.handleApplyFilter();

        view.addFieldText = "1-3";
        controller.handleAddToList();
        assertEquals(3, gameList.count());
    }

    @Test
    @DisplayName("handleAddToList — empty input does not throw, shows status")
    void testHandleAddEmptyInput() {
        view.addFieldText = "";
        assertDoesNotThrow(() -> controller.handleAddToList());
        assertEquals(0, gameList.count());
    }

    @Test
    @DisplayName("handleRemoveFromList — remove by name")
    void testHandleRemoveByName() {
        // Add all first
        view.addFieldText = "all";
        controller.handleAddToList();
        assertEquals(5, gameList.count());

        view.removeFieldText = "catan";
        controller.handleRemoveFromList();
        assertEquals(4, gameList.count());
        assertFalse(gameList.getGameNames().contains("Catan"));
    }

    @Test
    @DisplayName("handleRemoveFromList — remove all clears list")
    void testHandleRemoveAll() {
        view.addFieldText = "all";
        controller.handleAddToList();

        view.removeFieldText = "all";
        controller.handleRemoveFromList();
        assertEquals(0, gameList.count());
    }

    @Test
    @DisplayName("handleClearList — list is empty after clear")
    void testHandleClearList() {
        view.addFieldText = "all";
        controller.handleAddToList();
        assertTrue(gameList.count() > 0);

        controller.handleClearList();
        assertEquals(0, gameList.count());
        assertEquals(0, view.lastListSize, "View should show empty list");
    }

    // -----------------------------------------------------------------------
    // Double-click / add-selected tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("handleDoubleClickAdd — adds game at selected row index")
    void testHandleDoubleClickAdd() {
        view.filterText = "";
        view.sortCol    = GameData.NAME;
        view.ascending  = true;
        controller.handleApplyFilter(); // sorts by name: Catan at index 0

        view.selectedRows = new int[]{0}; // row 0 → 1-based index 1 → Catan
        controller.handleDoubleClickAdd();

        assertEquals(1, gameList.count());
        assertEquals("Catan", gameList.getGameNames().get(0));
    }

    @Test
    @DisplayName("handleDoubleClickAdd — no selection does nothing")
    void testHandleDoubleClickNoSelection() {
        view.selectedRows = new int[]{};
        assertDoesNotThrow(() -> controller.handleDoubleClickAdd());
        assertEquals(0, gameList.count());
    }

    @Test
    @DisplayName("handleAddSelected — adds multiple selected rows")
    void testHandleAddSelected() {
        view.filterText = "";
        view.sortCol    = GameData.NAME;
        view.ascending  = true;
        controller.handleApplyFilter();

        view.selectedRows = new int[]{0, 1, 2}; // first 3 alphabetically
        controller.handleAddSelected();

        assertEquals(3, gameList.count());
    }

    @Test
    @DisplayName("handleAddSelected — empty selection shows status, no crash")
    void testHandleAddSelectedEmpty() {
        view.selectedRows = new int[]{};
        assertDoesNotThrow(() -> controller.handleAddSelected());
        assertEquals(0, gameList.count());
        assertNotNull(view.lastStatus);
    }

    // -----------------------------------------------------------------------
    // Stub view implementation
    // -----------------------------------------------------------------------

    /**
     * A minimal stub/fake of {@link GUIView} that records all method calls
     * so tests can verify controller behaviour without a real Swing window.
     *
     * <p>Implements only the public API used by {@link GUIController}.</p>
     */
    private static class StubView extends GUIView {

        // Captured outputs
        int              lastResultsCount = 0;
        List<BoardGame>  lastResults      = null;
        int              lastListSize     = 0;
        String           lastStatus       = null;

        // Configurable inputs (set by each test)
        String   filterText      = "";
        GameData sortCol         = GameData.NAME;
        boolean  ascending       = true;
        String   addFieldText    = "";
        String   removeFieldText = "";
        int[]    selectedRows    = new int[]{};

        // Override constructor to avoid rendering the actual window
        StubView() {
            // Don't call super() — we don't want a visible window during tests.
            // We rely on GUIController calling setController(), not any Swing setup.
        }

        @Override
        public void setController(GUIController c) {
            // Intentionally no-op — controller stores it; no Swing wiring needed
        }

        @Override
        public void updateResultsTable(List<BoardGame> games, GameData col) {
            lastResultsCount = games.size();
            lastResults      = games;
        }

        @Override
        public void updateMyList(List<String> names) {
            lastListSize = names.size();
        }

        @Override
        public void setStatus(String msg) {
            lastStatus = msg;
        }

        @Override
        public void showError(String msg) {
            // Tests should not pop dialogs; errors are surfaced by assertions
        }

        @Override
        public String getFilterText()          { return filterText;      }
        @Override
        public GameData getSelectedSortColumn() { return sortCol;         }
        @Override
        public boolean isAscending()            { return ascending;       }
        @Override
        public String getAddFieldText()         { return addFieldText;    }
        @Override
        public String getRemoveFieldText()      { return removeFieldText; }
        @Override
        public int[] getSelectedTableRows()     { return selectedRows;    }
        @Override
        public void clearAddField()             { addFieldText    = "";   }
        @Override
        public void clearRemoveField()          { removeFieldText = "";   }
        @Override
        public String promptSaveFile()          { return null;            }
    }
}