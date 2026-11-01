package edu.course.lab02;

import java.util.Arrays;
import java.util.Objects;

/**
 * Образец данных (доменная модель для направления «Инженерия систем искусственного интеллекта»).
 * Обеспечивает инкапсуляцию состояния и защиту бизнес-инвариантов.
 */
public class DataSample {

    private final String id;
    private final String label;
    private SampleStatus status;
    private double[] features;

    //Создает наблюдение со строковым идентификатором.
    public DataSample(String id, String label, SampleStatus status, double[] features) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Идентификатор наблюдения не может быть null или пустым");
        }
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Метка наблюдения не может быть null или пустой");
        }
        if (status == null) {
            throw new IllegalArgumentException("Статус наблюдения не может быть null");
        }
        if (features == null) {
            throw new IllegalArgumentException("Массив признаков не может быть null");
        }

        this.id = id;
        this.label = label;
        this.status = status;
        this.features = features.clone();
    }

    //Создает наблюдение с типизированным идентификатором SampleId.
    public DataSample(SampleId id, String label, SampleStatus status, double[] features) {
        this(
                Objects.requireNonNull(id, "Идентификатор наблюдения не может быть null").value(),
                label,
                status,
                features
        );
    }

    //Изменяет текущий статус наблюдения.
    public void changeStatus(SampleStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Новый статус не может быть null");
        }
        this.status = newStatus;
    }

    //Проверяет, готово ли наблюдение к использованию.
    public boolean isReady() {
        return this.status == SampleStatus.READY;
    }

    //Вычисляет среднее значение числовых признаков.
    public double calculateMean() {
        if (this.features.length == 0) {
            return 0.0;
        }
        double sum = 0.0;
        for (double v : this.features) {
            sum += v;
        }
        return sum / this.features.length;
    }

    //Выполняет нормализацию признаков без изменения исходного объекта.
    public DataSample normalize() {
        if (this.features.length == 0) {
            return new DataSample(this.id, this.label, this.status, new double[0]);
        }

        double min = this.features[0];
        double max = this.features[0];
        for (double v : this.features) {
            if (v < min) {
                min = v;
            }
            if (v > max) {
                max = v;
            }
        }

        double[] normalized = new double[this.features.length];
        double range = max - min;
        if (range != 0.0) {
            for (int i = 0; i < this.features.length; i++) {
                normalized[i] = (this.features[i] - min) / range;
            }
        }

        return new DataSample(this.id, this.label, this.status, normalized);
    }

    //Возвращает строковый идентификатор.
    public String getId() {
        return this.id;
    }

    //Возвращает идентификатор в виде SampleId.
    public SampleId getSampleId() {
        return new SampleId(this.id);
    }

    //Возвращает метку класса.
    public String getLabel() {
        return this.label;
    }

    //Возвращает текущий статус наблюдения.
    public SampleStatus getStatus() {
        return this.status;
    }

    //Возвращает защитную копию массива признаков.
    public double[] getFeatures() {
        return this.features.clone();
    }

    @Override
    public String toString() {
        return "DataSample{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", status=" + status +
                ", features=" + Arrays.toString(features) +
                '}';
    }
}
