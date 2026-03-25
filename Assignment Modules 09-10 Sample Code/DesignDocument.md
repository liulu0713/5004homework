# Domain Name Information  Part 2 - Design Document


This document is meant to provide a tool for you to demonstrate the design process. You need to work on this before you code, and after have a finished product. That way you can compare the changes, and changes in design are normal as you work through a project. It is contrary to popular belief, but we are not perfect our first attempt. We need to iterate on our designs to make them better. This document is a tool to help you do that.


## (INITIAL DESIGN): Class Diagram

Place your class diagram below. If you are using the mermaid markdown, you may include the code for it here. For a reminder on the mermaid syntax, you may go [here](https://mermaid.js.org/syntax/classDiagram.html)
```mermaid

classDiagram
class IPlanner {
<<interface>>
+filter(String) Stream~BoardGame~
+filter(String, GameData) Stream~BoardGame~
}

    class IGameList {
        <<interface>>
        +getGameNames() List~String~
        +clear()
        +addToList(String, Stream~BoardGame~)
        +removeFromList(String)
    }

    class Planner {
        -allGames : List~BoardGame~
        -currentGames : List~BoardGame~
    }

    class GameList {
        -games : Set~BoardGame~
    }

    class BoardGame {
        -name : String
        -id : int
        -minPlayers, maxPlayers : int
        -minPlayTime, maxPlayTime : int
        -difficulty : double
        -rank : int
        -averageRating : double
        -yearPublished : int
    }

    class GameData {
        <<enum>>
        NAME, ID, RATING, DIFFICULTY
        RANK, MIN_PLAYERS, MAX_PLAYERS
        MIN_TIME, MAX_TIME, YEAR
    }

    IPlanner <|.. Planner
    IGameList <|.. GameList
    Planner --> BoardGame
    GameList --> BoardGame
    Planner --> GameData


```


## (INITIAL DESIGN): Tests to Write - Brainstorm

Write a test (in english) that you can picture for the class diagram you have created. This is the brainstorming stage in the TDD process.
> [!TIP]
> As a reminder, this is the TDD process we are following:
> 1. Figure out a number of tests by brainstorming (this step)
> 2. Write **one** test
> 3. Write **just enough** code to make that test pass
> 4. Refactor/update  as you go along
> 5. Repeat steps 2-4 until you have all the tests passing/fully built program

You should feel free to number your brainstorm.

1.Planner constructed with a set of games should return all games when filtered with an empty string, sorted by name ascending.
2.filter("minPlayers>3") should return only games whose minPlayers is greater than 3.
3.filter("maxPlayers<6") applied after filter("minPlayers>3") should be progressive — the second filter narrows the first result, not the full collection.
4.filter("rating>=8.0") should return only games with an average rating at or above 8.0.
5.filter("minPlayers>3,maxPlayers<6") with a comma (AND) should combine both conditions in one call.
6.Calling reset() between filters should restore the full unfiltered collection.
7.Sorting by GameData.RATING descending should put the highest-rated game first.
8.GameList.addToList("1", stream) should add the first game from the filtered stream to the list.
9.GameList.addToList("1-3", stream) should add games 1, 2, and 3 from the filtered stream.
10.GameList.addToList("all", stream) should add every game from the filtered stream.
11.GameList.removeFromList("2") should remove the second game from the current sorted list.
12.GameList.removeFromList("all") should clear the entire list.


## (FINAL DESIGN): Class Diagram

Go through your completed code, and update your class diagram to reflect the final design. It is normal that the two diagrams don't match! Rarely (though possible) is your initial design perfect. 

> [!WARNING]
> If you resubmit your assignment for manual grading, this is a section that often needs updating. You should double check with every resubmit to make sure it is up to date.


```mermaid
classDiagram
    class IPlanner {
        <<interface>>
        +filter(String) Stream~BoardGame~
        +filter(String, GameData) Stream~BoardGame~
        +filter(String, GameData, boolean) Stream~BoardGame~
        +reset()
    }
    class IGameList {
        <<interface>>
        +getGameNames() List~String~
        +clear()
        +addToList(String, Stream~BoardGame~)
        +removeFromList(String)
    }

    class Planner {
        -allGames : List~BoardGame~
        -currentGames : List~BoardGame~
        -applyOneFilter()
        -matches()
        -sortCurrent()
    }

    class GameList {
        -games : Set~BoardGame~
        -parseInputToGames()
        -sortBoardGames()
    }

    class BoardGame {
        -name, id : String/int
        -minPlayers, maxPlayers : int
        -minPlayTime, maxPlayTime : int
        -difficulty, averageRating : double
        -rank, yearPublished : int
        +toStringWithInfo(GameData)
    }

    class GameData {
        <<enum>>
        NAME, ID, RATING, DIFFICULTY
        RANK, MIN_PLAYERS, MAX_PLAYERS
        MIN_TIME, MAX_TIME, YEAR
        +fromColumnName() GameData$
        +fromString() GameData$
    }

    class Operations {
        <<enum>>
        EQUALS, NOT_EQUALS, GREATER_THAN
        LESS_THAN, GREATER_THAN_EQUALS
        LESS_THAN_EQUALS, CONTAINS
        +getOperatorFromStr() Operations$
    }

    class GUIView {
        +setController(GUIController)
        +updateResultsTable()
        +updateMyList()
        +getSelectedSortColumn() GameData
    }

    class GUIController {
        -planner : IPlanner
        -gameList : IGameList
        -lastFilteredGames : List~BoardGame~
        +handleApplyFilter()
        +handleAddToList()
        +handleRemoveFromList()
        +handleDoubleClickAdd()
    }

    class GamesLoader {
        +loadGamesFile(String)$ Set~BoardGame~
    }

    class BGArenaGui {
        +main(String[])$
        -launchGui()$
    }

    class ConsoleApp {
        -gameList : IGameList
        -planner : IPlanner
        +start()
    }

    IPlanner <|.. Planner
    IGameList <|.. GameList
    Planner --> BoardGame : filters/sorts
    Planner --> GameData : column lookup
    Planner --> Operations : operator parsing
    GameList --> BoardGame : stores
    GUIController --> IPlanner : calls filter/reset
    GUIController --> IGameList : calls add/remove
    GUIController --> GUIView : pushes updates
    GUIView --> GUIController : event callbacks
    BGArenaGui ..> GUIController : creates
    BGArenaGui ..> GamesLoader : calls
    GamesLoader ..> BoardGame : constructs
    ConsoleApp --> IPlanner : calls filter
    ConsoleApp --> IGameList : manages list
    
```


## (FINAL DESIGN): Reflection/Retrospective

> [!IMPORTANT]
> The value of reflective writing has been highly researched and documented within computer science, from learning to information to showing higher salaries in the workplace. For this next part, we encourage you to take time, and truly focus on your retrospective.

Take time to reflect on how your design has changed. Write in *prose* (i.e. do not bullet point your answers - it matters in how our brain processes the information). Make sure to include what were some major changes, and why you made them. What did you learn from this process? What would you do differently next time? What was the most challenging part of this process? For most students, it will be a paragraph or two.



The initial design provided a basic structure, but some parts turned out to be more complex during implementation.

One major change was the introduction of an Operation enum to handle filter operators like >, <, and ==. At first, I planned to parse filter expressions using simple if/else statements, but this quickly became messy. Creating a dedicated enum made the code cleaner and easier to manage. 

Another important addition was the MVC structure (GUIView, GUIController, and BGArenaGui), which was not included in the initial design. 

The GameList implementation was also more complex than expected. Supporting features like adding by index, ranges, and the keyword "all" required extra parsing logic. In addition, since the underlying data structure is a HashSet, I had to sort the list before performing index-based operations to ensure consistent behavior.

Overall, this project showed me that while interface design is important, many implementation details only become clear during coding. Next time, I would spend more time planning how features like filter parsing work before starting implementation.