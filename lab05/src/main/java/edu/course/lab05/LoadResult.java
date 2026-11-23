package edu.course.lab05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class LoadResult {

    private final List<DataSample> samples;
    private final List<ValidationError> errors;

    //Создает результат загрузки со списком записей и списком ошибок.
    public LoadResult(List<DataSample> samples, List<ValidationError> errors) {
        this.samples = samples != null ? Collections.unmodifiableList(new ArrayList<>(samples)) : List.of();
        this.errors = errors != null ? Collections.unmodifiableList(new ArrayList<>(errors)) : List.of();
    }

    //Возвращает список успешно загруженных образцов.
    public List<DataSample> getSamples() {
        return samples;
    }

    //Возвращает список элементов.
    public List<DataSample> getItems() {
        return samples;
    }

    //Возвращает список ошибок валидации.
    public List<ValidationError> getErrors() {
        return errors;
    }

    //Проверяет наличие ошибок валидации.
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    //Проверяет, завершилась ли загрузка без ошибок.
    public boolean isSuccessful() {
        return errors.isEmpty();
    }

    //Возвращает количество успешно загруженных записей.
    public int getSuccessCount() {
        return samples.size();
    }

    //Возвращает количество ошибок.
    public int getErrorCount() {
        return errors.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadResult that = (LoadResult) o;
        return Objects.equals(samples, that.samples) && Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(samples, errors);
    }

    @Override
    public String toString() {
        return "LoadResult{" +
                "samples=" + samples.size() +
                ", errors=" + errors.size() +
                '}';
    }
}
