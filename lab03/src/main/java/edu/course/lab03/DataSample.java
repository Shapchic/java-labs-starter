package edu.course.lab03;

import java.util.Arrays;
import java.util.Objects;

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

    //Создает наблюдение с типизированным идентификатором.
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

    //Проверяет, готово ли наблюдение к обучению/инференсу.
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

    //Возвращает строковый идентификатор.
    public String getId() {
        return this.id;
    }

    //Возвращает типизированный идентификатор.
    public SampleId getSampleId() {
        return new SampleId(this.id);
    }

    //Возвращает метку класса.
    public String getLabel() {
        return this.label;
    }

    //Возвращает текущий статус.
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
