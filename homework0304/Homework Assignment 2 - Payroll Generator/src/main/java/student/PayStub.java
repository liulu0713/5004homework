package student;

//import static jdk.internal.org.jline.utils.Colors.s;

public class PayStub implements IPayStub {
    private String employeeName;
    private double netPay;
    private double taxesPaid;
    private double ytdEarnings;
    private double ytdTaxesPaid;

    public PayStub(String employeeName,
                   double netPay,
                   double taxesPaid,
                   double ytdEarnings,
                   double ytdTaxesPaid){
        this.employeeName = employeeName;
        this.netPay = netPay;
        this.taxesPaid = taxesPaid;
        this.ytdEarnings = ytdEarnings;
        this.ytdTaxesPaid = ytdTaxesPaid;
    }

    @Override
    public double getPay(){
        return netPay;
    }

    @Override
    public double getTaxesPaid(){
        return taxesPaid;
    }

    @Override
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s", employeeName, formatDecimal(netPay),
                formatDecimal(taxesPaid), formatDecimal(ytdEarnings), formatDecimal(ytdTaxesPaid));
    }
    private String formatDecimal(double value) {
        // If the value is 1661.0, this converts it to "1661.0" instead of "1661.00"
        // If it's 1102.24, it stays "1102.24"
        String s = String.format("%.2f", value);
        if (s.endsWith("0")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
