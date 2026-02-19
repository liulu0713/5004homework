package student;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AbstractEmployee implements IEmployee {
    /** Employee name. */
    private String name;
    /** Employee id. */
    private String id;
    /** Employee payrate. */
    private double payRate;
    /** Employee pretax. */
    private double pretaxDeductions;
    /** Employee earnings. */
    private double ytdEarnings;
    /** Employee taxespaid. */
    private double ytdTaxesPaid;
    /** Flat payroll tax rate applied to taxable pay. */
    protected static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.2265);

    protected AbstractEmployee(
            String name,
            String id,
            double payRate,
            double pretaxDeductions,
            double ytdEarnings,
            double ytdTaxesPaid) {
        this.name = name;
        this.id = id;
        this.payRate = payRate;
        this.pretaxDeductions = pretaxDeductions;
        this.ytdEarnings = ytdEarnings;
        this.ytdTaxesPaid = ytdTaxesPaid;
    }


    protected abstract BigDecimal calculateGrossPay(double hoursWorked);

    @Override
    public IPayStub runPayroll(double hoursWorked) {
        if (hoursWorked < 0) {
            return null;
        }

        BigDecimal grossPay = calculateGrossPay(hoursWorked);
        BigDecimal pretax = BigDecimal.valueOf(pretaxDeductions);
        BigDecimal taxablePay = grossPay.subtract(pretax);
        if (taxablePay.compareTo(BigDecimal.ZERO) < 0) {
            taxablePay = BigDecimal.ZERO;
        }
        BigDecimal taxes = taxablePay.multiply(TAX_RATE);
        BigDecimal netPay = taxablePay.subtract(taxes).setScale(2, RoundingMode.HALF_UP);
        taxes = taxes.setScale(2, RoundingMode.HALF_UP);

        ytdEarnings += netPay.doubleValue();
        ytdTaxesPaid += taxes.doubleValue();

        return new PayStub(
                name, netPay.doubleValue(),
                taxes.doubleValue(),
                ytdEarnings,
                ytdTaxesPaid

        );

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public double getPayRate() {
        return payRate;
    }

    @Override
    public double getYTDEarnings() {
        return ytdEarnings;
    }

    @Override
    public double getYTDTaxesPaid() {
        return ytdTaxesPaid;
    }

    @Override
    public double getPretaxDeductions() {
        return pretaxDeductions;
    }


}









