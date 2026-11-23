package edu.course.lab05;

import java.util.Objects;

public final class DataSample {

    private final String id;
    private final String label;
    private final String source;
    private final int featureCount;
    private final SampleStatus status;

    //Создает наблюдение с заданными параметрами.
    public DataSample(String id, String label, String source, int featureCount, SampleStatus status) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Идентификатор образца (id) не может быть пустым");
        }
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Метка класса (label) не может быть пустой");
        }
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("Источник данных (source) не может быть пустым");
        }
        if (featureCount < 0) {
            throw new IllegalArgumentException("Количество признаков (featureCount) не может быть отрицательным: " + featureCount);
        }
        if (status == null) {
            throw new IllegalArgumentException("Статус образца (status) не может быть null");
        }

        this.id = id.trim();
        this.label = label.trim();
        this.source = source.trim();
        this.featureCount = featureCount;
        this.status = status;
    }

    //Возвращает идентификатор наблюдения.
    public String getId() {
        return id;
    }

    //Возвращает метку класса.
    public String getLabel() {
        return label;
    }

    //Возвращает источник данных.
    public String getSource() {
        return source;
    }

    //Возвращает количество признаков.
    public int getFeatureCount() {
        return featureCount;
    }

    //Возвращает статус наблюдения.
    public SampleStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSample that = (DataSample) o;
        return featureCount == that.featureCount &&
                Objects.equals(id, that.id) &&
                Objects.equals(label, that.label) &&
                Objects.equals(source, that.source) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, source, featureCount, status);
    }

    @Override
    public String toString() {
        return "DataSample{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", source='" + source + '\'' +
                ", featureCount=" + featureCount +
                ", status=" + status +
                '}';
    }
}
