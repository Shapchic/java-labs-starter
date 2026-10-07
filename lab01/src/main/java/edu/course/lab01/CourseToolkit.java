package edu.course.lab01;

/**
 * Небольшие методы для первой лабораторной работы.
 */
public final class CourseToolkit {

    private CourseToolkit() {
        // Утилитарный класс не должен иметь экземпляров.
    }

    /**
     * Возвращает true, если число четное.
     */
    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    public static boolean isPrime(int number) {

        if (number < 2) {
            return false;
        }

        for (int divisor = 2; divisor * divisor <= number; divisor++) {
            if (number % divisor == 0) {
                return false;
            }
        }

        return true;
    }

    public static boolean isPalindrome(String text) {

        if (text == null) {
            throw new IllegalArgumentException();
        }

        String reversed = "";

        for (int i = text.length() - 1; i >= 0; i--) {
            reversed += text.charAt(i);
        }

        return text.equals(reversed);
    }

    public static double average(int[] values) {

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException();
        }

        int sum = 0;

        for (int value : values) {
            sum += value;
        }

        return (double) sum / values.length;
    }
}
