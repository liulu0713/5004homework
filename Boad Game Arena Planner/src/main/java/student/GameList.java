package student;

import java.util.*;
import java.util.stream.Stream;

public class GameList implements IGameList {
    /** Stores the selected games with no duplicates. */
    private final Set<BoardGame> games;

    /**
     * Constructor for the GameList.
     */
    public GameList() {
        games = new HashSet<>();
    }

    @Override
    public List<String> getGameNames() {
        List<String> names = new ArrayList<>();
        for (BoardGame g : games) {
            names.add(g.getName());
        }
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public int count() {
        return games.size();
    }

    @Override
    public void saveGame(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }
        String f = filename.trim();
        if (f.isEmpty()) {
            throw new IllegalArgumentException("filename is empty");
        }

        // get sorted names
        List<String> names = getGameNames();

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(f);
            java.nio.file.Path parent = path.getParent();

            // ✅ create folder like "temp/" if needed
            if (parent != null) {
                java.nio.file.Files.createDirectories(parent);
            }

            // ✅ overwrite file (default behavior)
            java.nio.file.Files.write(path, names,
                    java.nio.charset.StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);

        } catch (Exception e) {
            throw new RuntimeException("Could not save to file:" + filename, e);
        }
    }


    private void sortBoardGames(List<BoardGame> list) {

        Collections.sort(list, new Comparator<BoardGame>() {

            @Override
            public int compare(BoardGame g1, BoardGame g2) {

                int result = g1.getName()
                        .compareToIgnoreCase(g2.getName());

                if (result != 0) {
                    return result;
                }

                return Integer.compare(g1.hashCode(), g2.hashCode());
            }
        });
    }

    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("str is null");
        }
        if (filtered == null) {
            throw new IllegalArgumentException("filtered is null");
        }

        String input = str.trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }

        // Stream 只能用一次，先转成 List
        List<BoardGame> filteredList = new ArrayList<>();
        filtered.forEach(filteredList::add);

        // 按名字排序（你已有 sortBoardGames）
        sortBoardGames(filteredList);

        int beforeSize = games.size();

        // 1) all
        if (input.equalsIgnoreCase("all")) {
            for (BoardGame game : filteredList) {
                games.add(game);
            }
        } else if (input.matches("\\d+")) {
            int index = Integer.parseInt(input);

            if (index < 1 || index > filteredList.size()) {
                throw new IllegalArgumentException("index out of range");
            }

            games.add(filteredList.get(index - 1));
        } else if (input.matches("\\d+\\s*-\\s*\\d+")) {
            String[] parts = input.split("-");
            int start = Integer.parseInt(parts[0].trim());
            int end = Integer.parseInt(parts[1].trim());

            if (start < 1 || end > filteredList.size() || start > end) {
                throw new IllegalArgumentException("range out of range");
            }

            for (int i = start - 1; i <= end - 1; i++) {
                games.add(filteredList.get(i));
            }
        } else {
            boolean found = false;

            for (BoardGame g : filteredList) {
                if (g.getName().equalsIgnoreCase(input)) {
                    games.add(g);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("No game named: " + input);
            }
        }

        if (!input.equalsIgnoreCase("all") && games.size() == beforeSize) {
            throw new IllegalArgumentException("No games added for: " + input);
        }

    }
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {

        if (str == null) {
            throw new IllegalArgumentException("str is null");
        }

        String input = str.trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }

        int beforeSize = games.size();

        // all -> clear
        if (input.equalsIgnoreCase("all")) {
            games.clear();
            return;
        }

        List<BoardGame> currentList = new ArrayList<>(games);
        sortBoardGames(currentList);

        if (input.matches("\\d+")) {

            int index = Integer.parseInt(input);

            if (index < 1 || index > currentList.size()) {
                throw new IllegalArgumentException("index out of range");
            }

            games.remove(currentList.get(index - 1));
        } else if (input.matches("\\d+\\s*-\\s*\\d+")) {
            String[] parts = input.split("-");
            int start = Integer.parseInt(parts[0].trim());
            int end = Integer.parseInt(parts[1].trim());

            if (start < 1 || end > currentList.size() || start > end) {
                throw new IllegalArgumentException("range out of range");
            }

            for (int i = end - 1; i >= start - 1; i--) {
                games.remove(currentList.get(i));
            }
        } else {
            boolean found = false;

            for (BoardGame g : currentList) {
                if (g.getName().equalsIgnoreCase(input)) {
                    games.remove(g);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("No game named: " + input);
            }
        }
        if (games.size() == beforeSize) {
            throw new IllegalArgumentException("No games removed for: " + input);
        }
    }

}
