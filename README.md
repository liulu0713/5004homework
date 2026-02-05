# Module 05 Team Activity - Equality, Hashing, and Collections Framework

For this activity, we are going look at the purpose of .equal and .hashCode methods in Java. We will also look at the Collections Framework in Java, specifically in the context of collections that need .equals and .hashCode methods for them to work effectively. 

Additionally, to further your understanding of Java, we will cover in more detail on how to read and write files. 

## Grading
Grades for team activities will be based on attendance and notes. You must attend, and as a team you need to generate notes that we can confirm your work. Ideally, you upload the notes as a PDF to the team meeting after you build them out. 

> [!TIP] 
> Good notes become a study guide for you and your team! Make sure they include everything you need to help better understand the weekly material. 

### ⭐ Working in Teams ⭐
When working in teams, remember do not let one person do all the work. Make sure to work together, and ask questions. It is also better if different people program, and you all take turns programming for various team assignments.

## Learning Objectives
This team activity is designed to help you understand the following concepts:
* How to properly implement .equals and .hashCode methods in Java
* Describe the Set and Map interfaces in Java
* How to use the HashSet and HashMap  in Java, along how they work with Steams.
* Be able to read and write to a  File in using Java

## Equality in Java

To better understand equality in java, it is worth discussing the difference between a primitive and an object. 

* Primitive types are the basic data types in Java, such as int, char, double, and boolean. Another way to look at them is they are types that can represented by a single number in memory. 
* Objects are complex data types that are made up of multiple primitive types or objects. In memory, they are often represented by a reference to a memory location. 

Let's take the following java code as an example:

```java
class Person {
  private String name;
  public Person(String theName) {
    this.name = theName;
  }
}

public class Student extends Person {
  private int id;
  public Student(String theName, int id) {
    super(theName);
    this.id = id;
  }

  public static void main(String[] args) {
    int value = 10;
    Person e1 = new Student("Alice", 1);
    Person e2 = new Student("Bob", 2);
    Person e3 = new Student("Carol", 3);
    Person arrayOfPeople[] = {e1, e2, e3};
    
    Person e4 = new Student("Alice", 1); // same as e1?
    
    System.out.println(e1 == arrayOfPeople[0]); // what is printed here?
    System.out.println(e1 == e4); // what is printed here?
  }
}
```

Using the PythonTutor (which also happens to allow java visualizations, though a bit buggy and limited) you may have used in 5001, we are able to generate a visualization of the code above, which you can see in the included student_memory.png. Where ever there is an arrow, there is actually a memory address. 

 ### 👉🏽 Discussion
 Looking at both the memory diagrams, and the code - what do you think will be printed when the code is run? Why? 


### Implementing .equals in Java

In general, the `==` sign can only truly compare primitives or memory addresses because they compare what is exactly on the stack frame at that moment. For objects, what is on the stack frame is actually the memory address of the object. To overcome that limitation, every Object in java has both a `.equals` and `.hashCode` method. They don't do anything special or different by default, but they can be overridden to provide a more meaningful comparison.

For example, if we wanted a student with the Name and ID to be equal to another student with the same name and ID, we could override the `.equals` method in the Student class. 


:fire: Task: Implement the `.equals` method in the Student class.  Use the two templates provided. Person.java and Student.java. Notice the main
changed some from the above example, so you can better test your results.  While using `instanceof` is *alright* can you think of how you would do it using reflection? 




### Implementing .hashCode in Java

The `.hashCode` method is used to generate a unique hash code (`int` value) for an object. This is used in conjunction with the `.equals` method to determine if two objects are equal. Why not just equals? There are multiple algorithms such as a hashing algorithm that requires an int representation of an object. By having .hashCode, we can use the int representation to quickly determine if two objects are equal along with other benefits. 

