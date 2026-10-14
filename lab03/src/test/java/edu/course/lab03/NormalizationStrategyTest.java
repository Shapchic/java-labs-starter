package edu.course.lab03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NormalizationStrategyTest {

    // --- Тесты MinMaxNormalization ---

    @Test
    @DisplayName("MinMaxNormalization: корректно масштабирует значения в диапазон [0.0, 1.0]")
    void testMinMaxNormalizationStandard() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        double[] input = {10.0, 20.0, 30.0};
        double[] expected = {0.0, 0.5, 1.0};

        double[] actual = strategy.normalize(input);
        assertArrayEquals(expected, actual, 1e-9);
    }

    @Test
    @DisplayName("MinMaxNormalization: вектор с одинаковыми элементами нормализуется в нули")
    void testMinMaxNormalizationConstantArray() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        double[] input = {5.0, 5.0, 5.0};
        double[] expected = {0.0, 0.0, 0.0};

        assertArrayEquals(expected, strategy.normalize(input), 1e-9);
    }

    @Test
    @DisplayName("MinMaxNormalization: пустой вектор возвращает пустой массив")
    void testMinMaxNormalizationEmpty() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        double[] actual = strategy.normalize(new double[0]);
        assertEquals(0, actual.length);
    }

    @Test
    @DisplayName("MinMaxNormalization: null аргумент вызывает исключение")
    void testMinMaxNormalizationNullThrowsException() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        assertThrows(IllegalArgumentException.class, () -> strategy.normalize(null));
    }

    @Test
    @DisplayName("MinMaxNormalization: исходный массив не изменяется (immutability)")
    void testMinMaxNormalizationPreservesInput() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        double[] input = {10.0, 20.0, 30.0};
        strategy.normalize(input);

        assertArrayEquals(new double[]{10.0, 20.0, 30.0}, input, 1e-9);
    }

    // --- Тесты MeanCenteringNormalization ---

    @Test
    @DisplayName("MeanCenteringNormalization: вычитает среднее арифметическое (сумма результата = 0)")
    void testMeanCenteringNormalizationStandard() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        double[] input = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] expected = {-2.0, -1.0, 0.0, 1.0, 2.0};

        double[] actual = strategy.normalize(input);
        assertArrayEquals(expected, actual, 1e-9);

        // Проверяем, что сумма центрированных значений равна нулю
        double sum = 0.0;
        for (double v : actual) {
            sum += v;
        }
        assertEquals(0.0, sum, 1e-9);
    }

    @Test
    @DisplayName("MeanCenteringNormalization: одинаковые элементы центрируются в нули")
    void testMeanCenteringNormalizationConstantArray() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        double[] input = {7.0, 7.0, 7.0};
        double[] expected = {0.0, 0.0, 0.0};

        assertArrayEquals(expected, strategy.normalize(input), 1e-9);
    }

    @Test
    @DisplayName("MeanCenteringNormalization: пустой вектор возвращает пустой массив")
    void testMeanCenteringNormalizationEmpty() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        double[] actual = strategy.normalize(new double[0]);
        assertEquals(0, actual.length);
    }

    @Test
    @DisplayName("MeanCenteringNormalization: null аргумент вызывает исключение")
    void testMeanCenteringNormalizationNullThrowsException() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        assertThrows(IllegalArgumentException.class, () -> strategy.normalize(null));
    }

    @Test
    @DisplayName("MeanCenteringNormalization: исходный массив не изменяется (immutability)")
    void testMeanCenteringNormalizationPreservesInput() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        double[] input = {2.0, 4.0, 6.0};
        strategy.normalize(input);

        assertArrayEquals(new double[]{2.0, 4.0, 6.0}, input, 1e-9);
    }

    // --- Тесты ZScoreNormalization (Дополнительная часть) ---

    @Test
    @DisplayName("ZScoreNormalization: стандартизирует вектор (среднее = 0, дисперсия = 1)")
    void testZScoreNormalizationStandard() {
        NormalizationStrategy strategy = new ZScoreNormalization();
        double[] input = {2.0, 4.0, 4.0, 4.0, 5.0, 5.0, 7.0, 9.0};

        double[] actual = strategy.normalize(input);

        // Проверяем среднее (должно быть 0)
        double sum = 0.0;
        for (double v : actual) {
            sum += v;
        }
        assertEquals(0.0, sum / actual.length, 1e-9);

        // Проверяем дисперсию (должна быть 1)
        double sumSq = 0.0;
        for (double v : actual) {
            sumSq += v * v;
        }
        assertEquals(1.0, sumSq / actual.length, 1e-9);
    }

    @Test
    @DisplayName("ZScoreNormalization: вектор с нулевым отклонением возвращает нули")
    void testZScoreNormalizationConstantArray() {
        NormalizationStrategy strategy = new ZScoreNormalization();
        double[] input = {3.0, 3.0, 3.0};
        double[] expected = {0.0, 0.0, 0.0};

        assertArrayEquals(expected, strategy.normalize(input), 1e-9);
    }

    @Test
    @DisplayName("ZScoreNormalization: пустой вектор возвращает пустой массив")
    void testZScoreNormalizationEmpty() {
        NormalizationStrategy strategy = new ZScoreNormalization();
        double[] actual = strategy.normalize(new double[0]);
        assertEquals(0, actual.length);
    }

    @Test
    @DisplayName("ZScoreNormalization: null аргумент вызывает исключение")
    void testZScoreNormalizationNullThrowsException() {
        NormalizationStrategy strategy = new ZScoreNormalization();
        assertThrows(IllegalArgumentException.class, () -> strategy.normalize(null));
    }
}
