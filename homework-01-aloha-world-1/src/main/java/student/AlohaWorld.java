package student;

/**
 * AlohaWorld is a simple greeter program that takes in input from the client, and greets them with
 * different greetings.
 *
 * <p>
 * This class is the "Driver" of the program. Most drivers only contain the main method, and are
 * used to start the program.
 * </p>
 *
 * <p>
 * Btw, this is a multi-line comment. It is also in a special format called Javadoc. Javadoc is a
 * special type of comment that can be used to generate documentation. It is often used to document
 * classes and methods. HTML can(and should) be included in these comment types.
 * </p>
 *
 * @author UPDATE with your name
 * @version SEMESTER_YEAR (Su2024 for Summer 24)
 **/

// this is a single line comment
// notice the class name matches the file name!
// it is a requirement in java for all files to be named after the class, and
// that
// they all contain a class.
public final class AlohaWorld {

    private AlohaWorld() {
        // this is a private constructor, it is used to prevent the class from being
        // instantiated.
        // This is a common practice in classes that are only used for static methods.
        // In this case, the only method is the main method which "drives" the program.
        // This class is often called a "Driver" for that reason.
    }

    /**
     * This is the main method. It is the entry point of the program.
     *
     * static means it can be called without creating an "instance" (object) of the class. As such
     * static is similar to a function in python.
     *
     * @param args the command line arguments, if provided, are added to this String array.
     */
    public static void main(String[] args) {
        String name = ConsoleView.getName();
        int locality = ConsoleView.getLocality();

        Greeter greeter = new Greeter(name, locality);
        ConsoleView.printGreeting(greeter.greet());

        while (ConsoleView.checkRunAgain()) {
            locality = ConsoleView.getLocality();
            greeter.setLocality(locality);
            ConsoleView.printGreeting(greeter.greet());
        }

    }
}
