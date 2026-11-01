package edu.course.lab04;

import java.util.Objects;

/**
 * Неизменяемый идентификатор образца данных (ключ для SampleRegistry).
 * Состоит из имени набора данных (dataset) и номера наблюдения (sampleNumber).
 */
public final class SampleId {

    private final String dataset;
    private final int sampleNumber;

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

    public String getDataset() {
        return dataset;
    }

    public int getSampleNumber() {
        return sampleNumber;
    }

    // Рекорд-стиль аксессоры для удобства
    public String dataset() {
        return dataset;
    }

    public int sampleNumber() {
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
