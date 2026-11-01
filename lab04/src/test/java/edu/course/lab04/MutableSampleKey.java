package edu.course.lab04;

import java.util.Objects;

//Изменяемый учебный ключ для демонстрации проблемы мутации ключа в HashMap (эксперимент 5).
public class MutableSampleKey {

    private String dataset;
    private int sampleNumber;

    public MutableSampleKey(String dataset, int sampleNumber) {
        this.dataset = dataset;
        this.sampleNumber = sampleNumber;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public int getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(int sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MutableSampleKey that = (MutableSampleKey) o;
        return sampleNumber == that.sampleNumber && Objects.equals(dataset, that.dataset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataset, sampleNumber);
    }

    @Override
    public String toString() {
        return "MutableSampleKey{" +
                "dataset='" + dataset + '\'' +
                ", sampleNumber=" + sampleNumber +
                '}';
    }
}
