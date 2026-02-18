package student;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameList implements IGameList {

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
        if (filename == null || filename.trim().isEmpty()) {
            throw new UnsupportedOperationException("filename is null/empty");
        }

        List<String> namelist = getGameNames();
        try {
            FileWriter writer = new FileWriter(filename);
            for (String name : namelist) {
                writer.write(name + "\n");
            }
            writer.close();
        } catch (IOException e) {
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
        if (str == null) throw new IllegalArgumentException("str is null");
        if (filtered == null) throw new IllegalArgumentException("filtered is null");

        String input = str.trim();
        if (input.isEmpty()) throw new IllegalArgumentException("empty input");

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
        }
        // 2) 单个数字
        else if (input.matches("\\d+")) {
            int index = Integer.parseInt(input);

            if (index < 1 || index > filteredList.size()) {
                throw new IllegalArgumentException("index out of range");
            }

            games.add(filteredList.get(index - 1));
        }
        // 3) 范围 1-5（允许空格）
        else if (input.matches("\\d+\\s*-\\s*\\d+")) {
            String[] parts = input.split("-");
            int start = Integer.parseInt(parts[0].trim());
            int end = Integer.parseInt(parts[1].trim());

            if (start < 1 || end > filteredList.size() || start > end) {
                throw new IllegalArgumentException("range out of range");
            }

            for (int i = start - 1; i <= end - 1; i++) {
                games.add(filteredList.get(i));
            }
        }
        // 4) 名字（优先级：如果不是数字/范围/all，就按名字）
        else {
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

        if (str == null) throw new IllegalArgumentException("str is null");

        String input = str.trim();
        if (input.isEmpty()) throw new IllegalArgumentException("empty input");

        int beforeSize = games.size();

        // all -> clear
        if (input.equalsIgnoreCase("all")) {
            games.clear();
            return;
        }

        // 当前 games 转成 list，并按名字排序（用于数字/范围索引）
        List<BoardGame> currentList = new ArrayList<>(games);
        sortBoardGames(currentList);

        // 1) 单个数字
        if (input.matches("\\d+")) {

            int index = Integer.parseInt(input);

            if (index < 1 || index > currentList.size()) {
                throw new IllegalArgumentException("index out of range");
            }

            games.remove(currentList.get(index - 1));
        }

        // 2) 范围（允许空格：1 - 5）
        else if (input.matches("\\d+\\s*-\\s*\\d+")) {

            String[] parts = input.split("-");
            int start = Integer.parseInt(parts[0].trim());
            int end = Integer.parseInt(parts[1].trim());

            if (start < 1 || end > currentList.size() || start > end) {
                throw new IllegalArgumentException("range out of range");
            }

            // 删除时建议从后往前删（更安全）
            for (int i = end - 1; i >= start - 1; i--) {
                games.remove(currentList.get(i));
            }
        }

        // 3) 名字
        else {
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

        // 如果没有任何变化（比如移除不存在/重复操作），按要求抛异常
        if (games.size() == beforeSize) {
            throw new IllegalArgumentException("No games removed for: " + input);
        }
    }

}