package student;

import java.util.HashMap;
import java.util.Map;

/**
 * Builds domain objects from CSV strings.
 */
public final class Builder {

    /** Column index mapping for employee CSV (built from header). */
    private static Map<String, Integer> employeeHeaderIndex = null;

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

        // If this is a header row, build mapping and skip it.
        if (parts.length > 0 && parts[0].equalsIgnoreCase("employee_type")) {
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < parts.length; i++) {
                map.put(parts[i].toLowerCase(), i);
            }
            employeeHeaderIndex = map;
            return null;
        }

        // If no header mapping was built, fall back to fixed positions (your current behavior).
        if (employeeHeaderIndex == null) {
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

        int typeIdx = idx("employee_type");
        int nameIdx = idx("name");
        int idIdx = firstIdx("id", "ID");
        int payIdx = firstIdx("payrate", "pay_rate", "annualsalary", "annual_salary", "salary");
        int pretaxIdx = firstIdx("pretaxdeductions", "pretax_deductions", "pretax");
        int ytdEarnIdx = firstIdx("ytdearnings", "ytd_earnings");
        int ytdTaxIdx = firstIdx("ytdtaxespaid", "ytd_taxes_paid");

        if (typeIdx < 0 || nameIdx < 0 || idIdx < 0 || payIdx < 0
                || pretaxIdx < 0 || ytdEarnIdx < 0 || ytdTaxIdx < 0) {
            return null;
        }

        String type = parts[typeIdx];
        String name = parts[nameIdx];
        String id = parts[idIdx];

        double payRateOrAnnualSalary = Double.parseDouble(parts[payIdx]);
        double pretax = Double.parseDouble(parts[pretaxIdx]);
        double ytdEarn = Double.parseDouble(parts[ytdEarnIdx]);
        double ytdTax = Double.parseDouble(parts[ytdTaxIdx]);

        if (type.equalsIgnoreCase("SALARY")) {
            return new SalaryEmployee(name, id, payRateOrAnnualSalary, pretax, ytdEarn, ytdTax);
        }
        if (type.equalsIgnoreCase("HOURLY")) {
            return new HourlyEmployee(name, id, payRateOrAnnualSalary, pretax, ytdEarn, ytdTax);
        }

        return null;
    }

    private static int idx(String key) {
        Integer v = employeeHeaderIndex.get(key.toLowerCase());
        return v == null ? -1 : v;
    }

    private static int firstIdx(String... keys) {
        for (String k : keys) {
            Integer v = employeeHeaderIndex.get(k.toLowerCase());
            if (v != null) {
                return v;
            }
        }
        return -1;
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
