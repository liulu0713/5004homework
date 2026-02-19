package student;

/** Represents a pay stub entry for an employee. */
public class PayStub implements IPayStub {
    /** Employee name. */
    private final String employeeName;
    /** Net pay for the pay period. */
    private final double netPay;
    /** Taxes paid for the pay period. */
    private final double taxesPaid;
    /** Year-to-date earnings after this payroll run. */
    private final double ytdEarnings;
    /** Year-to-date taxes paid after this payroll run. */
    private final double ytdTaxesPaid;

    /**
     * Constructs a pay stub.
     *
     * @param employeeName employee name
     * @param netPay net pay for this period
     * @param taxesPaid taxes paid for this period
     * @param ytdEarnings year-to-date earnings
     * @param ytdTaxesPaid year-to-date taxes paid
     */
    public PayStub(String employeeName, double netPay, double taxesPaid,
                   double ytdEarnings, double ytdTaxesPaid) {
        this.employeeName = employeeName;
        this.netPay = netPay;
        this.taxesPaid = taxesPaid;
        this.ytdEarnings = ytdEarnings;
        this.ytdTaxesPaid = ytdTaxesPaid;
    }

    /** Gets the net pay for the pay period. */
    @Override
    public double getPay() {
        return netPay;
    }

    /** Gets the taxes paid for the pay period. */
    @Override
    public double getTaxesPaid() {
        return taxesPaid;
    }

    /** Converts this pay stub to a CSV row. */
    @Override
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s",
                employeeName,
                formatDecimal(netPay),
                formatDecimal(taxesPaid),
                formatDecimal(ytdEarnings),
                formatDecimal(ytdTaxesPaid));
    }

    /**
     * Formats a decimal value with up to 2 decimal places, trimming trailing zeros.
     *
     * @param value value to format
     * @return formatted value string
     */
    private String formatDecimal(double value) {
        String s = String.format("%.2f", value); // always 2dp first
        if (s.endsWith("00")) {
            return s.substring(0, s.length() - 1); // ".00" -> ".0"
        }
        if (s.endsWith("0")) {
            return s.substring(0, s.length() - 1); // ".50" -> ".5"
        }
        return s;
    }
}
