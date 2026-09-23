package edu.course.lab01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseToolkitTest {

    @Test
    void returnsTrueForEvenNumber() {
        boolean result = CourseToolkit.isEven(8);

        assertTrue(result);
    }

    @Test
    void returnsFalseForOddNumber() {
        boolean result = CourseToolkit.isEven(7);

        assertFalse(result);
    }

    @Test
    void returnsTrueForNegativeEvenNumber() {
        boolean result = CourseToolkit.isEven(-8);

        assertTrue(result);
    }

    @Test
    void primeNumberReturnsTrue() {
        assertTrue(CourseToolkit.isPrime(7));
    }

    @Test
    void numberLessThanTwoIsNotPrime() {
        assertFalse(CourseToolkit.isPrime(1));
    }

    @Test
    void compositeNumberReturnsFalse() {
        assertFalse(CourseToolkit.isPrime(12));
    }

    @Test
    void squareOfPrimeNumberIsNotPrime() {
        assertFalse(CourseToolkit.isPrime(49));
    }

    @Test
    void palindromeStringReturnsTrue() {
        assertTrue(CourseToolkit.isPalindrome("level"));
    }

    @Test
    void differentCaseReturnsFalse() {
        assertFalse(CourseToolkit.isPalindrome("Level"));
    }

    @Test
    void nullTextThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CourseToolkit.isPalindrome(null)
        );
    }

    @Test
    void averageReturnsCorrectValue() {
        assertEquals(
                2.5,
                CourseToolkit.average(new int[]{2, 3})
        );
    }

    @Test
    void averageWorksWithNegativeNumbers() {
        assertEquals(
                -2.0,
                CourseToolkit.average(new int[]{-1, -3})
        );
    }

    @Test
    void emptyArrayThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> CourseToolkit.average(new int[]{})
        );
    }
}
