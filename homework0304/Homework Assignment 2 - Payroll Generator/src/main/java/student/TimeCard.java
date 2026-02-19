package student;

public class TimeCard implements ITimeCard {
    /** Employee id for this time card. */
    private String employeeId;
    /** Employee hoursworked. */
    private double hoursWorked;
    /**
     * Constructs a time card.
     * @param employeeId
     * @param hoursWorked hours worked
     */
    public TimeCard(String employeeId, double hoursWorked) {
        this.employeeId = employeeId;
        this.hoursWorked = hoursWorked;
    }
    @Override
    public String getEmployeeID() {
        return employeeId;
    }

    @Override
    public double getHoursWorked() {
        return hoursWorked;
    }
}

