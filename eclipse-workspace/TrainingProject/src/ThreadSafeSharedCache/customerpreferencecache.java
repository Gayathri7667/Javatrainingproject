package ThreadSafeSharedCache;

import java.util.concurrent.ConcurrentHashMap;

public class customerpreferencecache {

    // Thread-safe cache
    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    // Get preference (check cache first, else fetch from DB)
    public String getPreference(String customerId) {
        return cache.computeIfAbsent(customerId, id -> fetchFromDB(id));
    }

    // Update preference
    public void updatePreference(String customerId, String preference) {
        cache.put(customerId, preference);
        System.out.println("Update: Updated preference for " + customerId);
    }

    // Remove preference
    public void removePreference(String customerId) {
        cache.remove(customerId);
        System.out.println("Remove: Removed preference for " + customerId);
    }

    // Simulated DB call with 200ms delay
    private String fetchFromDB(String customerId) {
        try {
            Thread.sleep(200); // simulate DB delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "{\"theme\":\"dark\",\"lang\":\"en\",\"customer\":\"" + customerId + "\"}";
    }

    // Demo class with main()
    public static void main(String[] args) {
        customerpreferencecache cache = new customerpreferencecache();

        // First fetch -> from DB
        String first = cache.getPreference("CUST1");
        System.out.println("First fetch: " + first);

        // Second fetch -> from cache
        String second = cache.getPreference("CUST1");
        System.out.println("Second fetch (cached): " + second);

        // Update preference
        String updatedPref = "{\"theme\":\"light\",\"lang\":\"fr\",\"customer\":\"CUST1\"}";
        cache.updatePreference("CUST1", updatedPref);

        // Fetch after update
        String afterUpdate = cache.getPreference("CUST1");
        System.out.println("After update: " + afterUpdate);

        // Remove preference
        cache.removePreference("CUST1");

        // Fetch after removal (again from DB)
        String afterRemoval = cache.getPreference("CUST1");
        System.out.println("After removal, fetched again: " + afterRemoval);
    }
}
