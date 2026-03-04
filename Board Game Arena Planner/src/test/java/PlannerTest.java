import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import student.BoardGame;
import student.IPlanner;
import student.Planner;

public class PlannerTest {

    @Test
    public void testProgressiveFilteringCanCauseEmptyForNameContains() {
        Set<BoardGame> games = new HashSet<>();
        games.add(new BoardGame("Go Fish", 2, 2, 10, 20, 120, 3.0, 200, 6.5, 2001));
        games.add(new BoardGame("Monopoly", 8, 6, 10, 20, 1000, 1.0, 800, 5.0, 2007));

        IPlanner planner = new Planner(games);

        planner.filter("minPlayers>=6").toList();

        List<String> actual =
                planner.filter("name~=go")
                        .map(BoardGame::getName)
                        .toList();

        assertEquals(List.of(), actual);
    }
}




