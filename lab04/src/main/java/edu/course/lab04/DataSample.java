package edu.course.lab04;

import java.util.Arrays;
import java.util.Objects;

public class DataSample {

    private final SampleId id;
    private final String label;
    private SampleStatus status;
    private double[] features;

    //Создает наблюдение с заданными параметрами.
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

    //Создает наблюдение со статусом READY по умолчанию.
    public DataSample(SampleId id, String label, double[] features) {
        this(id, label, SampleStatus.READY, features);
    }

    //Создает наблюдение с пустым массивом признаков.
    public DataSample(SampleId id, String label) {
        this(id, label, SampleStatus.READY, new double[0]);
    }

    //Возвращает идентификатор наблюдения.
    public SampleId getId() {
        return id;
    }

    //Возвращает метку класса.
    public String getLabel() {
        return label;
    }

    //Возвращает текущий статус.
    public SampleStatus getStatus() {
        return status;
    }

    //Изменяет статус наблюдения.
    public void changeStatus(SampleStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Статус не может быть null");
        }
        this.status = newStatus;
    }

    //Проверяет, готово ли наблюдение.
    public boolean isReady() {
        return this.status == SampleStatus.READY;
    }

    //Возвращает защитную копию массива признаков.
    public double[] getFeatures() {
        return features.clone();
    }

    //Обновляет массив признаков с защитным копированием.
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
