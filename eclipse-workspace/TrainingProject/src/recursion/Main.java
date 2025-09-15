package recursion;

public class Main {

    // -------------------- (A) Directory Depth Finder --------------------
    public static int directoryDepthRecursive(String path) {
        if (path == null || path.isEmpty()) return 0;
        int slashIndex = path.indexOf('/');
        if (slashIndex == -1) {
            return 1; // last segment
        } else {
            return 1 + directoryDepthRecursive(path.substring(slashIndex + 1));
        }
    }

    public static int directoryDepthIterative(String path) {
        if (path == null || path.isEmpty()) return 0;
        int depth = 0;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                depth++;
            }
        }
        return depth + 1; // segments = slashes + 1
    }

    public static int maxDirectoryDepthRecursive(String[] paths) {
        int max = 0;
        for (String p : paths) {
            max = Math.max(max, directoryDepthRecursive(p.startsWith("/") ? p.substring(1) : p));
        }
        return max;
    }

    public static int maxDirectoryDepthIterative(String[] paths) {
        int max = 0;
        for (String p : paths) {
            max = Math.max(max, directoryDepthIterative(p.startsWith("/") ? p.substring(1) : p));
        }
        return max;
    }

    // -------------------- (B) Word Occurrence Counter --------------------
    public static int wordCountRecursive(String text, String word, int index) {
        if (index >= text.length()) return 0;
        int count = 0;

        // Check if word matches starting here
        if (index + word.length() <= text.length()) {
            boolean match = true;
            for (int i = 0; i < word.length(); i++) {
                char c1 = text.charAt(index + i);
                char c2 = word.charAt(i);
                // Enhanced switch for case-insensitive comparison
                switch (Character.toLowerCase(c1)) {
                    case 'a','b','c','d','e','f','g','h','i','j','k','l','m',
                         'n','o','p','q','r','s','t','u','v','w','x','y','z' -> {
                        if (Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                            match = false;
                        }
                    }
                    default -> {
                        if (c1 != c2) match = false;
                    }
                }
            }
            if (match) count = 1;
        }

        return count + wordCountRecursive(text, word, index + 1);
    }

    public static int wordCountIterative(String text, String word) {
        String[] parts = text.split("\\s+");
        int count = 0;
        for (String part : parts) {
            if (part.equalsIgnoreCase(word)) {
                count++;
            }
        }
        return count;
    }

    // -------------------- (C) Customer Queue Simulation --------------------
    public static void processQueueIterative(int[] queue) {
        System.out.println("Queue Processing Iterative:");
        outer:
        for (int id : queue) {
            if (id == 999) {
                System.out.println("Found VIP, stopping service.");
                break outer; // labeled loop
            }
            System.out.println("Serving ID: " + id);
        }
    }

    public static void processQueueRecursive(int[] queue, int index) {
        if (index >= queue.length) return;
        int id = queue[index];
        if (id == 999) {
            System.out.println("Found VIP, stopping service.");
            return;
        }
        System.out.println("Serving ID: " + id);
        processQueueRecursive(queue, index + 1);
    }

    // -------------------- (D) Nested Structure Sum --------------------
    // Represent nested arrays as Object[] where elements are either Integer or Object[]
    public static int nestedSumRecursive(Object[] arr) {
        int sum = 0;
        for (Object o : arr) {
            if (o instanceof Integer) {
                sum += (Integer) o;
            } else if (o instanceof Object[]) {
                sum += nestedSumRecursive((Object[]) o);
            }
        }
        return sum;
    }

    public static int nestedSumIterative(Object[] arr) {
        int sum = 0;
        // Iterative (limited depth for demo)
        for (Object o : arr) {
            if (o instanceof Integer) {
                sum += (Integer) o;
            } else if (o instanceof Object[]) {
                for (Object inner : (Object[]) o) {
                    if (inner instanceof Integer) {
                        sum += (Integer) inner;
                    } else if (inner instanceof Object[]) {
                        for (Object deeper : (Object[]) inner) {
                            if (deeper instanceof Integer) {
                                sum += (Integer) deeper;
                            }
                        }
                    }
                }
            }
        }
        return sum;
    }

    // -------------------- Main Demo --------------------
    public static void main(String[] args) {
        // (A) Directory Depth Finder
        String[] paths = {"/home/user/docs", "/home/user/docs/reports", "/home/user/music"};
        System.out.println("Directory Depth (Recursive): " + maxDirectoryDepthRecursive(paths));
        System.out.println("Directory Depth (Iterative): " + maxDirectoryDepthIterative(paths));

        // (B) Word Occurrence Counter
        String paragraph = "Java is great. I love java. JAVA is powerful.";
        String searchWord = "java";
        System.out.println("Occurrences of \"" + searchWord + "\" (Recursive): "
                + wordCountRecursive(paragraph, searchWord, 0));
        System.out.println("Occurrences of \"" + searchWord + "\" (Iterative): "
                + wordCountIterative(paragraph, searchWord));

        // (C) Customer Queue Simulation
        int[] queue = {101, 102, 999, 103};
        processQueueIterative(queue);
        System.out.println("Queue Processing Recursive:");
        processQueueRecursive(queue, 0);

        // (D) Nested Structure Sum
        Object[] nested = {1, new Object[]{2, 3}, new Object[]{4, new Object[]{5}}};
        System.out.println("Nested Structure Sum (Recursive): " + nestedSumRecursive(nested));
        System.out.println("Nested Structure Sum (Iterative): " + nestedSumIterative(nested));
    }
}
