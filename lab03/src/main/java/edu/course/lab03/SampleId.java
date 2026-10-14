package edu.course.lab03;

public record SampleId(String value) {

    public SampleId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Идентификатор наблюдения не может быть null или пустым");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
