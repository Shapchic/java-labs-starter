package edu.course.lab03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NormalizationStrategyTest {

    @Test
    @DisplayName("MinMaxNormalization масштабирует вектор в [0.0, 1.0]")
    void testMinMaxNormalizationStandard() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        double[] input = {10.0, 20.0, 30.0, 40.0, 50.0};
        double[] result = strategy.normalize(input);

        double[] expected = {0.0, 0.25, 0.5, 0.75, 1.0};
        assertArrayEquals(expected, result, 1e-9);
        assertNotSame(input, result);
        // Исходный массив не изменился
        assertEquals(10.0, input[0], 1e-9);
    }

    @Test
    @DisplayName("MinMaxNormalization возвращает нули, если все элементы одинаковы (min == max)")
    void testMinMaxNormalizationConstantArray() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        double[] input = {7.0, 7.0, 7.0};
        double[] result = strategy.normalize(input);

        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result, 1e-9);
    }

    @Test
    @DisplayName("MinMaxNormalization для пустого массива")
    void testMinMaxNormalizationEmpty() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        double[] result = strategy.normalize(new double[]{});
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("MinMaxNormalization выбрасывает исключение при null")
    void testMinMaxNormalizationNullThrowsException() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        assertThrows(IllegalArgumentException.class, () -> strategy.normalize(null));
    }

    @Test
    @DisplayName("MeanCenteringNormalization центрирует вектор так, что среднее равно 0")
    void testMeanCenteringStandard() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        double[] input = {10.0, 20.0, 30.0};
        double[] result = strategy.normalize(input);

        // mean = 20.0 -> [10-20, 20-20, 30-20] = [-10, 0, 10]
        assertArrayEquals(new double[]{-10.0, 0.0, 10.0}, result, 1e-9);

        // Проверяем, что среднее результирующего вектора равно 0
        double sum = 0.0;
        for (double v : result) {
            sum += v;
        }
        assertEquals(0.0, sum / result.length, 1e-9);
    }

    @Test
    @DisplayName("MeanCenteringNormalization для вектора с одинаковыми значениями дает нули")
    void testMeanCenteringConstantArray() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        double[] input = {5.0, 5.0, 5.0};
        double[] result = strategy.normalize(input);

        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result, 1e-9);
    }

    @Test
    @DisplayName("MeanCenteringNormalization для пустого массива")
    void testMeanCenteringEmpty() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        double[] result = strategy.normalize(new double[]{});
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("MeanCenteringNormalization выбрасывает исключение при null")
    void testMeanCenteringNullThrowsException() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        assertThrows(IllegalArgumentException.class, () -> strategy.normalize(null));
    }

    @Test
    @DisplayName("ZScoreNormalization стандартизирует вектор (mean = 0, std = 1)")
    void testZScoreNormalizationStandard() {
        NormalizationStrategy strategy = new ZScoreNormalization();
        double[] input = {2.0, 4.0, 4.0, 4.0, 5.0, 5.0, 7.0, 9.0};
        double[] result = strategy.normalize(input);

        // Проверяем, что среднее ≈ 0 и дисперсия ≈ 1
        double sum = 0.0;
        for (double v : result) {
            sum += v;
        }
        double mean = sum / result.length;
        assertEquals(0.0, mean, 1e-9);

        double sumSq = 0.0;
        for (double v : result) {
            sumSq += (v - mean) * (v - mean);
        }
        double variance = sumSq / result.length;
        assertEquals(1.0, variance, 1e-9);
    }

    @Test
    @DisplayName("ZScoreNormalization для константного вектора возвращает нули (std = 0)")
    void testZScoreNormalizationConstantArray() {
        NormalizationStrategy strategy = new ZScoreNormalization();
        double[] input = {4.0, 4.0, 4.0};
        double[] result = strategy.normalize(input);

        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result, 1e-9);
    }

    @Test
    @DisplayName("ZScoreNormalization для пустого массива")
    void testZScoreNormalizationEmpty() {
        NormalizationStrategy strategy = new ZScoreNormalization();
        double[] result = strategy.normalize(new double[]{});
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("ZScoreNormalization выбрасывает исключение при null")
    void testZScoreNormalizationNullThrowsException() {
        NormalizationStrategy strategy = new ZScoreNormalization();
        assertThrows(IllegalArgumentException.class, () -> strategy.normalize(null));
    }
}
