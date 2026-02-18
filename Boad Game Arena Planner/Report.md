# Report

Submitted report to be manually graded. We encourage you to review the report as you read through the provided
code as it is meant to help you understand some of the concepts. 

## Technical Questions

1. What is the difference between == and .equals in java? Provide a code example of each, where they would return different results for an object. Include the code snippet using the hash marks (```) to create a code block.
   ```java
public class EqualsVsDoubleEquals {
public static void main(String[] args) {
String a = new String("hello");
String b = new String("hello");

    System.out.println(a == b);        // false (different objects)
    System.out.println(a.equals(b));   // true  (same content)

    BoardGame g1 = new BoardGame("Catan", 1, 3, 4, 60, 90, 2.5, 200, 7.2, 1995);
    BoardGame g2 = new BoardGame("Catan", 1, 9, 9, 1, 1, 9.9, 9, 9.9, 2099);

    System.out.println(g1 == g2);        // false (different objects)
    System.out.println(g1.equals(g2));   // true  (BoardGame.equals ignores many fields)
}
}


   ```



2. Logical sorting can be difficult when talking about case. For example, should "apple" come before "Banana" or after? How would you sort a list of strings in a case-insensitive manner? 

[//]: # (case insentive sorting:)
List<String> names = new ArrayList<>(List.of("apple", "Banana", "cherry"));
names.sort(String.CASE_INSENSITIVE_ORDER);
System.out.println(names); // [apple, Banana, cherry]




3. In our version of the solution, we had the following code (snippet)
    ```java
    public static Operations getOperatorFromStr(String str) {
        if (str.contains(">=")) {
            return Operations.GREATER_THAN_EQUALS;
        } else if (str.contains("<=")) {
            return Operations.LESS_THAN_EQUALS;
        } else if (str.contains(">")) {
            return Operations.GREATER_THAN;
        } else if (str.contains("<")) {
            return Operations.LESS_THAN;
        } else if (str.contains("=="))...
    ```
    Why would the order in which we checked matter (if it does matter)? Provide examples either way proving your point. 

if (str.contains(">")) return GREATER_THAN;
else if (str.contains(">=")) return GREATER_THAN_EQUALS; 



4. What is the difference between a List and a Set in Java? When would you use one over the other? 

the content in list can be the same,the can be visit by index,
set doesn't have the same content.


5. In GamesLoader.java, we use a Map to help figure out the columns. What is a map? Why would we use a Map here? 

Map is a key to value structure, we can visit by the key to find the value.


6. GameData.java is actually an `enum` with special properties we added to help with column name mappings. What is an `enum` in Java? Why would we use it for this application?


An enum in Java is a special type that represents a fixed set of constants. It is more type-safe and readable than using public static final String values, and it can also include fields and methods.




7. Rewrite the following as an if else statement inside the empty code block.
    ```java
    switch (ct) {
                case CMD_QUESTION: // same as help
                case CMD_HELP:
                    processHelp();
                    break;
                case INVALID:
                default:
                    CONSOLE.printf("%s%n", ConsoleText.INVALID);
            }
    ``` 

    ```java
    // your code here, don't forget the class name that is dropped in the switch block..
    // don't forget the class name that is dropped in the switch block..
if (ct == ConsoleText.CMD_QUESTION || ct == ConsoleText.CMD_HELP) {
    processHelp();
} else {
    CONSOLE.printf("%s%n", ConsoleText.INVALID);
}

    ```

## Deeper Thinking

ConsoleApp.java uses a .properties file that contains all the strings
that are displayed to the client. This is a common pattern in software development
as it can help localize the application for different languages. You can see this
talked about here on [Java Localization – Formatting Messages](https://www.baeldung.com/java-localization-messages-formatting).

Take time to look through the console.properties file, and change some of the messages to
another language (probably the welcome message is easier). It could even be a made up language and for this - and only this - alright to use a translator. See how the main program changes, but there are still limitations in 
the current layout. 

Post a copy of the run with the updated languages below this. Use three back ticks (```) to create a code block. 

```text
// your consoles output here
```

Now, thinking about localization - we have the question of why does it matter? The obvious
one is more about market share, but there may be other reasons.  I encourage
you to take time researching localization and the importance of having programs
flexible enough to be localized to different languages and cultures. Maybe pull up data on the
various spoken languages around the world? What about areas with internet access - do they match? Just some ideas to get you started. Another question you are welcome to talk about - what are the dangers of trying to localize your program and doing it wrong? Can you find any examples of that? Business marketing classes love to point out an example of a car name in Mexico that meant something very different in Spanish than it did in English - however [Snopes has shown that is a false tale](https://www.snopes.com/fact-check/chevrolet-nova-name-spanish/).  As a developer, what are some things you can do to reduce 'hick ups' when expanding your program to other languages?



1.GameList should not allow duplicates
If I add two BoardGame objects that are equal according to equals(), the list count should not increase.
2.GameList add "all" should add all filtered games
Using "all" should add every game from the current filtered stream.
3.Planner sort ascending and descending should work
Sorting by rating descending should return games ordered from highest to lowest rating.
4.Planner filter numeric comparison should work
Filtering with minPlayers > 3 should only return games satisfying the condition.


As a reminder, deeper thinking questions are meant to require some research and to be answered in a paragraph for with references. The goal is to open up some of the discussion topics in CS, so you are better informed going into industry. 