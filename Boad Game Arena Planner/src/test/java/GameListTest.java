
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import student.BoardGame;
import student.GameList;

public class GameListTest {

    private GameList gameList;

    private BoardGame catan;
    private BoardGame azul;
    private BoardGame pandemic;
    private BoardGame brass;

    @BeforeEach
    void setup() {
        gameList = new GameList();

        catan = new BoardGame("Catan", 1, 3, 4, 60, 90, 2.5, 200, 7.2, 1995);
        azul = new BoardGame("Azul", 2, 2, 4, 30, 45, 1.8, 50, 7.8, 2017);
        pandemic = new BoardGame("Pandemic", 3, 2, 4, 45, 60, 2.4, 120, 7.6, 2008);
        brass = new BoardGame("Brass: Birmingham", 4, 2, 4, 90, 120, 3.9, 1, 8.6, 2018);
    }

    @Test
    void testGetGameNamesSortedCaseInsensitive() {
        // 乱序加入
        Stream<BoardGame> filtered = Stream.of(pandemic, catan, azul);
        gameList.addToList("all", filtered);

        assertEquals(List.of("Azul", "Catan", "Pandemic"), gameList.getGameNames());
    }

    @Test
    void testNoDuplicatesBasedOnEquals() {
        Stream<BoardGame> filtered = Stream.of(catan);
        gameList.addToList("all", filtered);

        // equals 基于 name+id（其他字段被排除）
        BoardGame catanSameId = new BoardGame("Catan", 1, 9, 9, 1, 1, 9.9, 9, 9.9, 2099);
        gameList.addToList("all", Stream.of(catanSameId));

        assertEquals(1, gameList.count());
    }

    @Test
    void testAddByNamePriority() {
        Stream<BoardGame> filtered = Stream.of(azul, catan);

        gameList.addToList("catan", filtered); // 忽略大小写
        assertEquals(List.of("Catan"), gameList.getGameNames());
    }

    @Test
    void testAddByIndexAndRange() {
        // 过滤列表按名字排序应该是：Azul, Brass, Catan, Pandemic
        List<BoardGame> filteredList = List.of(pandemic, catan, brass, azul);

        gameList.addToList("1", filteredList.stream()); // add Azul
        assertEquals(List.of("Azul"), gameList.getGameNames());

        gameList.addToList("2-3", filteredList.stream()); // add Brass & Catan
        assertEquals(List.of("Azul", "Brass: Birmingham", "Catan"), gameList.getGameNames());
    }

    @Test
    void testAddAll() {
        gameList.addToList("all", Stream.of(azul, catan, pandemic));
        assertEquals(3, gameList.count());
    }

    @Test
    void testAddInvalidThrows() {
        List<BoardGame> filteredList = List.of(azul, catan);

        assertThrows(IllegalArgumentException.class, () -> gameList.addToList("", filteredList.stream()));
        assertThrows(IllegalArgumentException.class, () -> gameList.addToList("0", filteredList.stream()));
        assertThrows(IllegalArgumentException.class, () -> gameList.addToList("3", filteredList.stream()));   // out of range
        assertThrows(IllegalArgumentException.class, () -> gameList.addToList("2-1", filteredList.stream())); // bad range
        assertThrows(IllegalArgumentException.class, () -> gameList.addToList("NoSuchGame", filteredList.stream()));
    }

    @Test
    void testRemoveByNameIndexRangeAndAll() {
        gameList.addToList("all", Stream.of(azul, catan, pandemic, brass));
        assertEquals(4, gameList.count());

        // remove by name
        gameList.removeFromList("pandemic");
        assertEquals(List.of("Azul", "Brass: Birmingham", "Catan"), gameList.getGameNames());

        // remove by index (sorted list index 1-based)
        // 当前顺序：Azul(1), Brass(2), Catan(3)
        gameList.removeFromList("2"); // remove Brass
        assertEquals(List.of("Azul", "Catan"), gameList.getGameNames());

        // remove by range
        // 当前顺序：Azul(1), Catan(2)
        gameList.removeFromList("1-1"); // remove Azul
        assertEquals(List.of("Catan"), gameList.getGameNames());

        // remove all
        gameList.removeFromList("all");
        assertEquals(0, gameList.count());
    }

    @Test
    void testRemoveInvalidThrows() {
        gameList.addToList("all", Stream.of(azul, catan));

        assertThrows(IllegalArgumentException.class, () -> gameList.removeFromList(""));
        assertThrows(IllegalArgumentException.class, () -> gameList.removeFromList("0"));
        assertThrows(IllegalArgumentException.class, () -> gameList.removeFromList("3"));     // out of range
        assertThrows(IllegalArgumentException.class, () -> gameList.removeFromList("2-9"));   // out of range
        assertThrows(IllegalArgumentException.class, () -> gameList.removeFromList("NoSuchGame"));
    }
}
