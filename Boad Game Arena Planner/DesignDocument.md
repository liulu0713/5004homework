# Board Game Arena Planner Design Document


This document is meant to provide a tool for you to demonstrate the design process. You need to work on this before you code, and after have a finished product. That way you can compare the changes, and changes in design are normal as you work through a project. It is contrary to popular belief, but we are not perfect our first attempt. We need to iterate on our designs to make them better. This document is a tool to help you do that.

If you are using mermaid markup to generate your class diagrams, you may edit this document in the sections below to insert your markup to generate each diagram. Otherwise, you may simply include the images for each diagram requested below in your zipped submission (be sure to name each diagram image clearly in this case!)


## (INITIAL DESIGN): Class Diagram 

Include a UML class diagram of your initial design for this assignment. If you are using the mermaid markdown, you may include the code for it here. For a reminder on the mermaid syntax, you may go [here](https://mermaid.js.org/syntax/classDiagram.html)

### Provided Code
classDiagram
direction LR

class IGameList {
<<interface>>
+String ADD_ALL
+List~String~ getGameNames()
+void clear()
+int count()
+void saveGame(String filename)
+void addToList(String str, Stream~BoardGame~ filtered)
+void removeFromList(String str)
}
class IPlanner {
<<interface>>
+Stream~BoardGame~ filter(String filter)
+Stream~BoardGame~ filter(String filter, GameData sortOn)
+Stream~BoardGame~ filter(String filter, GameData sortOn, boolean ascending)
+void reset()
}

class BoardGame {
+String getName()
+int getId()
+int getMinPlayers()
+int getMaxPlayers()
+int getMinPlayTime()
+int getMaxPlayTime()
+double getDifficulty()
+int getRank()
+double getRating()
+int getYearPublished()
+String toStringWithInfo(GameData col)
+boolean equals(Object obj)
+int hashCode()
}
lass GameData {
<<enum>>
NAME
ID
RATING
DIFFICULTY
RANK
MIN_PLAYERS
MAX_PLAYERS
MIN_TIME
MAX_TIME
YEAR
+String getColumnName()
+static GameData fromColumnName(String columnName)
+static GameData fromString(String name)
}

class GameList
class Planner

IGameList <|.. GameList
IPlanner  <|.. Planner
Planner --> BoardGame : filters/sorts
GameList --> BoardGame : stores
Planner --> GameData : parses/sorts
BoardGame --> GameData : toStringWithInfo
Provide a class diagram for the provided code as you read through it.  For the classes you are adding, you will create them as a separate diagram, so for now, you can just point towards the interfaces for the provided code diagram.



### Your Plans/Design

Create a class diagram for the classes you plan to create. This is your initial design, and it is okay if it changes. Your starting points are the interfaces.
classDiagram
direction LR

class GameList {
-Set~BoardGame~ games
+GameList()
+List~String~ getGameNames()
+void clear()
+int count()
+void saveGame(String filename)
+void addToList(String str, Stream~BoardGame~ filtered)
+void removeFromList(String str)
-void sortBoardGames(List~BoardGame~ list)
}

class Planner {
-List~BoardGame~ allGames
-List~BoardGame~ currentGames
+Planner(Set~BoardGame~ games)
+Stream~BoardGame~ filter(String filter)
+Stream~BoardGame~ filter(String filter, GameData sortOn)
+Stream~BoardGame~ filter(String filter, GameData sortOn, boolean ascending)
+void reset()
-List~BoardGame~ applyFilter(List~BoardGame~, GameData, Operation, String)
-boolean matches(BoardGame, GameData, Operation, String)
-double getNumeric(BoardGame, GameData)
-void sortCurrent(GameData, boolean)
}

class Operation {
<<enum>>
GREATER_EQ
LESS_EQ
NOT_EQUALS
EQUALS
CONTAINS
GREATER
LESS
+static Operation fromString(String s)
}

class IGameList {<<interface>>}
class IPlanner {<<interface>>}
class BoardGame
class GameData {<<enum>>}

IGameList <|.. GameList
IPlanner  <|.. Planner
Planner --> Operation : parses
Planner --> GameData : uses
GameList --> BoardGame : stores
Planner --> BoardGame : filters/sorts




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

1. GameList_addAll: Input "all" adds all games from the filtered stream.
2. GameList_removeByName: Remove a game by name (case-insensitive).




## (FINAL DESIGN): Class Diagram

Go through your completed code, and update your class diagram to reflect the final design. It is normal that the two diagrams don't match! Rarely (though possible) is your initial design perfect. 

For the final design, you just need to do a single diagram that includes both the original classes and the classes you added. 

> [!WARNING]
> If you resubmit your assignment for manual grading, this is a section that often needs updating. You should double check with every resubmit to make sure it is up to date.
classDiagram
direction LR

class IGameList {
<<interface>>
+String ADD_ALL
+List~String~ getGameNames()
+void clear()
+int count()
+void saveGame(String filename)
+void addToList(String str, Stream~BoardGame~ filtered)
+void removeFromList(String str)
}

class IPlanner {
<<interface>>
+Stream~BoardGame~ filter(String filter)
+Stream~BoardGame~ filter(String filter, GameData sortOn)
+Stream~BoardGame~ filter(String filter, GameData sortOn, boolean ascending)
+void reset()
}

class GameList {
-Set~BoardGame~ games
+GameList()
+List~String~ getGameNames()
+void clear()
+int count()
+void saveGame(String filename)
+void addToList(String str, Stream~BoardGame~ filtered)
+void removeFromList(String str)
-void sortBoardGames(List~BoardGame~ list)
}

class Planner {
-List~BoardGame~ allGames
-List~BoardGame~ currentGames
+Planner(Set~BoardGame~ games)
+Stream~BoardGame~ filter(String filter)
+Stream~BoardGame~ filter(String filter, GameData sortOn)
+Stream~BoardGame~ filter(String filter, GameData sortOn, boolean ascending)
+void reset()
-List~BoardGame~ applyFilter(List~BoardGame~, GameData, Operation, String)
-boolean matches(BoardGame, GameData, Operation, String)
-double getNumeric(BoardGame, GameData)
-void sortCurrent(GameData, boolean)
-String[] split(String, String)
-String removeSpaces(String)
}

class Operation {
<<enum>>
GREATER_EQ
LESS_EQ
NOT_EQUALS
EQUALS
CONTAINS
GREATER
LESS
+String token
+static Operation fromString(String s)
}

class BoardGame {
+String getName()
+int getId()
+int getMinPlayers()
+int getMaxPlayers()
+int getMinPlayTime()
+int getMaxPlayTime()
+double getDifficulty()
+int getRank()
+double getRating()
+int getYearPublished()
+String toStringWithInfo(GameData col)
+boolean equals(Object obj)
+int hashCode()
}

class GameData {
<<enum>>
NAME
ID
RATING
DIFFICULTY
RANK
MIN_PLAYERS
MAX_PLAYERS
MIN_TIME
MAX_TIME
YEAR
-String columnName
+String getColumnName()
+static GameData fromColumnName(String columnName)
+static GameData fromString(String name)
}

IGameList <|.. GameList
IPlanner  <|.. Planner
GameList o-- BoardGame : contains
Planner o-- BoardGame : filters/sorts
Planner --> GameData : parses/sorts
Planner --> Operation : parses
BoardGame --> GameData : toStringWithInfo





## (FINAL DESIGN): Reflection/Retrospective

> [!IMPORTANT]
> The value of reflective writing has been highly researched and documented within computer science, from learning to information to showing higher salaries in the workplace. For this next part, we encourage you to take time, and truly focus on your retrospective.

Take time to reflect on how your design has changed. Write in *prose* (i.e. do not bullet point your answers - it matters in how our brain processes the information). Make sure to include what were some major changes, and why you made them. What did you learn from this process? What would you do differently next time? What was the most challenging part of this process? For most students, it will be a paragraph or two.
When I began this assignment, I planned to implement the interfaces directly with minimal helper logic, 
but I quickly realized that ordering, uniqueness, and parsing introduced many edge cases. 
My design evolved to separate responsibilities more clearly: GameList manages a unique set of games 
and consistent case insensitive sorting, while Planner handles progressive filtering and sorting. 
The most challenging part was ensuring progressive filtering, 
enforcing that ID cannot be filtered or sorted, and maintaining stable sorting. 
