package EmployeePerformanceReport;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class Employee {
    private String name;
    private String department;
    private int performanceScore; // 0-100
    private int yearsOfExperience;

    public Employee(String name, String department, int performanceScore, int yearsOfExperience) {
        this.name = name;
        this.department = department;
        this.performanceScore = performanceScore;
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getPerformanceScore() { return performanceScore; }
    public int getYearsOfExperience() { return yearsOfExperience; }

    @Override
    public String toString() {
        return String.format("%s (%s) Score=%d Exp=%d", 
                name, department, performanceScore, yearsOfExperience);
    }
}

class DepartmentReport {
    private String departmentName;
    private double averagePerformanceScore;
    private int maxPerformanceScore;
    private int minPerformanceScore;
    private double averageExperience;
    private long employeeCount;

    public DepartmentReport(String departmentName, double averagePerformanceScore,
                            int maxPerformanceScore, int minPerformanceScore,
                            double averageExperience, long employeeCount) {
        this.departmentName = departmentName;
        this.averagePerformanceScore = averagePerformanceScore;
        this.maxPerformanceScore = maxPerformanceScore;
        this.minPerformanceScore = minPerformanceScore;
        this.averageExperience = averageExperience;
        this.employeeCount = employeeCount;
    }

    public String getDepartmentName() { return departmentName; }

    @Override
    public String toString() {
        return "Department: " + departmentName + "\n" +
               " Avg Performance: " + String.format("%.2f", averagePerformanceScore) + "\n" +
               " Max Performance: " + maxPerformanceScore + "\n" +
               " Min Performance: " + minPerformanceScore + "\n" +
               " Avg Experience: " + String.format("%.2f", averageExperience) + "\n" +
               " Employee Count: " + employeeCount;
    }
}

// Helper accumulator class for stats
class DepartmentAccumulator {
    String departmentName;
    int sumPerformance = 0;
    int maxPerformance = Integer.MIN_VALUE;
    int minPerformance = Integer.MAX_VALUE;
    int sumExperience = 0;
    long count = 0;

    DepartmentAccumulator(String dept) {
        this.departmentName = dept;
    }

    void add(Employee e) {
        sumPerformance += e.getPerformanceScore();
        maxPerformance = Math.max(maxPerformance, e.getPerformanceScore());
        minPerformance = Math.min(minPerformance, e.getPerformanceScore());
        sumExperience += e.getYearsOfExperience();
        count++;
    }

    DepartmentAccumulator combine(DepartmentAccumulator other) {
        sumPerformance += other.sumPerformance;
        maxPerformance = Math.max(maxPerformance, other.maxPerformance);
        minPerformance = Math.min(minPerformance, other.minPerformance);
        sumExperience += other.sumExperience;
        count += other.count;
        return this;
    }

    DepartmentReport finish() {
        return new DepartmentReport(
                departmentName,
                count == 0 ? 0.0 : (double) sumPerformance / count,
                maxPerformance == Integer.MIN_VALUE ? 0 : maxPerformance,
                minPerformance == Integer.MAX_VALUE ? 0 : minPerformance,
                count == 0 ? 0.0 : (double) sumExperience / count,
                count
        );
    }
}

public class employeereportgenerator {
    public static void main(String[] args) {
        List<Employee> employees = List.of(
            new Employee("Alice", "Engineering", 85, 4),
            new Employee("Bob", "Engineering", 90, 5),
            new Employee("Charlie", "HR", 70, 2),
            new Employee("Diana", "Engineering", 60, 1),
            new Employee("Eve", "HR", 95, 6),
            new Employee("Frank", "Sales", 80, 3)
        );

        // Custom Collector
        Collector<Employee, Map<String, DepartmentAccumulator>, Map<String, DepartmentReport>> departmentCollector =
            Collector.of(
                HashMap::new, // supplier
                (map, emp) -> {
                    map.computeIfAbsent(emp.getDepartment(), DepartmentAccumulator::new)
                       .add(emp);
                }, // accumulator
                (map1, map2) -> {
                    map2.forEach((dept, acc) ->
                        map1.merge(dept, acc, DepartmentAccumulator::combine)
                    );
                    return map1;
                }, // combiner
                (map) -> {
                    return map.entrySet().stream()
                        .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().finish()
                        ));
                }, // finisher
                Collector.Characteristics.UNORDERED
            );

        // Generate report using parallel stream
        Map<String, DepartmentReport> reports =
            employees.parallelStream().collect(departmentCollector);

        // Print reports
        reports.values().forEach(System.out::println);
    }
}
