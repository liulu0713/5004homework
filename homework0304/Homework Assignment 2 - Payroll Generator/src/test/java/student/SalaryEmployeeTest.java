package student; // This allows access to protected methods

import org.junit.jupiter.api.Test;
import student.SalaryEmployee;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class SalaryEmployeeTest {

    @Test
    public void testCalculateGrossPayStandard() {
        // Arrange: $48,000 salary. 48000 / 24 = 2000.00
        SalaryEmployee emp = new SalaryEmployee("Jane Doe", "123", 48000.00, 500.00, 0.0, 0.0);

        // Act: Salaried employees ignore hoursWorked
        BigDecimal expected = new BigDecimal("2000.00");
        BigDecimal actual = emp.calculateGrossPay(40.0);

        // Assert
        assertEquals(expected, actual, "Gross pay should be annual salary divided by 24");
    }

    @Test
    public void testCalculateGrossPayRounding() {
        // Arrange: $50,000 salary. 50000 / 24 = 2083.3333...
        SalaryEmployee emp = new SalaryEmployee("John Smith", "456", 50000.00, 0.0, 0.0, 0.0);

        // Act: Ensure RoundingMode.HALF_UP is working
        BigDecimal expected = new BigDecimal("2083.33");
        BigDecimal actual = emp.calculateGrossPay(0.0);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testToCSV() {
        // Arrange
        SalaryEmployee emp = new SalaryEmployee("Alice", "001", 60000.00, 100.0, 5000.0, 1200.0);

        // Act
        String expected = "SALARY,Alice,001,60000.00,100.00,5000.00,1200.00";
        String actual = emp.toCSV();

        // Assert
        assertEquals(expected, actual);
    }




    /*
    * SALARY,Nami,s193,200000,1000,17017,4983
    * */
    @Test
    public void reproduceAssertionError() {
        // Based on your debug: Salary is 100000.0
        // Based on employees.csv: Edward Elric has 100000 salary and 250 deduction
        SalaryEmployee emp = new SalaryEmployee("Edward Elric", "f103", 100000.0, 250.0, 11000.0, 2333.0);

        // Test 1: Check if PayRate is stored correctly in the super class
        // If this returns 100.0, your constructor arguments are likely swapped.
        assertEquals(100000.0, emp.getPayRate(), 0.001, "PayRate should be 100000.0");

        // Test 2: Check Gross Pay Calculation
        // 100000 / 24 = 4166.67
        BigDecimal expectedGross = new BigDecimal("4166.67");
        assertEquals(expectedGross, emp.calculateGrossPay(0.0), "Gross pay calculation mismatch");
    }
}