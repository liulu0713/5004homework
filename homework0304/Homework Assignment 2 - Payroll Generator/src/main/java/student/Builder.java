package student;

import java.util.HashMap;
import java.util.Map;

/**
 *This is a static class (essentially functions) that will help you build objects from CSV strings.
 *These objects are then used in the rest of the program. Often these builders are associated
 *with the objects themselves and the concept of a factory, but we placed
 *them here to keep the code clean (and to help guide you).
 *
 */
public final class Builder {

    private Builder() {
    }

    /**
     * Builds an employee object from a CSV string.
     *You may end up checking the type of employee (hourly or salary) by looking at the first element of the CSV string. Then building an object specific to that type
     * @param csv the CSV string
     * @return the employee object
     */
    public static IEmployee buildEmployeeFromCSV(String csv) {
        if (csv == null) {
            return null;
        }

        String trimmed = csv.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        String[] parts = trimmed.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        // Skip header row
        // If this is a header row, skip it — column order is fixed per IEmployee.toCSV() contract.
        if (parts[0].equalsIgnoreCase("employee_type")) {
            return null;
        }

        if (parts.length < 7) {
            return null;
        }
        // CSV column order (per FileUtil.EMPLOYEE_HEADER):
        // employee_type, name, ID, payRate, pretaxDeductions, YTDEarnings, YTDTaxesPaid
        String type = parts[0];
        String name = parts[1];
        String id = parts[2];
        double payRate = Double.parseDouble(parts[3]);
        double pretaxDeductions = Double.parseDouble(parts[4]);
        double ytdEarnings = Double.parseDouble(parts[5]);
        double ytdTaxesPaid = Double.parseDouble(parts[6]);

        if (type.equalsIgnoreCase("SALARY")) {
            return new SalaryEmployee(name, id, payRate, ytdEarnings, ytdTaxesPaid,pretaxDeductions);
        }
        if (type.equalsIgnoreCase("HOURLY")) {
            return new HourlyEmployee(name, id, payRate, ytdEarnings, ytdTaxesPaid,pretaxDeductions);
        }
        return null;
    }
    /**
     * Converts a time card from a CSV string.
     *
     * @param csv CSV line representing a time card
     * @return a TimeCard object, or null if the line is not data
     */
    public static ITimeCard buildTimeCardFromCSV(String csv) {
        if (csv == null) {
            return null;
        }

        String trimmed = csv.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        String[] parts = trimmed.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length > 0 && parts[0].equalsIgnoreCase("employee_id")) {
            return null; // skip header
        }

        if (parts.length < 2) {
            return null;
        }

        String employeeId = parts[0];
        double hoursWorked = Double.parseDouble(parts[1]);

        return new TimeCard(employeeId, hoursWorked);
    }
}
