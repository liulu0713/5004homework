import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Solutions {

    public static List<Integer> getEvens(List<Integer> numbers) {
        List<Integer> evens = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) % 2 == 0) {
                evens.add(numbers.get(i));
            }

        }
        return evens;
    }

    public static int sum(List<Integer> numbers) {
        int sum = 0;
        for (int i = 0; i < numbers.size(); i++) {
            sum += numbers.get(i);
        }
        return sum;
    }

    public static List<Integer> squared(List<Integer> numbers) {
        List<Integer> squared = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            squared.add(numbers.get(i) * numbers.get(i));
        }
        return squared;
    }

    public static List<Book> listToBooks(List<String> bookStrings) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < bookStrings.size(); i++) {
            Book tmp = Book.createFromString(bookStrings.get(i));
            books.add(tmp);
        }
        return books;
    }


    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10); // this function is a
                                                                              // quick way to build
                                                                              // a list
        List<Integer> evens = getEvens(numbers);
        System.out.println(evens);

        List<Integer> evens2 =
                numbers.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
        System.out.println(evens2);


        // reduce example

        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        int sum = integers.stream().reduce(0, (a, b) -> a + b);
        System.out.println(sum);

        int sum2 = sum(integers);
        System.out.println(sum2);

        // map examples
        List<Integer> squared = integers.stream().map(n -> n * n).collect(Collectors.toList());
        System.out.println(squared);

        List<Integer> squared2 = squared(integers);
        System.out.println(squared2);

        // map examples harder

        List<String> bookStrings = Arrays.asList("The Great Gatsby,F. Scott Fitzgerald,1925",
                "To Kill a Mockingbird,Harper Lee,1960", "1984,George Orwell,1949",
                "The Catcher in the Rye,J.D. Salinger,1951", "Beloved,Toni Morrison,1987");
        List<Book> books =
                bookStrings.stream().map(Book::createFromString).collect(Collectors.toList());
        System.out.println(books);

        List<Book> books2 = listToBooks(bookStrings);
        System.out.println(books2);

        // sort examples

        List<Integer> unsorted = Arrays.asList(5, 3, 1, 2, 4);
        List<Integer> sorted = unsorted.stream().sorted().collect(Collectors.toList());
        System.out.println(sorted);


        List<Book> sortedBooks = books.stream().sorted().collect(Collectors.toList());
        System.out.println(sortedBooks);

        // discussion challenges
        List<Book> post1950 = books.stream().filter(b -> b.getYear() > 1950).sorted()
                .collect(Collectors.toList());
        System.out.println(post1950);

        int sumOfOdds = numbers.stream().filter(n -> n % 2 != 0).reduce(0, (a, b) -> a + b);
        System.out.println(sumOfOdds);
    }

}
