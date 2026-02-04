package student;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class SalaryEmployee extends AbstractEmployee {

    public SalaryEmployee(String name, String id, double annualSalary,
                          double pretaxDeductions,
                          double ytdEarnings, double ytdTaxesPaid) {
        super(name, id, annualSalary, pretaxDeductions, ytdEarnings, ytdTaxesPaid);
    }

    @Override
    protected BigDecimal calculateGrossPay(double hoursWorked) {
        return BigDecimal.valueOf(payRate).divide(BigDecimal.valueOf(24), 2, RoundingMode.HALF_UP);
    }

    @Override
    public String getEmployeeType() {
        return "SALARY";
    }

    @Override
    public String toCSV() {
        return String.join(",",
                getEmployeeType(), name, id,
                String.valueOf(payRate),
                String.valueOf(pretaxDeductions),
                String.format("%.2f", ytdEarnings),
                String.format("%.2f", ytdTaxesPaid));
    }
}
