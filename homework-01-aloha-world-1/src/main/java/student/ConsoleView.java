package student;

import java.util.Scanner; // this imports the Scanner class from the java.util package.
import java.util.List; // this imports the List class from the java.util package.

/**
 * ClientView is a class that is used to interact with the client.
 *
 * <p>
 * Every function is static, along with keeping the Scanner to system.in static. It is suggested
 * that client interaction goes through this class.
 * </p>
 */
public final class ConsoleView {

    // Scanner can read input from clients,strings, or files.
    // However, when setting the scanner to System.in, you only want to do it once in the
    // application to
    // prevent conflicts.
    /** scanner pointing towards System.in. */
    private static final Scanner SCANNER = new Scanner(System.in); // System.in is a standard input
                                                                // stream, it is used to read
                                                                // data from the keyboard.
    /** Stores the locality lists from Greeter to prevent additional copies. */
    private static final List<String> LOCALITY_OPTIONS = Greeter.getLocalityList(); // get the
                                                                                    // options from
                                                                                    // the Greeter
                                                                                    // class, doing
                                                                                    // this here, so
                                                                                    // it is only
                                                                                    // called once

    private ConsoleView() {
        // this is a private constructor, it is used to prevent the class from being instantiated.
        // This is a common practice in classes that are only used for static methods.
    }

    /**
     * Asks the client for their name.
     *
     * @return the name of the client
     */
    public static String getName() {
        // these two combined statements are similar to input("Welcome, what is your name? "), in
        // python
        System.out.print("Welcome, what is your name? "); // this is a print statement, notice the
                                                          // print vs println used below
        return SCANNER.nextLine().trim(); // this is a method call, it returns the next line of
                                          // input from the client
    }

    /**
     * Asks the client to select a locality by number. If they select an invalid number, it will
     * continue to ask them until they select a valid number.
     *
     *
     * @return the locality selected by the client
     */
    public static int getLocality() {
        System.out.println("Select a locality: ");
        for (int i = 0; i < LOCALITY_OPTIONS.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + LOCALITY_OPTIONS.get(i));
        }
        System.out.print("> ");
        String input = SCANNER.nextLine().trim();
        try {
            int val = Integer.parseInt(input); // converts the input to an integer, similar to
                                               // int(value) in python, but only goes from strings
                                               // to integers.
            if (val > 0 && val <= LOCALITY_OPTIONS.size()) {
                return val;
            } else {
                System.out.println("Invalid input, please try again.");
                return getLocality();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, please try again., Numbers only.");
            return getLocality();
        }
    }

    /**
     * Asks the client if they would like to be greeted again.
     *
     * They can respond with yes, y, no, or n. If they respond with something else will continue to
     * call itself until they respond with a valid answer.
     *
     * @return true if they want to be greeted again, false if they don't.
     */
    public static boolean checkRunAgain() {
        System.out.print("Would you like be greeted again (yes/no)? ");
        String input = SCANNER.nextLine().toLowerCase(); // converts the input to lowercase
        if (input.equals("yes") || input.equals("y")) {
            return true;
        } else if (input.equals("no") || input.equals("n")) { // || is the 'or' operator in many
                                                              // languages, && is the 'and' operator
            return false;
        } else {
            System.out.println("Invalid answer, please try again.");
            return checkRunAgain();
        }

    }

    /**
     * Currently acts as a pass through to System.out.println.
     *
     * This is a good practice to keep all print statements in one place, so that if you need to
     * change the output, you only need to change it in one place.
     *
     * @param greeting the greeting to print
     */
    public static void printGreeting(String greeting) {
        System.out.println(greeting);
    }
}
