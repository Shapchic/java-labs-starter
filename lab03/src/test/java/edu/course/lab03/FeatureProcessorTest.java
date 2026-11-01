package edu.course.lab03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FeatureProcessorTest {

    @Test
    @DisplayName("FeatureProcessor с MinMaxNormalization масштабирует вектор")
    void testProcessWithMinMaxNormalization() {
        NormalizationStrategy strategy = new MinMaxNormalization();
        FeatureProcessor processor = new FeatureProcessor(strategy);

        double[] input = {0.0, 50.0, 100.0};
        double[] result = processor.process(input);

        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, result, 1e-9);
        assertSame(strategy, processor.getStrategy());
    }

    @Test
    @DisplayName("FeatureProcessor с MeanCenteringNormalization центрирует вектор")
    void testProcessWithMeanCenteringNormalization() {
        NormalizationStrategy strategy = new MeanCenteringNormalization();
        FeatureProcessor processor = new FeatureProcessor(strategy);

        double[] input = {10.0, 20.0, 30.0};
        double[] result = processor.process(input);

        assertArrayEquals(new double[]{-10.0, 0.0, 10.0}, result, 1e-9);
    }

    @Test
    @DisplayName("FeatureProcessor с ZScoreNormalization стандартизирует вектор")
    void testProcessWithZScoreNormalization() {
        NormalizationStrategy strategy = new ZScoreNormalization();
        FeatureProcessor processor = new FeatureProcessor(strategy);

        double[] input = {10.0, 20.0, 30.0};
        double[] result = processor.process(input);

        double std = Math.sqrt(200.0 / 3.0);
        double[] expected = {-10.0 / std, 0.0, 10.0 / std};

        assertArrayEquals(expected, result, 1e-9);
    }

    @Test
    @DisplayName("Тест взаимозаменяемости стратегий: один процессор применяет разные алгоритмы без переписывания")
    void testStrategySubstitutionDemonstration() {
        double[] features = {0.0, 10.0, 20.0};

        FeatureProcessor processor = new FeatureProcessor(new MinMaxNormalization());
        double[] minMaxResult = processor.process(features);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, minMaxResult, 1e-9);

        // Замена стратегии на центрирование по среднему (среднее = 10.0)
        processor.changeStrategy(new MeanCenteringNormalization());
        double[] centeredResult = processor.process(features);
        assertArrayEquals(new double[]{-10.0, 0.0, 10.0}, centeredResult, 1e-9);

        // Замена стратегии на Z-score
        processor.changeStrategy(new ZScoreNormalization());
        double[] zScoreResult = processor.process(features);
        double std = Math.sqrt((100.0 + 0.0 + 100.0) / 3.0);
        assertArrayEquals(new double[]{-10.0 / std, 0.0, 10.0 / std}, zScoreResult, 1e-9);
    }

    @Test
    @DisplayName("Входной вектор не изменяется при вызове process (immutability)")
    void testInputArrayIsNotModified() {
        FeatureProcessor processor = new FeatureProcessor(new MeanCenteringNormalization());
        double[] input = {100.0, 200.0};

        processor.process(input);
        assertArrayEquals(new double[]{100.0, 200.0}, input, 1e-9);
    }

    @Test
    @DisplayName("Выброс исключений при невалидных аргументах в FeatureProcessor")
    void testValidationExceptions() {
        assertThrows(IllegalArgumentException.class, () -> new FeatureProcessor(null));

        FeatureProcessor processor = new FeatureProcessor(new MinMaxNormalization());
        assertThrows(IllegalArgumentException.class, () -> processor.process(null));
        assertThrows(IllegalArgumentException.class, () -> processor.changeStrategy(null));
    }
}
