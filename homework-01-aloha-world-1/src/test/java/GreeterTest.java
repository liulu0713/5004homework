import org.junit.jupiter.api.Test;

import student.Greeter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;




/**
 * Greeter Test Class. STUDENTS - it is safe to assume these tests
 * are correct when you are debugging your Greeter.java
 *
 * <p>In Test Driven Development, tests are written before the code is written.
 * This is to ensure that you have carefully defined the
 * requirements of each method. It also helps to ensure that the code is
 * correct, and that it is maintainable. It is also a good way to ensure
 * that the code is testable, and that it is modular.</p>
 *
 * <p>However, to many programmers, it often feels like a chicken and egg,
 * and can feel tedious. It is also often difficult to write tests for
 * code that requires interaction with the client, file system, or network.
 * We will explore a few ways do that this semester, but for now, we will
 * only focus on testing methods that are easier to test. </p>
 */
public class GreeterTest {
    // these are so i don't 'hardcode' the names in the tests

    private static final String NAME_ONE = "John";
    private static final String NAME_TWO = "Alice";

    // these are used to test the 'readonly' accessor methods. They are
    // here as a means to not keep recreating the object every method.
    private static final Greeter greeter = new Greeter(NAME_ONE, 1);
    private static final Greeter greeter2 = new Greeter(NAME_ONE, 1);
    private static final Greeter greeter3 = new Greeter(NAME_ONE);
    private static final Greeter greeter4 = new Greeter(NAME_TWO, 3);





    /**
     * Tests the constructor, making sure it throws an exception if the locality is out of range
     */
    @Test
    public void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Greeter(NAME_ONE, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Greeter(NAME_ONE, 5);
        });
        Greeter greeter = new Greeter(NAME_ONE, 1); // just making sure a basic one is created
        assertTrue(greeter instanceof Greeter);
        greeter = new Greeter(NAME_ONE);
        assertTrue(greeter instanceof Greeter);
    }

    /**
     * Test the getName method.
     */
    @Test
    public void testGetName() {
        assertEquals(NAME_ONE, greeter.getName());
    }

    /**
     * Test the getLocality method.
     */
    @Test
    public void testGetLocality() {
        assertEquals(1, greeter.getLocality());
        assertEquals(2, greeter3.getLocality()); // testing the default locality
    }

    /**
     * Test the getLocalityList static method.
     */
    @Test
    public void testGetLocalityList() {
        List<String> localityList = List.of("Hawaii", "USA", "China", "Italy");
        assertEquals(localityList, Greeter.getLocalityList());
    }

    /**
     * Test the equals and hashCode methods. Tested together due to the nature of the methods.
     */
    @Test
    public void testEquals() {
        assertEquals(true, greeter.equals(greeter2));
        assertEquals(greeter.hashCode(), greeter2.hashCode());
        assertEquals(false, greeter.equals(greeter3));
        assertNotEquals(greeter2.hashCode(), greeter3.hashCode());
    }

    /**
     * Test the toString method. Making sure it follows the correct format of
     * <pre>
     * {name:"John", locality:"Hawaii"}
     * </p>
     */
    @Test
    public void testToString() {
        assertEquals("{name:\"John\", locality:\"Hawaii\"}", greeter.toString());
        assertEquals("{name:\"Alice\", locality:\"China\"}", greeter4.toString());
    }

    /**
     * Tests the greet function, looking at various localities and different notations
     * if the locality is China.
     */
    @Test
    public void testGreet() {
        Greeter greeter = new Greeter(NAME_ONE, 1); // this is local to the method
        assertEquals("Aloha, John!", greeter.greet());
        greeter = new Greeter(NAME_ONE, 2);
        assertEquals("Hello, John!", greeter.greet());
        greeter = new Greeter(NAME_ONE, 3);
        assertEquals("John, Ni Hao!", greeter.greet(true));
        assertEquals("John, 你好!", greeter.greet(false));
        greeter = new Greeter(NAME_ONE, 4);
        assertEquals("Ciao, John!", greeter.greet());
    }

    /**
     * Tests the setLocality method, making sure it throws an exception if the locality is out of range
     */
    @Test
    public void testSetLocality() {
        Greeter greeter = new Greeter(NAME_ONE, 1); // local to the method, since we are modifying the locality
        greeter.setLocality(2);
        assertEquals(2, greeter.getLocality());
        greeter.setLocality(3);
        assertEquals(3, greeter.getLocality());
        greeter.setLocality(4);
        assertEquals(4, greeter.getLocality());

        // This is a special assert that is checking to
        // see if the method throws an exception of a certain type
        assertThrows(IllegalArgumentException.class, () -> {
            greeter.setLocality(5);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            greeter.setLocality(0);
        });

    }

}
