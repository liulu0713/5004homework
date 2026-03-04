package student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Planner implements IPlanner {
    /** All games loaded into the planner (never changes). */
    private final List<BoardGame> allGames;

    /** Current working set after progressive filters are applied. */
    private List<BoardGame> currentGames;

    /**
     * Constructs a planner with the full board game collection.
     *
     * @param games the full collection of board games
     * @throws IllegalArgumentException if games is null
     */
    public Planner(Set<BoardGame> games) {
        if (games == null) {
            throw new IllegalArgumentException("games is null");
        }
        this.allGames = new ArrayList<>(games);
        this.currentGames = new ArrayList<>(games);
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        return filter(filter, GameData.NAME, true);
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        return filter(filter, sortOn, true);
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        if (sortOn == GameData.ID) {
            throw new IllegalArgumentException();
        }
        String f = (filter == null) ? "" : filter;
        if (!f.trim().isEmpty()) {
            applyFilterString(f);
        }
        sortCurrent(sortOn, ascending);
        return currentGames.stream();
    }

    @Override
    public void reset() {
        currentGames = new ArrayList<>(allGames);
    }

    // -------------------------------------------------------------------------
    // Filter parsing – one responsibility per method
    // -------------------------------------------------------------------------

    /** Splits a comma-separated filter string and applies each condition. */
    private void applyFilterString(String filterString) {
        for (String raw : filterString.split(",")) {
            String cond = raw.trim();
            if (!cond.isEmpty()) {
                applySingleCondition(cond);
            }
        }
    }

    /** Parses one condition (e.g. "minPlayers>2") and narrows currentGames. */
    private void applySingleCondition(String cond) {
        Operation op = Operation.fromString(cond);
        String[] lr = splitByOperator(cond, op.getToken());
        String left  = lr[0].trim();
        String right = lr[1].trim();

        GameData col = GameData.fromString(left);
        if (col == GameData.ID) {
            throw new IllegalArgumentException("Cannot filter on id");
        }
        currentGames = applyOneFilter(currentGames, col, op, right);
    }

    /** Splits cond into [left, right] around opToken. */
    private String[] splitByOperator(String cond, String opToken) {
        int idx = cond.indexOf(opToken);
        if (idx < 0) {
            throw new IllegalArgumentException("Invalid filter: " + cond);
        }
        String left  = cond.substring(0, idx);
        String right = cond.substring(idx + opToken.length());
        if (left.trim().isEmpty() || right.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid filter: " + cond);
        }
        return new String[]{left, right};
    }

    // -------------------------------------------------------------------------
    // Filtering helpers
    // -------------------------------------------------------------------------

    /** Returns the subset of input whose games satisfy the condition. */
    private List<BoardGame> applyOneFilter(List<BoardGame> input, GameData col,
                                           Operation op, String rightRaw) {
        List<BoardGame> out = new ArrayList<>();
        for (BoardGame g : input) {
            if (matchesCondition(g, col, op, rightRaw)) {
                out.add(g);
            }
        }
        return out;
    }

    /** Dispatches to a string or numeric matcher based on the column type. */
    private boolean matchesCondition(BoardGame g, GameData col,
                                     Operation op, String rightRaw) {
        if (col == GameData.NAME) {
            return matchesStringCondition(g.getName(), op, rightRaw);
        }
        return matchesNumericCondition(getNumericValue(g, col), op, rightRaw);
    }

    /** Evaluates a name/string condition case-insensitively. */
    private boolean matchesStringCondition(String name, Operation op, String rawValue) {
        String gameName    = (name == null)     ? "" : name.toLowerCase();
        String filterValue = (rawValue == null) ? "" : rawValue.toLowerCase();

        switch (op) {
            case CONTAINS:
                return gameName.contains(filterValue)
                        || gameName.replace(" ", "").contains(filterValue.replace(" ", ""));
            case EQUALS:     return gameName.equals(filterValue);
            case NOT_EQUALS: return !gameName.equals(filterValue);
            case GREATER:    return gameName.compareTo(filterValue) > 0;
            case GREATER_EQ: return gameName.compareTo(filterValue) >= 0;
            case LESS:       return gameName.compareTo(filterValue) < 0;
            case LESS_EQ:    return gameName.compareTo(filterValue) <= 0;
            default:
                throw new IllegalArgumentException("Invalid operator for name: " + op);
        }
    }

    /** Parses the right-hand value and evaluates a numeric condition. */
    private boolean matchesNumericCondition(double leftNum, Operation op, String rawValue) {
        double rightNum = parseDouble(rawValue);
        switch (op) {
            case GREATER:    return leftNum >  rightNum;
            case GREATER_EQ: return leftNum >= rightNum;
            case LESS:       return leftNum <  rightNum;
            case LESS_EQ:    return leftNum <= rightNum;
            case EQUALS:     return leftNum == rightNum;
            case NOT_EQUALS: return leftNum != rightNum;
            default:
                throw new IllegalArgumentException("Invalid operator for numeric column: " + op);
        }
    }

    /** Parses a string as a double, throwing a clean error if invalid. */
    private double parseDouble(String raw) {
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expected a number but got: " + raw);
        }
    }

    // -------------------------------------------------------------------------
    // Numeric value extraction
    // -------------------------------------------------------------------------

    private double getNumericValue(BoardGame g, GameData col) {
        switch (col) {
            case RATING:      return g.getRating();
            case DIFFICULTY:  return g.getDifficulty();
            case RANK:        return g.getRank();
            case MIN_PLAYERS: return g.getMinPlayers();
            case MAX_PLAYERS: return g.getMaxPlayers();
            case MIN_TIME:    return g.getMinPlayTime();
            case MAX_TIME:    return g.getMaxPlayTime();
            case YEAR:        return g.getYearPublished();
            default:
                throw new IllegalArgumentException();
        }
    }

    // -------------------------------------------------------------------------
    // Sorting
    // -------------------------------------------------------------------------

    private void sortCurrent(GameData sortOn, boolean ascending) {
        Collections.sort(currentGames, (g1, g2) -> {
            int cmp = compareByColumn(g1, g2, sortOn);
            if (!ascending) {
                cmp = -cmp;
            }
            if (cmp == 0) {
                cmp = g1.getName().compareToIgnoreCase(g2.getName());
            }
            if (cmp == 0) {
                cmp = Integer.compare(g1.hashCode(), g2.hashCode());
            }
            return cmp;
        });
    }

    private int compareByColumn(BoardGame g1, BoardGame g2, GameData col) {
        if (col == GameData.NAME) {
            return g1.getName().compareToIgnoreCase(g2.getName());
        }
        return Double.compare(getNumericValue(g1, col), getNumericValue(g2, col));
    }

    // -------------------------------------------------------------------------
    // Operation enum
    // -------------------------------------------------------------------------

    private enum Operation {
        /** Greater than or equal to (>=). */
        GREATER_EQ(">="),
        /** Less than or equal to (<=). */
        LESS_EQ("<="),
        /** Not equal to (!=). */
        NOT_EQUALS("!="),
        /** Equal to (==). */
        EQUALS("=="),
        /** Contains (~=). */
        CONTAINS("~="),
        /** Greater than (>). */
        GREATER(">"),
        /** Less than (<). */
        LESS("<");

        private final String token;

        Operation(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public static Operation fromString(String s) {
            if (s.contains(">=")) { return GREATER_EQ; }
            if (s.contains("<=")) { return LESS_EQ;    }
            if (s.contains("!=")) { return NOT_EQUALS; }
            if (s.contains("==")) { return EQUALS;     }
            if (s.contains("~=")) { return CONTAINS;   }
            if (s.contains(">"))  { return GREATER;    }
            if (s.contains("<"))  { return LESS;        }
            throw new IllegalArgumentException("No operator found in: " + s);
        }
    }
}