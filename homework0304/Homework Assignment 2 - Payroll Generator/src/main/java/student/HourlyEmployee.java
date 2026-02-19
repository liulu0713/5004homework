package student;

import java.math.BigDecimal;

//import static jdk.internal.org.jline.utils.Colors.s;
/** Represents an hourly employee. */
public class HourlyEmployee extends AbstractEmployee {
    /**
     * Constructs an hourly employee.
     *
     * @param name employee name
     * @param id employee id
     * @param payRate hourly pay rate
     * @param pretaxDeductions pretax deductions
     * @param ytdEarnings year-to-date earnings
     * @param ytdTaxesPaid year-to-date taxes paid
     */
    public HourlyEmployee(String name, String id,
                          double payRate, double pretaxDeductions, double ytdEarnings, double ytdTaxesPaid) {
        super(name, id, payRate, pretaxDeductions, ytdEarnings, ytdTaxesPaid);
    }
    /** Calculates gross pay including overtime rules. */
    @Override
    protected BigDecimal calculateGrossPay(double hoursWorked) {
        BigDecimal rate = BigDecimal.valueOf(getPayRate());

        if (hoursWorked < 40) {
            return rate.multiply(BigDecimal.valueOf(hoursWorked));
        }

        BigDecimal regular = rate.multiply(BigDecimal.valueOf(40));
        BigDecimal overtime = rate.multiply(BigDecimal.valueOf(1.5))
                .multiply(BigDecimal.valueOf(hoursWorked - 40));
        return regular.add(overtime);
    }
    /** get Employeetype. */
    @Override
    public String getEmployeeType() {
        return "HOURLY";
    }
    /** get strings. */
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

