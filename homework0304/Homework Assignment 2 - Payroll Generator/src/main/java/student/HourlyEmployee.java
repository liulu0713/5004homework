package student;

import java.math.BigDecimal;

//import static jdk.internal.org.jline.utils.Colors.s;

public class HourlyEmployee extends AbstractEmployee {

    public HourlyEmployee(String name, String id,
                          double payRate, double pretaxDeductions, double ytdEarnings, double ytdTaxesPaid) {
        super(name, id, payRate, pretaxDeductions, ytdEarnings, ytdTaxesPaid);
    }


    @Override
    protected BigDecimal calculateGrossPay(double hoursWorked){
        BigDecimal rate = BigDecimal.valueOf(payRate);

        if(hoursWorked < 40) {
            return rate.multiply(BigDecimal.valueOf(hoursWorked));
        }

        BigDecimal regular = rate.multiply(BigDecimal.valueOf(40));
        BigDecimal overtime = rate.multiply(BigDecimal.valueOf(1.5))
                .multiply(BigDecimal.valueOf(hoursWorked - 40));
        return regular.add(overtime);
    }

    @Override
    public String getEmployeeType() {
        return "HOURLY";
    }

    @Override
    public String toCSV() {
            return String.format("%s, %s, %s, %.2f, %.2f, %.2f, %.2f", getEmployeeType(), name, id, payRate, pretaxDeductions, ytdEarnings, ytdTaxesPaid);
    }
}