> [!IMPORTANT]
> It is standard practice to always override the `.hashCode` method when you override the `.equals` method, 
> as not having them equivalency between the two causes unusual errors when dealing with built in java collections.
> These two methods along with .toString() are the most common methods to override in any
> new class you create.
>
> Always, if your .equals method returns true when comparing two objects, 
> your .hashCode method should return the same value for the two objects. 


:fire: Task: Implement the `.hashCode` method in the Student class.  Use the two templates provided. Person.java) and Student.java.

The most common way to implement the `.hashCode` method is to use the `Objects.hash` method. This method takes in a variable number of arguments and returns a hash code based on the values of the arguments. 

```java

@Override
public int hashCode() {
  return Objects.hash(name, id);
}
```

Notice that ID is a int, and name is a string. That is fine. What it does is take the hashCode of Integer(id), and the hashCode of String(name) and combines them.  You could attempt to generate a unique identifier for the student on your own, such as doing the following

```java

@Override
public int hashCode() {
  int hash = id;
  for (int i = 0; i < name.length(); i++) {
    hash += name.charAt(i);
  }
  return hash;
}
```

But then you may find some objects are equivalent that are not. For example, in the above code, the following two students end up returning the same hash code:

```java
Student s1 = new Student("jan", 1);
Student s2 = new Student("anj", 1);
```

As all the characters are the same even if they are in a different order. This is called a "Collision". In practice they are unavoidable, but the more unique the hash code, the less likely they are to occur. A topic you may further explore in CS 5008.


