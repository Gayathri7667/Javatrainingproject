package myutils;

public class ArrayUtils {

    // Sort array ascending
    public static int[] sortAscending(int[] arr) {
        if (arr == null) return new int[0];
        int[] result = arr.clone();
        for (int i = 0; i < result.length - 1; i++) {
            for (int j = i + 1; j < result.length; j++) {
                if (result[i] > result[j]) {
                    int temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                }
            }
        }
        return result;
    }

    // Sort array descending
    public static int[] sortDescending(int[] arr) {
        if (arr == null) return new int[0];
        int[] result = arr.clone();
        for (int i = 0; i < result.length - 1; i++) {
            for (int j = i + 1; j < result.length; j++) {
                if (result[i] < result[j]) {
                    int temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                }
            }
        }
        return result;
    }

    // Find max element
    public static int findMax(int[] arr) {
        if (arr == null || arr.length == 0) throw new IllegalArgumentException("Array is empty");
        int max = arr[0];
        for (int num : arr) {
            if (num > max) max = num;
        }
        return max;
    }

    // Find min element
    public static int findMin(int[] arr) {
        if (arr == null || arr.length == 0) throw new IllegalArgumentException("Array is empty");
        int min = arr[0];
        for (int num : arr) {
            if (num < min) min = num;
        }
        return min;
    }

    // Reverse array
    public static int[] reverse(int[] arr) {
        if (arr == null) return new int[0];
        int[] result = arr.clone();
        int left = 0, right = result.length - 1;
        while (left < right) {
            int temp = result[left];
            result[left] = result[right];
            result[right] = temp;
            left++;
            right--;
        }
        return result;
    }

    // Merge two arrays
    public static int[] merge(int[] arr1, int[] arr2) {
        if (arr1 == null) arr1 = new int[0];
        if (arr2 == null) arr2 = new int[0];
        int[] merged = new int[arr1.length + arr2.length];
        int index = 0;
        for (int num : arr1) merged[index++] = num;
        for (int num : arr2) merged[index++] = num;
        return merged;
    }
}