package edu.course.lab03;

public class MinMaxNormalization implements NormalizationStrategy {

    @Override
    public double[] normalize(double[] values) {
        if (values == null) {
            throw new IllegalArgumentException("Входной вектор не может быть null");
        }
        if (values.length == 0) {
            return new double[0];
        }

        double min = values[0];
        double max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
            }
            if (values[i] > max) {
                max = values[i];
            }
        }

        double[] result = new double[values.length];
        double range = max - min;
        if (range == 0.0) {
            // Все значения одинаковы — шкалирование дает нули
            return result;
        }

        for (int i = 0; i < values.length; i++) {
            result[i] = (values[i] - min) / range;
        }

        return result;
    }
}
