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
}