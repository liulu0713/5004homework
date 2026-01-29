import java.time.Instant;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class ListRunner {


    public static double runAddToFrontTest(List<Integer> list) {
        Instant start = Instant.now();

        for (int i = 0; i < 1000000; i++) {
            list.add(0, i); // add to the front
        }
        Instant end = Instant.now();


        return (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
    }


    public static double accessNthElement(List<Integer> list, int n) {
        Instant start = Instant.now();

        for (int i = 0; i < 1000000; i++) {
            list.get(n); // get the nth element
        }
        Instant end = Instant.now();
        return (end.toEpochMilli() - start.toEpochMilli()) / 1000.0;
    }


    public static void main(String[] args) {
        List<Integer> list = new LinkedList<>();
        double time = runAddToFrontTest(list);

        System.out.println("Time taken for linked list: " + time + " seconds");

        List<Integer> list2 = new ArrayList<>();

        time = runAddToFrontTest(list2); // notice same function, doesn't care for the type of list

        System.out.println("Time taken for array list: " + time + " seconds");
        // access nth element

        time = accessNthElement(list, 5000);
        System.out.println(
                "Time taken to access 5000th element in linked list: " + time + " seconds");

        time = accessNthElement(list2, 5000);
        System.out
                .println("Time taken to access 5000th element in array list: " + time + " seconds");


    }
}
