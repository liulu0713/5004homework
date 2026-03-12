package student;

/**
 * Greeting stores a locality ID,locality name,and greeting text.
 * A format sring describing where the greeting and name go.
 * 
 * https://cs5004-khoury-lionelle.github.io/hello_world/student/package-summary.html
 * 
 */
public class Greeting {
      private final int localityID;
      private final String localityName;
      private final String asciiGreeting;
      private final String unicodeGreeting;


      private final String formatStr;

      public Greeting(int localityID, String localityName) {
          this(localityID, localityName, "Hello");
      }

      public Greeting(int localityID, String localityName, String greeting) {
          this(localityID, localityName, greeting, greeting, "%s, %%s!");
      }

    /**
     *
     * @param localityID
     * @param localityName
     * @param asciiGreeting
     * @param unicodeGreeting
     * @param formatStr
     */
      public Greeting(int localityID, String localityName, String asciiGreeting,
                      String unicodeGreeting, String formatStr) {
          this.localityID = localityID;
          this.localityName = localityName;
          this.asciiGreeting = asciiGreeting;
          this.unicodeGreeting = unicodeGreeting;
          this.formatStr = formatStr;
      }
      /** Returns the locality id number. */
      public int getLocalityID() {
          return this.localityID;
      }
      /** Returns the locality name. */
      public String getLocalityName() {
        return localityName;
      }
      /** Returns the ascii greeting string. */
      public String getAsciiGreeting() {
        return asciiGreeting;
      }
     /** Returns the Unicode greeting string. */
      public String getUnicodeGreeting() {
        return unicodeGreeting;
      }
      /** return unicode format string. */
      public String getFormatStr() {
        return getFormatStr(false);
      }
      public String getFormatStr(boolean asciiOnly) {
        String greet = asciiOnly ? asciiGreeting : unicodeGreeting;
        // greeting fill with formatStr，leave a space %s for name
        // formatStr="%s, %%s!"，greet="Hello" -> "Hello, %s!"
        return String.format(formatStr, greet);
    }
    @Override
    public String toString() {
        return String.format(
                "{localityID:%d, localityName:\"%s\", asciiGreeting:\"%s\", unicodeGreeting:\"%s\"}",
                localityID, localityName, asciiGreeting, unicodeGreeting
        );
    }
}
