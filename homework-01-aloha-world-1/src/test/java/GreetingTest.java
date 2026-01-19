import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import student.Greeting;

/**
 * Class to test the Greeting class. STUDENTS, you can assume the methods in this class are correct.
 * However, since Greeting isn't created by default, we have commented out all of these methods. You
 * should uncomment them as you work through building the Greeting class.
 *
 * You are also free to add your own tests! Just providing the unit tests this time, so you can
 * focus on java syntax more than testing for HW01. Future homeworks, you will be expected to build
 * your own test classes.
 */
public class GreetingTest {
    private Greeting usa;
    private Greeting hawaii;
    private Greeting china;

    // uncomment test below as you complete methods in Greeting.java

    /**
     * Executed before every tests, resets the values of the Greeting objects.
     */
    @BeforeEach
    public void setUp() {
      usa = new Greeting(2, "USA");
      hawaii = new Greeting(1, "Hawaii", "Aloha");
      china = new Greeting(3, "China", "Ni Hao", "你好", "%%s, %s!");
    // }


    /**
     * Tests to make sure the locality ID is being returned properly.
     */
 @Test
        public void testGetLocalityID(); {
       assertEquals(2, usa.getLocalityID());
       assertEquals(1, hawaii.getLocalityID());
       assertEquals(3, china.getLocalityID());
    }

    /**
     * Tests to make sure the locality name is being returned properly.
     */
    // @Test
    // public void testGetLocalityName() {
    // assertEquals("USA", usa.getLocalityName());
    // assertEquals("Hawaii", hawaii.getLocalityName());
    // assertEquals("China", china.getLocalityName());
    // }

    /**
     * Tests to make sure the ascii greeting is being returned properly.
     */
    // @Test
    // public void testGetAsciiGreeting() {
    // assertEquals("Hello", usa.getAsciiGreeting());
    // assertEquals("Aloha", hawaii.getAsciiGreeting());
    // assertEquals("Ni Hao", china.getAsciiGreeting());
    // }

    /**
     * Tests to make sure the unicode greeting is being returned properly.
     */
    // @Test
    // public void testGetUnicodeGreeting() {
    // assertEquals("Hello", usa.getUnicodeGreeting());
    // assertEquals("Aloha", hawaii.getUnicodeGreeting());
    // assertEquals("你好", china.getUnicodeGreeting()); // only changes for this one, but worth
    // checking
    // }

    /**
     * Tests to make sure the format string is being returned properly.
     */
    // @Test
    // public void testGetFormatStr() {
    // System.out.println(china.getFormatStr());
    // assertEquals("%s, 你好!", china.getFormatStr());
    // assertEquals("%s, Ni Hao!", china.getFormatStr(true));
    // assertEquals("Hello, %s!", usa.getFormatStr());
    // assertEquals("Aloha, %s!", hawaii.getFormatStr());
    // }

    /**
     * Tests to make sure the toString method is working properly.
     */
    // @Test
    // public void testToString() {
    // assertEquals("{localityID:2, localityName:\"USA\", asciiGreeting:\"Hello\",
    // unicodeGreeting:\"Hello\"}", usa.toString());
    // assertEquals("{localityID:1, localityName:\"Hawaii\", asciiGreeting:\"Aloha\",
    // unicodeGreeting:\"Aloha\"}", hawaii.toString());
    // assertEquals("{localityID:3, localityName:\"China\", asciiGreeting:\"Ni Hao\",
    // unicodeGreeting:\"你好\"}", china.toString());
    // }

    /**
     * Tests to make sure the format string is formatted properly to generate a greeting.
     */
    // @Test
    // public void testGreet() {
    // assertEquals("Hello, John!", String.format(usa.getFormatStr(),"John"));
    // assertEquals("Aloha, John!", String.format(hawaii.getFormatStr(), "John"));
    // assertEquals("John, 你好!", String.format(china.getFormatStr(), "John"));
    // assertEquals("John, Ni Hao!", String.format(china.getFormatStr(true), "John"));
    // }

}
