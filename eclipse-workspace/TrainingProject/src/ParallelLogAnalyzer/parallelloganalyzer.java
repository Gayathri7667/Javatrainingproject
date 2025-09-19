package ParallelLogAnalyzer;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

class LogEntry {
    int userId;
    String url;
    int responseTime;

    public LogEntry(int userId, String url, int responseTime) {
        this.userId = userId;
        this.url = url;
        this.responseTime = responseTime;
    }
}

class LogAnalysisResult {
    Map<Integer, Integer> userRequestCounts = new HashMap<>();
    Map<String, Integer> urlHits = new HashMap<>();
    long totalResponseTime = 0;
    long totalRequests = 0;

    // Merge results from subtasks
    public void merge(LogAnalysisResult other) {
        other.userRequestCounts.forEach(
            (user, count) -> userRequestCounts.merge(user, count, Integer::sum)
        );
        other.urlHits.forEach(
            (url, count) -> urlHits.merge(url, count, Integer::sum)
        );
        this.totalResponseTime += other.totalResponseTime;
        this.totalRequests += other.totalRequests;
    }
}

class LogAnalyzerTask extends RecursiveTask<LogAnalysisResult> {
    private static final int THRESHOLD = 5000; // adjust for splitting
    private final List<LogEntry> logs;
    private final int start;
    private final int end;

    public LogAnalyzerTask(List<LogEntry> logs, int start, int end) {
        this.logs = logs;
        this.start = start;
        this.end = end;
    }

    @Override
    protected LogAnalysisResult compute() {
        if (end - start <= THRESHOLD) {
            // Sequential processing
            LogAnalysisResult result = new LogAnalysisResult();
            for (int i = start; i < end; i++) {
                LogEntry log = logs.get(i);
                result.userRequestCounts.merge(log.userId, 1, Integer::sum);
                result.urlHits.merge(log.url, 1, Integer::sum);
                result.totalResponseTime += log.responseTime;
                result.totalRequests++;
            }
            return result;
        } else {
            // Split task
            int mid = (start + end) / 2;
            LogAnalyzerTask leftTask = new LogAnalyzerTask(logs, start, mid);
            LogAnalyzerTask rightTask = new LogAnalyzerTask(logs, mid, end);
            leftTask.fork();
            LogAnalysisResult rightResult = rightTask.compute();
            LogAnalysisResult leftResult = leftTask.join();
            leftResult.merge(rightResult);
            return leftResult;
        }
    }
}

public class parallelloganalyzer {
    public static void main(String[] args) {
        // 1. Generate synthetic log data
        List<LogEntry> logs = generateSyntheticLogs(50_000);

        // 2. Use ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();
        LogAnalyzerTask task = new LogAnalyzerTask(logs, 0, logs.size());
        LogAnalysisResult result = pool.invoke(task);

        // 3. Display results
        printResults(result);
    }

    private static List<LogEntry> generateSyntheticLogs(int count) {
        Random random = new Random();
        String[] urls = {"/home", "/products/123", "/products/456", "/checkout", "/cart", "/login", "/profile"};

        List<LogEntry> logs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int userId = 1000 + random.nextInt(50); // 50 users
            String url = urls[random.nextInt(urls.length)];
            int responseTime = 50 + random.nextInt(1000); // between 50-1050 ms
            logs.add(new LogEntry(userId, url, responseTime));
        }
        return logs;
    }

    private static void printResults(LogAnalysisResult result) {
        System.out.println("=== Parallel Log Analysis Results ===");

        // Top 5 URLs
        System.out.println("\nTop 5 URLs:");
        result.urlHits.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .forEach(e -> System.out.println(e.getKey() + " → " + e.getValue() + " hits"));

        // User Request Counts
        System.out.println("\nUser Request Counts:");
        result.userRequestCounts.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> System.out.println("userId=" + e.getKey() + " → " + e.getValue() + " requests"));

        // Average Response Time
        double avgResponseTime = (result.totalRequests == 0) ? 0 :
                (double) result.totalResponseTime / result.totalRequests;
        System.out.println("\nAverage Response Time: " + String.format("%.2f", avgResponseTime) + " ms");
    }
}
