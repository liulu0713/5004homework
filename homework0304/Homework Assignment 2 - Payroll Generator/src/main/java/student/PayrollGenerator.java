package student;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main driver for the PayrollGenerator program.
 *
 * Students, you are free to modify this file as needed, but you need to leave in the parts where we
 * can pass in the employee and payroll files as arguments.
 */
public final class PayrollGenerator {
    /** default file name for employees. */
    private static final String DEFAULT_EMPLOYEE_FILE = "resources/employees.csv";
    /** default file name for pay stub output. */
    private static final String DEFAULT_PAYROLL_FILE = "resources/pay_stubs.csv";
    /** default time card file name. */
    private static final String DEFAULT_TIME_CARD_FILE = "resources/time_cards.csv";

    /** Private constructor to prevent instantiation. */
    private PayrollGenerator() {
    }

    /**
     * Main driver for the program.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Arguments arguments = Arguments.process(args);

        List<String> employeeLines = FileUtil.readFileToList(arguments.getEmployeeFile());
        List<String> timeCards = FileUtil.readFileToList(arguments.getTimeCards());

        List<IEmployee> employees = employeeLines.stream()
                .map(Builder::buildEmployeeFromCSV)
                .filter(e -> e != null)
                .collect(Collectors.toList());

        List<ITimeCard> timeCardList = timeCards.stream()
                .map(Builder::buildTimeCardFromCSV)
                .filter(c -> c != null)
                .collect(Collectors.toList());

        List<IPayStub> payStubs = new LinkedList<>();

        for (ITimeCard card : timeCardList) {
            String cardId = card.getEmployeeID();
            if (cardId == null) {
                continue;
            }
            cardId = cardId.trim();

            IEmployee currentEmployee = null;
            for (IEmployee emp : employees) {
                String empId = emp.getID();
                if (empId != null && empId.trim().equals(cardId)) {
                    currentEmployee = emp;
                    break;
                }
            }

            if (currentEmployee != null) {
                IPayStub stub = currentEmployee.runPayroll(card.getHoursWorked());
                if (stub != null) {
                    payStubs.add(stub);
                }
            }
        }

        List<String> newEmployeeLines = employees.stream()
                .map(IEmployee::toCSV)
                .collect(Collectors.toList());
        newEmployeeLines.add(0, FileUtil.EMPLOYEE_HEADER);
        FileUtil.writeFile(arguments.getEmployeeFile(), newEmployeeLines);

        List<String> payStubLines = payStubs.stream()
                .map(IPayStub::toCSV)
                .collect(Collectors.toList());
        payStubLines.add(0, FileUtil.PAY_STUB_HEADER);
        FileUtil.writeFile(arguments.getPayrollFile(), payStubLines);
    }

    /**
     * This is an internal class. Please leave it as is/do not modify!
     */
    private static final class Arguments {
        /** sets the employeeFile argument. */
        private String employeeFile = DEFAULT_EMPLOYEE_FILE;
        /** sets the payrollFile argument. */
        private String payrollFile = DEFAULT_PAYROLL_FILE;
        /** sets the timeCards argument. */
        private String timeCards = DEFAULT_TIME_CARD_FILE;

        /** Constructor for Arguments. */
        private Arguments() {
        }

        /** @return the name of the employee file */
        public String getEmployeeFile() {
            return employeeFile;
        }

        /** @return the name of the payroll file */
        public String getPayrollFile() {
            return payrollFile;
        }

        /** @return the name of the time card file */
        public String getTimeCards() {
            return timeCards;
        }

        /** Prints the help message. */
        public void printHelp() {
            System.out.println(
                    "Usage: java student.PayrollGenerator [-e employee_file] [-t time_cards_file] [-o payroll_file]");
            System.out.println("Options:");
            System.out.println(
                    "  -e employee_file  Input file containing employee information. Default is employees.csv");
            System.out.println(
                    "  -t time_cards_file  Input file containing time card information. Default is time_cards.csv");
            System.out.println(
                    "  -o payroll_file   Output file containing payroll information. Default is pay_stubs.csv");
            System.out.println("  -h                Print this help message");
        }

        /**
         * Processes the arguments.
         *
         * @param args the arguments
         * @return an Argument object with file names added
         */
        public static Arguments process(String[] args) {
            Arguments arguments = new Arguments();
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-e")) {
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        arguments.employeeFile = args[i + 1];
                    } else {
                        System.out.println("Missing argument for -i option");
                        arguments.printHelp();
                        System.exit(1);
                    }
                } else if (args[i].equals("-t")) {
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        arguments.timeCards = args[i + 1];
                    } else {
                        System.out.println("Missing argument for -t option");
                        arguments.printHelp();
                        System.exit(1);
                    }
                } else if (args[i].equals("-o")) {
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        arguments.payrollFile = args[i + 1];
                    } else {
                        System.out.println("Missing argument for -o option");
                        arguments.printHelp();
                        System.exit(1);
                    }
                } else if (args[i].equals("-h")) {
                    arguments.printHelp();
                    System.exit(0);
                } else if (args[i].startsWith("-")) {
                    System.out.println("Unknown option: " + args[i]);
                    arguments.printHelp();
                    System.exit(1);
                }
            }
            return arguments;
        }
    }
}