> [!NOTE]
> In industry is very common to use a ready made class that handles equality and hashCode for you. These class are called [EqualsBuilder](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/builder/EqualsBuilder.html) and [HashCodeBuilder](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/builder/HashCodeBuilder.html). They are part of the Apache Commons Lang library. You can see an example [here](https://www.codeproject.com/articles/143431/apache-commons-equalsbuilder-and-hashcodebuilder).
>
> Starting with Homework 05, you can OPTIONALLY use the [Apache Commons Lang Library](https://commons.apache.org/) classes in your code as we have included the commons-lang3 library in the project.

## Sets in Java

In mathematics a set contains a collection of unique elements. This was also used in python using the 'Set' data structure. In Java, the `Set` interface is used to represent a collection of unique elements. The `Set` interface is implemented by the `HashSet` class.

As such, to create a new set, you can do the following:

```java
Set<String> set = new HashSet<>();
set.add("Alice");
set.add("Bob");
set.add("Carol");

System.out.println(set);

set.add("Alice"); // what will happen?

System.out.println(set.contains("Alice"));
```

👉🏽 Discussion - Without fully knowing the code, discuss as a group what you can tell from the code. For example: What type are you storing in the set? What is the interface? What is the actual class?

Also, when we ran the code on our computer, the following output was generated:

```text
[Bob, Alice, Carol]
```

What do you notice about the order of the items? 


### :fire: Practice!

You will fine SetPractice.java in the repository. This contains a main method so you can practice with sets. 

1. Take a look at the provided code, discuss what it does. 
2. Add to the code an set that uses Person, with both Person and Student objects. Provide some examples including what happens when you try to add a duplicate. For example, the constructors looked like the following:
  ```java
    Person p1 = new Person("Alice");
    Person p2 = new Student("Alice", 1);
    Person p3 = new Student("Alice", 2);
    Person p4 = new Student("Alice", 1); // same as p2?
  ```

What happens when you add p4 to the set? Is it working the same way when you add Strings? Why or why not? (if not, double check your equal and hashCode methods)  


## Maps in Java

In python, a set was related to a dictionary. The dictionary would take key:value pairs, but all keys had to be unique. As such, the collection of keys was a set.  In java, this is very similar, but instead of calling it a dictionary, we call it a `Map`. The `Map` interface is used to represent a collection of key-value pairs. The `Map` interface is implemented by the `HashMap` class. 

As such, to create a new map, you can do the following:

```java
 Map<String, Double> map = new HashMap<>();

map.put("Apple", 3.5);
map.put("Banana", 2.0);
map.put("Cherry", 4.0);

// let's print the map

System.out.println(map);
```


👉🏽 Discussion - Without fully knowing the code, discuss as a group what you can tell from the code.  Additionally, what do you think will happen if we attempt to do a `map.put("Apple", 1.99)`?


Also, do you think the order of the items will be the same as the order it was added?

### :fire: Practice!

You will find the code in a file called MapPractice.java in the repository. This contains a main method so you can practice with maps. Run the code, was the order of the output what you expected? Why or why not? 

What happens when you add a duplicate key with a different value?


1. Go ahead and add your own examples to the file. We suggest you at least add a Map of Person keys, with `Set<String>` as values.

Yes, your value can be any valid object, including another collection! For your keys, you can use any object that has a proper .equals and .hashCode method.

### Streams with Sets

It is possible to still use streams with maps and sets. For example, lets take our set and add some additional values to it. 

```java
Set<Person> set = new HashSet<>();
Person p1 = new Person("Alice");
Person p2 = new Student("Alice", 1);
Person p3 = new Student("Alice", 2);

people.add(new Person("Bob"));
people.add(new Person("Carol"));

 people.stream().filter(p -> p.getName().equals("Alice")).forEach(System.out::println);

```

👉🏽 Discussion - What do you think will be printed when the code is run? Why? Talk about each method (`.filter`, `.forEach`) of the stream. 


### Streams with Maps

With maps are a bit more complicated, but you can still use streams. Often,
you either use `.keySet().stream()` or `.values().stream()` to get the stream of keys or values. You can also use `.entrySet().stream()` to get a stream of key-value pairs. 

```java
Map<String, Double> food = new HashMap<>();

food.put("Apple", 3.5);
food.put("Banana", 2.0);
food.put("Cherry", 4.0);


food.entrySet().stream().map(x -> x.getKey() + " costs " + x.getValue())
      .forEach(System.out::println);
 System.out.println("Food that costs more than 3.0");
 food.entrySet().stream().filter(x -> x.getValue() > 3.0)
          .map(x -> x.getKey() + " costs " + x.getValue())
          .forEach(System.out::println);
```

Make sure to run the above code, and see what happens. 

> [!NOTE] 
> A common gotcha with streams() is that they are a copy
> of the original collection. As such, if you want to modify the original 
> collection, you need to collect the stream back into a collection OR use the 
> `forEach` method on the original collection directly or use standard
> for loops. 

Speaking of for loops, you can still use it with a map.
```java
System.out.println("For each loop example");
for(Map.Entry<String, Double> entry : food.entrySet()) {
    System.out.println(entry.getKey() + " costs " + entry.getValue());
}

// or

System.out.println("For each loop example");
for(String key : food.keySet()) {
    System.out.println(key + " costs " + food.get(key));
}
```
In the case for Maps, a for:each loop is a stronger option than a for number iterated loop. 

Needless to say, there are multiple ways to handle it in java, and 
for now, you should focus on what makes the most sense to you. If you are wonder, "but what is more efficient?" the answer is, it depends on the situation.

## File I/O in Java

While not directly related to the course module, we wanted you to practice with reading and writing files in java. This is a common task in software development, and it is important to know how to do it. The logic and 'theory' is still the same that you learned in CS 5001 - Most notable

1. Protect against exceptions
2. Ideally read in as strings and then parse the information (for text files)
3. Always close the file when you are done with it. (though this is often done automatically). 

### Reading Files Pre Java 17

Pre Java 17, the most common way to read a file was to use the `BufferedReader` class. This class is used to read text from a character-input stream. It can be used to read data line by line by readLine() method. 

```java
public static void main(String[] args) {
    try {
        BufferedReader reader = new BufferedReader(new FileReader("raven.txt"));

        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line.trim());
        }
        reader.close();
        System.out.println(lines);

    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}
```
You can run the above example using BufferedReaderExample.java in the repository. Make sure raven.txt is in the same directory as the file.

### Writing Files Pre Java 17

Pre Java 17, the most common way to write a file was to use the `BufferedWriter` class. This class is used to write text to a character-output stream. It can be used to write data line by line by write() method. 

```java
public static void main(String[] args) {
    try {
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));

        writer.write("Aloha World");
        writer.newLine();
        writer.write("This is a test");

        writer.close();

    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}
```
You can run the above example using BufferedWriterExample.java in the repository. 

### Reading Files Post Java 17

Starting with Java 17, you can use the `Files` class to read and write files. The `Files` class provides a method called `readAllLines` that reads all lines from a file as a `List` of `String`. 

```java
public static void main(String[] args) {
    try {
        List<String> lines = Files.readAllLines(Path.of("raven.txt"));
        System.out.println(lines);

    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}
```
This is similar to what was done with the `BufferedReader` class, but it is a bit more concise. It is also what you was done in homework 03. You can also use the Files.lines() method to get a Stream of lines from a file if you already
plan to work with a stream (using .map or .filter for example). 


> [!NOTE]
> Using java.nio.Files (new input output) only reads the lines in as a group. If you want to read a line and process it, then read another line, you need to go back to the BufferedReader class. This is intentional. Ideally, you want to read all lines at once, and then process them. This keeps from having potential access issues with the file. However, for very large files, that may not be possible to do in a time efficient manner. What is better to use? As per usual with CS, the answer is "it depends", though for the most part, the Files class is the better option.




### Writing Files Post Java 17

Starting with Java 17, you can use the `Files` class to write files. The `Files` class provides a method called `write` that writes a `List` of `String` to a file. 

```java
public static void main(String[] args) {
    try {
        List<String> lines = new ArrayList<>();
        lines.add("Aloha World");
        lines.add("This is a test");

        Files.write(Path.of("output.txt"), lines);

    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
}
```

### :fire: Practice!

Now it is your turn to practice reading files. You will notice a file called courses.txt in the repository. This file contains a list of 
student names, and the courses they are taking. A name may show up
more than once, but the course will be different. You should use a HashMap of `<String, Set<String>>` to store the information. We are intentionally suggesting String to make it easier (as the file doesn't have student ID in it). 

This is good practice with file reading, and make sure you discuss as a group what you are doing.

You can use StudentLoader.java as a template to get started.


## 👉🏽  Design Thought Discussion

Often when designing classes, there are two approaches one can take. One is to focus on the 'goals' of the application, and design the classes around the primary ideas. The other is to focus on the data, and design the classes around the data.

In practice, it ends up being a combination of both.  

Taking the data into account (courses.txt) how would have that changed your design of Student.java if you started there?

Is it a good or bad idea to design around the data? Why or why not? 

There isn't a clear answer on this, and that is alright! It is more about understanding the trade-offs and making the best decision for the situation. A good Software Architect will be able to do both, and often they take both approaches into account when designing a system. 

Take away, if you are pounding your head on getting your data to fit your design, then it may be time to reevaluate your design. And for much of what you do, a data driven approach is really good for the 'data/model layer' of your application.


## :fire: Java Practice Problem
As part of **every** team activity, we will ask you to work on a Java Practice problem, and submit the code to the team files section (or as part of your notes). This is meant to give you practice similar to technical interviews, but also help build up your java skills. **Each team member needs to select a different problem!** But you can share/and should share answers and help each other. Remember, to learn a new language, the best thing you can do is practice! Here are some resources to find practice problems but you are not limited to them:

* [CodeHS - Java Practice](https://codehs.com/practice/java)
* [Coding Bat - Java](https://codingbat.com/java)
* [Hacker Rank - Java(Basic)](https://www.hackerrank.com/domains/java?filters%5Bskills%5D%5B%5D=Java%20%28Basic%29)
