package edu.course.lab04;

import java.util.Arrays;
import java.util.Objects;

/**
 * Модель образца данных (значение для SampleRegistry).
 * Адаптирована для модуля lab04 с привязкой к составному ключу SampleId.
 */
public class DataSample {

    private final SampleId id;
    private final String label;
    private SampleStatus status;
    private double[] features;

    public DataSample(SampleId id, String label, SampleStatus status, double[] features) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор SampleId не может быть null");
        }
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Метка класса не может быть null или пустой");
        }
        if (status == null) {
            throw new IllegalArgumentException("Статус не может быть null");
        }
        if (features == null) {
            throw new IllegalArgumentException("Массив признаков не может быть null");
        }

        this.id = id;
        this.label = label.trim();
        this.status = status;
        this.features = features.clone();
    }

    public DataSample(SampleId id, String label, double[] features) {
        this(id, label, SampleStatus.READY, features);
    }

    public DataSample(SampleId id, String label) {
        this(id, label, SampleStatus.READY, new double[0]);
    }

    public SampleId getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public SampleStatus getStatus() {
        return status;
    }

    public void changeStatus(SampleStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Статус не может быть null");
        }
        this.status = newStatus;
    }

    public boolean isReady() {
        return this.status == SampleStatus.READY;
    }

    public double[] getFeatures() {
        return features.clone();
    }

    public void updateFeatures(double[] newFeatures) {
        if (newFeatures == null) {
            throw new IllegalArgumentException("Массив признаков не может быть null");
        }
        this.features = newFeatures.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataSample that = (DataSample) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(label, that.label) &&
                status == that.status &&
                Arrays.equals(features, that.features);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, label, status);
        result = 31 * result + Arrays.hashCode(features);
        return result;
    }

    @Override
    public String toString() {
        return "DataSample{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", status=" + status +
                ", features=" + Arrays.toString(features) +
                '}';
    }
}
