package edu.course.lab04;

import java.util.Objects;

public final class SampleId {

    private final String dataset;
    private final int sampleNumber;

    //Создает составной идентификатор образца данных.
    public SampleId(String dataset, int sampleNumber) {
        if (dataset == null || dataset.isBlank()) {
            throw new IllegalArgumentException("Имя набора данных не может быть null или пустым");
        }
        if (sampleNumber < 0) {
            throw new IllegalArgumentException("Номер наблюдения не может быть отрицательным: " + sampleNumber);
        }
        this.dataset = dataset.trim();
        this.sampleNumber = sampleNumber;
    }

    //Возвращает имя набора данных.
    public String getDataset() {
        return dataset;
    }

    //Возвращает номер наблюдения.
    public int getSampleNumber() {
        return sampleNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SampleId sampleId = (SampleId) o;
        return sampleNumber == sampleId.sampleNumber && Objects.equals(dataset, sampleId.dataset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataset, sampleNumber);
    }

    @Override
    public String toString() {
        return "SampleId{" +
                "dataset='" + dataset + '\'' +
                ", sampleNumber=" + sampleNumber +
                '}';
    }
}
