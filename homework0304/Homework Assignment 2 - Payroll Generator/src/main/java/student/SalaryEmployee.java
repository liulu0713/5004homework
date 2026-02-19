package student;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Represents a salaried employee. */
public class SalaryEmployee extends AbstractEmployee {

    /**
     * Constructs a salaried employee.
     *
     * @param name employee name
     * @param id employee id
     * @param annualSalary annual salary
     * @param pretaxDeductions pretax deductions
     * @param ytdEarnings year-to-date earnings
     * @param ytdTaxesPaid year-to-date taxes paid
     */
    public SalaryEmployee(String name, String id, double annualSalary,
                          double pretaxDeductions, double ytdEarnings, double ytdTaxesPaid) {

        super(name, id, annualSalary, pretaxDeductions, ytdEarnings, ytdTaxesPaid);
    }

    /** Calculates gross pay for salaried employee (annualSalary / 24). */
    @Override
    protected BigDecimal calculateGrossPay(double hoursWorked) {
        return BigDecimal.valueOf(getPayRate())
                .divide(BigDecimal.valueOf(24), 2, RoundingMode.HALF_UP);
    }

    /** Returns employee type. */
    @Override
    public String getEmployeeType() {
        return "SALARY";
    }

    /** Converts employee to CSV format. */
    @Override
    public String toCSV() {
        return String.format("%s,%s,%s,%.2f,%.2f,%.2f,%.2f",
                getEmployeeType(),
                getName(),
                getID(),
                getPayRate(),
                getPretaxDeductions(),
                getYTDEarnings(),
                getYTDTaxesPaid());
    }
}
