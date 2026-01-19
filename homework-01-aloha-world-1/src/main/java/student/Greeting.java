package student;

/**
 * This class is a place holder which you will fully implement based on the javadoc
 * 
 * https://cs5004-khoury-lionelle.github.io/hello_world/student/package-summary.html
 * 
 */
public class Greeting {
      private int localityID;
      private String localityName;
      private String asciiGreeting;
      private String unicodeGreeting;
      private String formatStr;

      public Greeting(int localityID, String localityName) {
          this(localityID, localityName, greeting:"Hello");
      }

      public Greeting(int localityID, String localityName, String greeting) {
          this.localityID, localityName, greeting, greeting, formatStr:"%s, %%s!"
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
      public int getLocalityID() {
          return this.localityID;
      }
      public static  void main(String[ ] args){

      }
}
