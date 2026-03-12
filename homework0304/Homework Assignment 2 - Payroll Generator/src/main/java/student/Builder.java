package student;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds domain objects from CSV strings.
 */
public final class Builder {

    /** Column index mapping for employee CSV (built from header). */

    /** Private constructor to prevent instantiation. */
    private Builder() {
    }

    /**
     * Builds an employee object from a CSV string.
     *
     * @param line CSV line representing an employee
     * @return the employee instance, or null if the line is not data
     */
    public static IEmployee buildEmployeeFromCSV(String line) {
        if (line == null) {
            return null;
        }

        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        String[] parts = trimmed.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        // If this is a header row, skip it — column order is fixed per IEmployee.toCSV() contract.
        if (parts.length > 0 && parts[0].equalsIgnoreCase("employee_type")) {
            return null;
        }


        // Fixed-position parsing matching the column order specified by IEmployee.toCSV():
        // employee_type, name, ID, payRate, pretaxDeductions, YTDEarnings, YTDTaxesPaid

        if (parts.length < 7) {
            return null;
        }
        String type = parts[0];
        String name = parts[1];
        String id = parts[2];

        double payRateOrAnnualSalary = Double.parseDouble(parts[3]);
        double pretax = Double.parseDouble(parts[4]);
        double ytdEarn = Double.parseDouble(parts[5]);
        double ytdTax = Double.parseDouble(parts[6]);

        if (type.equalsIgnoreCase("SALARY")) {
            return new SalaryEmployee(name, id, payRateOrAnnualSalary, pretax, ytdEarn, ytdTax);
        }
        if (type.equalsIgnoreCase("HOURLY")) {
            return new HourlyEmployee(name, id, payRateOrAnnualSalary, pretax, ytdEarn, ytdTax);
        }
        return null;
    }
    /**
     * Converts a time card from a CSV string.
     *
     * @param line CSV line representing a time card
     * @return a time card object, or null if the line is not data
     */
    public static ITimeCard buildTimeCardFromCSV(String line) {
        if (line == null) {
            return null;
        }

        String trimmed = line.trim();
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
