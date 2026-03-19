package student;/*
 * Students, build off this class. We are providing one sample test case as file reading is new to
 * you.
 * 
 * NOTE: you may end up changing this completely depending on how you setup your project.
 * 
 * we are just using .main() as we know that is an entry point that we specified.
 * 
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPayrollGenerator {

    @TempDir
    static Path tempDir;


    @Test
    public void testFinalPayStub() throws IOException {
        // copy employees.csv into tempDir
        Path employees = tempDir.resolve("employees.csv");
        Files.copy(Paths.get("resources/employees.csv"), employees);

        // get the path of the paystubs.csv
        Path payStubs = tempDir.resolve("paystubs.csv");



        String[] args = {"-e", employees.toString(), "-t", "resources/time_cards.csv", // allowed,
                                                                                       // this isn't
                                                                                       // modified -
                                                                                       // so safe
                "-o", payStubs.toString()};

        // run main method
        PayrollGenerator.main(args);



        String expectedPayStubs = Files
                .readString(Paths.get("resources/original/pay_stubs_solution_to_original.csv"));

        String actualPayStubs = Files.readString(payStubs);

        assertEquals(expectedPayStubs, actualPayStubs);


        // you could also read lines and compared the lists


    }
    @Test
    public void testBuilderParseSalaryEmployee() {
        String csv = "SALARY,Jane Doe,S001,60000.00,200.00,0.00,0.00";
        IEmployee emp = Builder.buildEmployeeFromCSV(csv);
        assertNotNull(emp);
        assertEquals("SALARY", emp.getEmployeeType());
        assertEquals("Jane Doe", emp.getName());
        assertEquals("S001", emp.getID());
        assertEquals(60000.0, emp.getPayRate(), 0.001);
        assertEquals(200.0, emp.getPretaxDeductions(), 0.001);
    }

    @Test
    public void testBuilderParseHourlyEmployee() {
        String csv = "HOURLY,Bob Builder,H001,20.00,100.00,500.00,120.00";
        IEmployee emp = Builder.buildEmployeeFromCSV(csv);
        assertNotNull(emp);
        assertEquals("HOURLY", emp.getEmployeeType());
        assertEquals("Bob Builder", emp.getName());
        assertEquals("H001", emp.getID());
        assertEquals(20.0, emp.getPayRate(), 0.001);
    }

    @Test
    public void testBuilderReturnsNullForHeaderRow() {
        String csv = "employee_type,name,ID,payRate,pretaxDeductions,YTDEarnings,YTDTaxesPaid";
        assertNull(Builder.buildEmployeeFromCSV(csv));
    }

    @Test
    public void testBuilderReturnsNullForNullInput() {
        assertNull(Builder.buildEmployeeFromCSV(null));
    }

    @Test
    public void testBuilderReturnsNullForEmptyString() {
        assertNull(Builder.buildEmployeeFromCSV(""));
    }

    @Test
    public void testBuilderReturnsNullForBlankString() {
        assertNull(Builder.buildEmployeeFromCSV("   "));
    }

    @Test
    public void testBuilderReturnsNullForUnknownType() {
        String csv = "CONTRACTOR,Alice,C001,50.00,0.00,0.00,0.00";
        assertNull(Builder.buildEmployeeFromCSV(csv));
    }

    @Test
    public void testBuilderCaseInsensitiveType() {
        // Type matching should be case-insensitive
        String csvLower = "salary,Jane Doe,S001,60000.00,200.00,0.00,0.00";
        IEmployee emp = Builder.buildEmployeeFromCSV(csvLower);
        assertNotNull(emp, "Builder should handle lowercase 'salary'");
        assertEquals("SALARY", emp.getEmployeeType());
    }

    // -------------------------------------------------------------------------
    // Builder - buildTimeCardFromCSV
    // -------------------------------------------------------------------------

    @Test
    public void testBuilderParseTimeCard() {
        String csv = "H001,40.0";
        ITimeCard card = Builder.buildTimeCardFromCSV(csv);
        assertNotNull(card);
        assertEquals("H001", card.getEmployeeID());
        assertEquals(40.0, card.getHoursWorked(), 0.001);
    }

    @Test
    public void testBuilderTimeCardReturnsNullForHeaderRow() {
        String csv = "employee_id,hours_worked";
        assertNull(Builder.buildTimeCardFromCSV(csv));
    }

    @Test
    public void testBuilderTimeCardReturnsNullForNull() {
        assertNull(Builder.buildTimeCardFromCSV(null));
    }

    @Test
    public void testBuilderTimeCardReturnsNullForEmpty() {
        assertNull(Builder.buildTimeCardFromCSV(""));
    }

    @Test
    public void testBuilderTimeCardWithNegativeHours() {
        // Builder should still parse it; runPayroll() handles the negative skip
        String csv = "H001,-5.0";
        ITimeCard card = Builder.buildTimeCardFromCSV(csv);
        assertNotNull(card);
        assertEquals(-5.0, card.getHoursWorked(), 0.001);
    }

    // -------------------------------------------------------------------------
    // PayStub output
    // -------------------------------------------------------------------------

    @Test
    public void testPayStubCSVFormat() {
        // Verify the CSV output format of a PayStub independent of file I/O
        SalaryEmployee emp = new SalaryEmployee("Jane Doe", "S001", 48000.0, 0.0, 0.0, 0.0);
        IPayStub stub = emp.runPayroll(40.0);
        assertNotNull(stub);
        String csv = stub.toCSV();
        // Format: employee_name,net_pay,taxes,ytd_earnings,ytd_taxes_paid
        assertTrue(csv.startsWith("Jane Doe,"), "PayStub CSV must start with employee name");
        String[] parts = csv.split(",");
        assertEquals(5, parts.length, "PayStub CSV must have exactly 5 fields");
    }

    @Test
    public void testPayStubGetPay() {
        HourlyEmployee emp = new HourlyEmployee("Bob Builder", "H001", 20.0, 0.0, 0.0, 0.0);
        IPayStub stub = emp.runPayroll(40.0);
        assertNotNull(stub);
        assertEquals(618.80, stub.getPay(), 0.001);
    }

    @Test
    public void testPayStubGetTaxesPaid() {
        HourlyEmployee emp = new HourlyEmployee("Bob Builder", "H001", 20.0, 0.0, 0.0, 0.0);
        IPayStub stub = emp.runPayroll(40.0);
        assertNotNull(stub);
        assertEquals(181.20, stub.getTaxesPaid(), 0.001);
    }

    // -------------------------------------------------------------------------
    // Edge-case integration: payroll is skipped for negative hours
    // -------------------------------------------------------------------------

    @Test
    public void testNegativeHoursSkipsPayroll() throws IOException {
        // Write a custom employees.csv with one hourly employee
        Path employees = tempDir.resolve("employees_neg.csv");
        Files.writeString(employees,
                "employee_type,name,ID,payRate,pretaxDeductions,YTDEarnings,YTDTaxesPaid\n" +
                        "HOURLY,Test Worker,X001,20.00,0.00,0.00,0.00\n");

        // Write a time_cards.csv with negative hours for that employee
        Path timeCards = tempDir.resolve("time_cards_neg.csv");
        Files.writeString(timeCards, "employee_id,hours_worked\nX001,-5.0\n");

        Path payStubs = tempDir.resolve("paystubs_neg.csv");

        String[] args = {"-e", employees.toString(),
                "-t", timeCards.toString(),
                "-o", payStubs.toString()};
        PayrollGenerator.main(args);

        // The output file should contain no pay stubs (only the header row)
        List<String> lines = Files.readAllLines(payStubs);
        // Only the header line should be present (no data rows for skipped employees)
        assertEquals(1, lines.size(), "No pay stubs should be written for negative-hour employees");
    }
}