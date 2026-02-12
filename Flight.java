import java.util.ArrayList;
import java.util.List;


public class Flight implements Comparable<Flight> {
    private String airline;
    private int flightNumber;
    private String airlineCode;

    public Flight(String airline, int flightNumber, String airlineCode) {
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.airlineCode = airlineCode;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public String getFlightID() {
        return airlineCode + flightNumber;
    }


    @Override
    public int compareTo(Flight o) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String toString() {
        return "Flight{" + "airline='" + airline + '\'' + ", flightNumber=" + flightNumber
                + ", airlineCode='" + airlineCode + '\'' + '}';
    }


    public static void main(String[] args) {
        Flight f1 = new Flight("Delta", 123, "DL");
        Flight f2 = new Flight("American", 456, "AA");
        Flight f3 = new Flight("United", 789, "UA");

        System.out.println(f1.compareTo(f2));
        System.out.println(f2.compareTo(f3));
        System.out.println(f3.compareTo(f1));

        List<Flight> flights = new ArrayList<>();
        // can't use list.of() only, as it returns an immutable list, so we add one to new list
        flights.addAll(List.of(f1, f2, f3));


        System.out.println(flights);

        flights.sort(null);
        System.out.println(flights);

        // TODO: Create a lambda expression to sort the flights by flight number

        System.out.println(flights);


        // TODO: Add code using a TreeSet to store the flights in order



        // TODO: Add code to store the flights in a TreeMap
        // use the flight as key, and Integer as value - storing the number of seats on the flight
    }
}
