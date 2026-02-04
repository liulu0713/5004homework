package student;

public class TimeCard implements ITimeCard {
    private String employeeId;
    private double hoursWorked;

    public TimeCard(String employeeId, double hoursWorked){
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

