package student;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.Collections;


public class Planner implements IPlanner {
    private final List<BoardGame> allGames;
    private List<BoardGame> currentGames;

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

    private String removeSpaces(String s) {
        return s.replaceAll("\\s+", "");
    }


    private String[] splitByOperator(String cond, String opToken) {
        int idx = cond.indexOf(opToken);
        if (idx < 0) throw new IllegalArgumentException("Invalid filter: " + cond);

        String left = cond.substring(0, idx);
        String right = cond.substring(idx + opToken.length());

        if (left.isEmpty() || right.isEmpty()) {
            throw new IllegalArgumentException("Invalid filter: " + cond);
        }
        return new String[]{left, right};
    }

    private enum Operation {
        GREATER_EQ(">="),
        LESS_EQ("<="),
        NOT_EQUALS("!="),
        EQUALS("=="),
        CONTAINS("~="),
        GREATER(">"),
        LESS("<");

        final String token;

        Operation(String token) {
            this.token = token;
        }

        static Operation fromString(String s) {
            if (s.contains(">=")) return GREATER_EQ;
            if (s.contains("<=")) return LESS_EQ;
            if (s.contains("!=")) return NOT_EQUALS;
            if (s.contains("==")) return EQUALS;
            if (s.contains("~=")) return CONTAINS;
            if (s.contains(">")) return GREATER;
            if (s.contains("<")) return LESS;
            throw new IllegalArgumentException("No operator found in: " + s);
        }
    }

    private double getNumericValue(BoardGame g, GameData col) {
        switch (col) {
            case RATING:
                return g.getRating();
            case DIFFICULTY:
                return g.getDifficulty();
            case RANK:
                return g.getRank();
            case MIN_PLAYERS:
                return g.getMinPlayers();
            case MAX_PLAYERS:
                return g.getMaxPlayers();
            case MIN_TIME:
                return g.getMinPlayTime();
            case MAX_TIME:
                return g.getMaxPlayTime();
            case YEAR:
                return g.getYearPublished();
            default:
                throw new IllegalArgumentException();


        }
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {

        if (sortOn == GameData.ID) {
            throw new IllegalArgumentException();
        }
        String f = (filter == null) ? "" : filter;
        if (f.trim().isEmpty()) {
            sortCurrent(sortOn, ascending);
            return currentGames.stream();
        }
        String[] parts = f.split(",");
        List<BoardGame> result = new ArrayList<>(currentGames);

        for (String raw : parts) {
            String cond = removeSpaces(raw);
            if (cond.isEmpty()) continue;
            Operation op = Operation.fromString(cond);
            String[] lr = splitByOperator(cond, op.token);
            String left = lr[0];
            String right = lr[1];

            GameData col = GameData.fromString(left);
            if (col == null) {
                throw new IllegalArgumentException("Unknown column: " + left);
            }
            if (col == GameData.ID) {
                throw new IllegalArgumentException("Cannot filter on id");
            }
            result = applyOneFilter(result, col, op, right);
        }


        currentGames = result;
        sortCurrent(sortOn, ascending);

        return currentGames.stream();
    }


    @Override
    public void reset() {
        currentGames = new ArrayList<>(allGames);
    }

    private List<BoardGame> applyOneFilter(List<BoardGame> input, GameData col, Operation op, String rightRaw) {
        List<BoardGame> out = new ArrayList<>();

        for (BoardGame g : input) {
            if (matches(g, col, op, rightRaw)) {
                out.add(g);
            }
        }

        return out;
    }

    private boolean matches(BoardGame g, GameData col, Operation op, String rightRaw) {
        if (col == GameData.NAME) {
            String left = g.getName();
            String right = rightRaw;

            if (left == null) left = "";
            if (right == null) right = "";

            left = left.toLowerCase();
            right = right.toLowerCase();

            switch (op) {
                case CONTAINS:
                    return left.contains(right);
                case EQUALS:
                    return left.equals(right);
                case NOT_EQUALS:
                    return !left.equals(right);
                default:
                    throw new IllegalArgumentException("Operator " + op + " not valid for NAME");
            }
        }

        double leftNum = getNumericValue(g, col);

        double rightNum;
        try {
            rightNum = Double.parseDouble(rightRaw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + rightRaw);
        }

        switch (op) {
            case GREATER:
                return leftNum > rightNum;
            case GREATER_EQ:
                return leftNum >= rightNum;
            case LESS:
                return leftNum < rightNum;
            case LESS_EQ:
                return leftNum <= rightNum;
            case EQUALS:
                return leftNum == rightNum;
            case NOT_EQUALS:
                return leftNum != rightNum;
            case CONTAINS:
                throw new IllegalArgumentException("Operator ~= not valid for numeric columns");
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }

    private int compareByColumn(BoardGame g1, BoardGame g2, GameData col) {

        if (col == GameData.NAME) {
            return g1.getName().compareToIgnoreCase(g2.getName());
        }

        double a = getNumericValue(g1, col);
        double b = getNumericValue(g2, col);
        return Double.compare(a, b);
    }

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
}
