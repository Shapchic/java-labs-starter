package edu.course.lab03;

public class FeatureProcessor {

    private NormalizationStrategy strategy;

    //Создает процессор с указанной стратегией нормализации.
    public FeatureProcessor(NormalizationStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Стратегия нормализации не может быть null");
        }
        this.strategy = strategy;
    }

    //Обрабатывает один вектор признаков с использованием текущей стратегии.
    public double[] process(double[] values) {
        if (values == null) {
            throw new IllegalArgumentException("Входной вектор не может быть null");
        }
        return this.strategy.normalize(values);
    }

    //Обрабатывает объект наблюдения DataSample, возвращая новый объект с нормализованными признаками.
    public DataSample processSample(DataSample sample) {
        if (sample == null) {
            throw new IllegalArgumentException("Наблюдение не может быть null");
        }
        double[] normalizedFeatures = this.strategy.normalize(sample.getFeatures());
        return new DataSample(
                sample.getId(),
                sample.getLabel(),
                sample.getStatus(),
                normalizedFeatures
        );
    }

    //Заменяет текущую стратегию нормализации на новую.
    public void changeStrategy(NormalizationStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Стратегия нормализации не может быть null");
        }
        this.strategy = strategy;
    }

    //Возвращает текущую стратегию нормализации.
    public NormalizationStrategy getStrategy() {
        return this.strategy;
    }
}
