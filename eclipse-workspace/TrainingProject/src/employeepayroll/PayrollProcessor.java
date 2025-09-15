package employeepayroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PayrollProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PayrollProcessor.class);

    public static void main(String[] args) {
        String inputFile = "employees.txt";
        String outputFile = "processed_employees.txt";

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile));
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String processed = processLine(line, lineNumber);
                    writer.write(processed);
                    writer.newLine();
                    logger.info("Successfully processed line {}: {}", lineNumber, line);
                } catch (invalidemployeerecordexception e) {
                    logger.error("Invalid record: {}", e.getMessage());
                }
            }

        } catch (IOException e) {
            logger.error("File error: {}", e.getMessage(), e);
        }
    }

    private static String processLine(String line, int lineNumber) throws invalidemployeerecordexception {
        String[] parts = line.split(",");

        if (parts.length < 4) {
            throw new invalidemployeerecordexception(lineNumber, "Record must have 4 fields");
        }

        // Validate EmployeeID
        int empId;
        try {
            empId = Integer.parseInt(parts[0].trim());
            if (empId <= 0) {
                throw new NumberFormatException("EmployeeID must be positive");
            }
        } catch (NumberFormatException e) {
            throw new invalidemployeerecordexception(lineNumber, "Invalid EmployeeID");
        }

        // Validate Name
        String name = parts[1].trim();
        if (name.isEmpty()) {
            throw new invalidemployeerecordexception(lineNumber, "Name is empty");
        }

        // Validate BasicSalary
        double basicSalary;
        try {
            basicSalary = Double.parseDouble(parts[2].trim());
            if (basicSalary <= 0) {
                throw new invalidemployeerecordexception(lineNumber, "BasicSalary must be > 0");
            }
        } catch (NumberFormatException e) {
            throw new invalidemployeerecordexception(lineNumber, "Invalid BasicSalary");
        }

        // Validate Bonus
        double bonus;
        try {
            bonus = Double.parseDouble(parts[3].trim());
        } catch (NumberFormatException e) {
            throw new invalidemployeerecordexception(lineNumber, "Invalid Bonus");
        }

        double netSalary = basicSalary + bonus;
        return empId + "," + name + "," + basicSalary + "," + bonus + "," + netSalary;
    }
}
