package student;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Implementation of the IGameList interface.
 * Manages a set of board games with support for adding and removing by index, range, or name.
 */
public class GameList implements IGameList {
    /** Stores the selected games with no duplicates. */
    private final Set<BoardGame> games;

    /**
     * Constructor for the GameList.
     */
    public GameList() {this.games = new HashSet<>();
    }

    @Override
    public List<String> getGameNames() {
        return games.stream()
                    .map(BoardGame::getName)
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .collect(Collectors.toList());
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

            // create folder like "temp/" if needed
            if (parent != null) {
                java.nio.file.Files.createDirectories(parent);
            }

            // overwrite file (default behavior)
            java.nio.file.Files.write(path, names,
                    java.nio.charset.StandardCharsets.UTF_8,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.TRUNCATE_EXISTING);

        } catch (Exception e) {
            throw new RuntimeException("Could not save to file:" + filename, e);
        }
    }

    /**
     * helper method to parse input strings and return a list of target games.
     * @param str The list of games to select from.
     * @return A list of games matching the input requested.
     */
    private List<BoardGame> parseInputToGames(String str, List<BoardGame> filteredList) {
        String input = str.trim();
        List<BoardGame> targets = new ArrayList<>();

        if (input.equalsIgnoreCase("all")){
            targets.addAll(filteredList);
        } else if (input.matches("\\d+")) {
            int index = Integer.parseInt(input);
            if(index < 1 || index > filteredList.size()){
                throw new IllegalArgumentException("index out of range");
            }
            targets.add(filteredList.get(index - 1));
        } else if (input.matches("\\d+\\s*-\\s*\\d+")) {
            String[] parts = input.split("-");
            int start = Integer.parseInt(parts[0].trim());
            int end = Integer.parseInt(parts[1].trim());
            if (start < 1 || end > filteredList.size() || start > end) {
                throw new IllegalArgumentException("range out of range");
        }
            for (int i = start - 1; i <= end - 1; i++) {
                targets.add(filteredList.get(i));
            }
        } else {
            boolean found = false;

            for (BoardGame g : filteredList) {
                if (g.getName().equalsIgnoreCase(input)) {
                    targets.add(g);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("No game named: " + input);
            }
        }

        return targets;
    }



    private void sortBoardGames(List<BoardGame> list) {

        list.sort((g1, g2) -> {

                int result = g1.getName()
                        .compareToIgnoreCase(g2.getName());

                if (result != 0) {
                    return result;
                }

                return Integer.compare(g1.hashCode(), g2.hashCode());

        });
    }

    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("str is null");
        }

        // Stream only one time，convert to List
        List<BoardGame> filteredList = new ArrayList<>();
        filtered.forEach(filteredList::add);


        //
            List<BoardGame> toAdd = parseInputToGames(str, filteredList);
            int beforeSize = games.size();
            games.addAll(toAdd);

            if (!str.trim().equalsIgnoreCase("all") && games.size() == beforeSize) {
                throw new IllegalArgumentException("No new games added for: " + str);
        }

    }
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {

        if (str == null) {
            throw new IllegalArgumentException("str is null");
        }
        if (str.trim().equalsIgnoreCase("all")) {
            games.clear();
            return;
        }
            List<BoardGame> currentList = new ArrayList<>(games);
            sortBoardGames(currentList);

            List<BoardGame> toRemove = parseInputToGames(str, currentList);
            int beforeSize = games.size();
            games.removeAll(toRemove);

            if (games.size() == beforeSize) {
                throw new IllegalArgumentException("No games removed for: " + str);
        }
    }

}
