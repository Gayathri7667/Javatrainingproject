package myutils;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // String Utils Demo
        System.out.println("---- String Utils Demo ----");
        String messy = "   java   utility   LAB   ";
        String normalized = StringUtils.normalize(messy);
        System.out.println("Normalized: " + normalized);
        System.out.println("Title Case: " + StringUtils.toTitleCase(normalized));
        System.out.println("Is 'level' a palindrome? " + StringUtils.isPalindrome("level"));
        System.out.println("Occurrences of 'java': " +
            StringUtils.countOccurrences("java is java and java rocks", "java"));

        // Array Utils Demo
        System.out.println("\n---- Array Utils Demo ----");
        int[] arr = {55, 90, 12, 67};
        System.out.println("Original: " + Arrays.toString(arr));
        System.out.println("Ascending: " + Arrays.toString(ArrayUtils.sortAscending(arr)));
        System.out.println("Descending: " + Arrays.toString(ArrayUtils.sortDescending(arr)));
        System.out.println("Max: " + ArrayUtils.findMax(arr));
        System.out.println("Min: " + ArrayUtils.findMin(arr));
        System.out.println("Reversed: " + Arrays.toString(ArrayUtils.reverse(arr)));

        int[] arr1 = {101, 102};
        int[] arr2 = {201, 202};
        System.out.println("Merged: " + Arrays.toString(ArrayUtils.merge(arr1, arr2)));
    }
}
