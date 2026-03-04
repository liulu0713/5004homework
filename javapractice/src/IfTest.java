public class IfTest {
    public static void main(String[] args) {
        System.out.println("you need " + calc() +" years" );
    }

    public static int calc() {
       double money = 300000;
        double rate = 0.07;
        int year = 0;

        while (money < 1000000) {
            year++;
            money = money * (1 + rate);
        }
        return year;
    }
}

