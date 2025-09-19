package ThreadLocalbasedRequest;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// Utility class for ThreadLocal Request ID
class RequestContext {
    private static final ThreadLocal<String> requestIdHolder = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        requestIdHolder.set(requestId);
    }

    public static String getRequestId() {
        return requestIdHolder.get();
    }

    public static void clear() {
        requestIdHolder.remove();
    }
}

// Simulated request processor
class RequestProcessor implements Runnable {
    @Override
    public void run() {
        String requestId = UUID.randomUUID().toString(); // generate unique Request ID
        try {
            // set Request ID in ThreadLocal
            RequestContext.setRequestId(requestId);

            log("Starting request");
            simulateWork();
            log("Processing request");
            simulateWork();
            log("Finished request");

        } finally {
            // Always clear ThreadLocal to prevent memory leaks
            RequestContext.clear();
        }
    }

    private void log(String message) {
        String threadName = Thread.currentThread().getName();
        String reqId = RequestContext.getRequestId();
        System.out.printf("[%s] %s: %s%n", threadName, message, reqId);
    }

    private void simulateWork() {
        try {
            Thread.sleep(200); // simulate some work
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Main class
public class requesttrackingapp {
    public static void main(String[] args) {
        int threadPoolSize = 5;
        int totalRequests = 10;

        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(new RequestProcessor());
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All requests processed.");
    }
}
