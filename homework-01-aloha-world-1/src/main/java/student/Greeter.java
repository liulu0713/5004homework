package student; // the "package", in java folder structure maters and creates packages.

// for this class, everything will often be under student, as the autograder
// uses packages to keep student code separate from provided code. It is also
// good design practice to isolate your files into packages.

import java.util.List;

/**
 * Greeter holds the name of the person, and the locality of greeting to be used.
 *
 * <p>
 * The greeter class is meant to be a simple class that acts as a container for the information,
 * along with generating the proper greeting.
 * </p>
 *
 */
public class Greeter {
    /** holds the name of the person. Immutable. */
    private final String name; // final keyword indicates it is not possible to change the value of
                               // the field
    /** int value of locality. Mutable */
    private int locality;

    /** List of locality options. */
    private static List<String> localityList = List.of("Hawaii", "USA", "China", "Italy");

    /** int value of Hawaii greeting. */
    private static final int HAWAII = 1;

    /** int value of china greeting. */
    private static final int CHINA = 3;

    /** int value of Italy greeting. */
    private static final int ITALY = 4;

    /** int value of the DEFAULT locality. */
    private static final int DEFAULT_LOCALITY = 2;

    /**
     * This is the constructor for the Greeter class.
     *
     * This constructor assumes 2 as the default locality.
     * 
     * @param name of the person to greet
     */
    public Greeter(String name) {
        this(name, DEFAULT_LOCALITY); // notice we call the more detailed constructor!
    }

    /**
     * This is the constructor for the Greeter class.
     *
     * @param name name of the person to greet
     * @param locality the locality in which to greet them.
     */
    public Greeter(String name, int locality) {
        this.name = name;
        if (locality < 1 || locality > localityList.size()) {
            throw new IllegalArgumentException(
                    String.format("Locality must be between 1 and %d", localityList.size()));
        }
        this.locality = locality;
    }

    /**
     * Returns the name of the greeter.
     *
     * This is called an "accessor" method. It is a method that
     * returns the value of a private field. It is a good practice
     * to make fields private and use accessors to get the value.
     *
     * @return the name of the greeter
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the locality of the greeter.
     *
     * This is called an "accessor" method. It is a method that returns the value of a private
     * field. It is a good practice to make fields private and use accessors to get the value.
     * 
     * @return the int value of the locality
     */
    public int getLocality() {
        return locality;
    }

    /**
     * Used to (re)set the locality of the greeter.
     *
     * This is called a "mutator" method. It is a method that sets the value of a private field. It
     * is a good practice to make fields private and use mutators to set the value.
     *
     * Anything that doesn't have a mutator is a 'readonly' field.
     *
     * @param locality the int value of the locality, if out of range, throws an
     *        IllegalArgumentException
     */
    public void setLocality(int locality) {
        if (locality < 1 || locality > localityList.size()) {
            throw new IllegalArgumentException(
                    String.format("Locality must be between 1 and %d", localityList.size()));
        }
        this.locality = locality;
    }

    /**
     * This method is used to greet the user. It will return a greeting based on their set locality.
     * It will use ascii characters for the greeting.
     *
     * This is called an "overloaded" method. It is a method with the same name as another method,
     * but with different parameters. This helps simplify the code calls, and allows for more
     * flexibility in the code.
     * 
     * @return the greeting
     * @see #greet(boolean)
     */
    public String greet() {
        return greet(true); // assumes terminal printing/ascii only first
    }

    /**
     * This method is used to greet the user. It will return a greeting based on their set locality.
     * If the ascii_only flag is set to true, it will only use ascii characters. If it is set to
     * false, it will use the unicode characters for the greeting.
     *
     * <p>
     * Examples:
     * </p>
     *
     * <pre>
     *
     * {@code
     * Greeter greeter = new Greeter("Kailani", 1);
     *
     * System.out.println(greeter.greet(true)); // Aloha, Kailani!
     *
     * greeter.setLocality(3);
     *
     * System.out.println(greeter.greet(false)); // Kailani, 你好!
     * System.out.println(greeter.greet(true)); // Kailani, Ni Hao!
     *
     * greeter.setLocality(2);
     * System.out.println(greeter.greet(true)); // Hello, Kailani!
     *
     * }
     * </pre>
     *
     * Note for any locality not 1, 3, 4 it will default to "Hello, {@code<name>}!" which is the
     * default greeting.
     *
     * @param asciiOnly if true, only ascii characters will be used
     * @return the greeting. Possible options are "Hello", "Aloha", "Ni Hao", "Ciao" based on the
     *         locality, so if the locality is 1, and the name is "Kailani", it will return "Aloha,
     *         Kailani!"
     */
    public String greet(boolean asciiOnly) {
        String greeting; // default greeting
        switch (locality) {
            case HAWAII:
                greeting = String.format("Aloha, %s!", name);
                break;
            // skip case 2, it is the default greeting
            case CHINA:
                if (asciiOnly) {
                    greeting = String.format("%s, Ni Hao!", name);
                } else {
                    greeting = String.format("%s, 你好!", name);
                    // note "你好!" while is allowed in java sa unicode,
                    // most terminals don't allow non-ascii characters unless enabled
                }
                break;
            case ITALY:
                greeting = "Ciao, " + name + "!";
                break;
            default:
                greeting = String.format("Hello, %s!", name); // default greeting
        }
        return greeting;
    }

    /**
     * Returns the locality as a string. If the locality is not between 1 and localityList.size() it
     * will return "USA"
     * 
     * @return the locality as a string
     */
    private String getLocalityString() {
        if (locality < 1 || locality >= localityList.size()) {
            return "USA";
        }
        return localityList.get(locality);
    }

    /**
     * For new objects is is often a good idea to override the hashCode method.
     *
     * HashCodes are used in various data structures (like hashtables) to provide a 'unique'
     * identifier for an object. In this case as long as the name and locality are the same, the
     * hashcode will be the same.
     *
     * Java assumes that if .equals is true, the hashcodes should also be the same.
     * 
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return name.hashCode() + locality;
    }

    /**
     * For new objects is is often a good idea to override the equals method.
     *
     * In this case, we can compare two greeter objects and if the name and the locality are the
     * same, they are treated as the same object. Without this, it would actually compare the memory
     * addresses of the objects to confirm they are the same.
     *
     * @param obj the object to compare to
     * @return true if the objects are the same, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Greeter) {
            Greeter other = (Greeter) obj;
            return name.equals(other.name) && locality == other.locality;
        }
        return false;
    }

    /**
     * This method is used to convert the object to a string. Even if you are not 'printing' the
     * object, this is very common to override even for debugging purposes. It allows you do
     * something like
     *
     * <pre>
     * {@code
     *
     * Greeter greeter = new Greeter("Kailani", 1);
     * System.out.println(greeter);
     * }
     * </pre>
     *
     * and it would print out
     *
     * <pre>
     * {name:"Kailani", locality:"Hawaii"}
     * </pre>
     *
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return String.format("{name:\"%s\", locality:\"%s\"}", name, getLocalityString());
    }


    /**
     * Returns a copy of the locality list.
     * 
     * @return a copy of the locality list
     */
    public static List<String> getLocalityList() {
        return List.copyOf(localityList); // return a copy so original isn't modified
    }
}
