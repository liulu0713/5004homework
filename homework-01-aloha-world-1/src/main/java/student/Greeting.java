package student;



/**
 * Greeting stores a locality ID,locality name,and greeting text.
 * A format string describing where the greeting and name go.
 * https://cs5004-khoury-lionelle.github.io/hello_world/student/package-summary.html
 */
public class Greeting { /** The ID number representing the locality. */
   private final int localityID;
   /** The name of the locality. */
   private final String localityName;
    /** The ASCII greeting string. */
   private final String asciiGreeting;
    /** The Unicode greeting string. */
   private final String unicodeGreeting;

    /** The format string used to construct the greeting. */

    private final String formatStr;
    /**
     * Creates a Greeting with a default greeting text.
     * @param localityID the locality ID
     * @param localityName the locality name
     */
    public Greeting(int localityID, String localityName) {
          this(localityID, localityName, "Hello");
      }
    /**
     * Creates a Greeting with a specified greeting text.
     * @param localityID the locality ID
     * @param localityName the locality name
     * @param greeting the greeting text
     */
    public Greeting(int localityID, String localityName, String greeting) {
        this(localityID, localityName, greeting, greeting, "%s, %%s!");
      }

    /**
     * Creates a Greeting object with all parameters specified.
     * @param localityID the locality identifier
     * @param localityName the name of the locality
     * @param asciiGreeting the ASCII greeting string
     * @param unicodeGreeting the Unicode greeting string
     * @param formatStr the format string used to format the greeting
     */
      public Greeting(int localityID, String localityName, String asciiGreeting,
                      String unicodeGreeting, String formatStr) {
          this.localityID = localityID;
          this.localityName = localityName;
          this.asciiGreeting = asciiGreeting;
          this.unicodeGreeting = unicodeGreeting;
          this.formatStr = formatStr;
      }
      /** Returns the locality id number.
       * @return the locality ID.
       */
      public int getLocalityID() {
          return this.localityID;
      }
      /** Returns the locality name.
       * @return the locality name.
       */
      public String getLocalityName() {
        return localityName;
      }
      /** Returns the ascii greeting string.
       * @return the locality ID
       */
      public String getAsciiGreeting() {
        return asciiGreeting;
      }
     /** Returns the Unicode greeting string.
      * @return the getUnicodeGreeting.
      */
      public String getUnicodeGreeting() {
        return unicodeGreeting;
      }
      /** return unicode format string.
       * @return the unicodeGreeting.
       */
      public String getFormatStr() {
        return getFormatStr(false);
      }
    /**
     * Returns the formatted greeting string.
     * @param asciiOnly true if ASCII greeting should be used
     * @return the formatted greeting string
     */
      public String getFormatStr(boolean asciiOnly) {
        String greet = asciiOnly ? asciiGreeting : unicodeGreeting;
        // greeting fill with formatStr，leave a space %s for name
        // formatStr="%s, %%s!"，greet="Hello" -> "Hello, %s!"
        return String.format(formatStr, greet);
    }
    /**
     * Returns a string representation of the Greeting object.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return String.format(
                "{localityID:%d, localityName:\"%s\", asciiGreeting:\"%s\", unicodeGreeting:\"%s\"}",
                localityID, localityName, asciiGreeting, unicodeGreeting
        );
    }
}
