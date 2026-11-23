package edu.course.lab05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование контейнера результата загрузки LoadResult")
class LoadResultTest {

    @Test
    @DisplayName("Успешный результат без ошибок")
    void shouldRepresentSuccessfulResult() {
        DataSample sample = new DataSample("S-1", "cat", "cam", 10, SampleStatus.READY);
        LoadResult result = new LoadResult(List.of(sample), List.of());

        assertTrue(result.isSuccessful());
        assertFalse(result.hasErrors());
        assertEquals(1, result.getSuccessCount());
        assertEquals(0, result.getErrorCount());
        assertEquals(1, result.getSamples().size());
        assertEquals(1, result.getItems().size());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    @DisplayName("Результат, содержащий ошибки валидации")
    void shouldRepresentResultWithErrors() {
        ValidationError error = new ValidationError(2, "status", "Неизвестный статус");
        LoadResult result = new LoadResult(List.of(), List.of(error));

        assertFalse(result.isSuccessful());
        assertTrue(result.hasErrors());
        assertEquals(0, result.getSuccessCount());
        assertEquals(1, result.getErrorCount());
        assertEquals(1, result.getErrors().size());
    }

    @Test
    @DisplayName("Списки внутри LoadResult неизменяемы (защита от мутаций)")
    void shouldReturnUnmodifiableLists() {
        List<DataSample> mutableSamples = new ArrayList<>();
        mutableSamples.add(new DataSample("S-1", "cat", "cam", 10, SampleStatus.READY));

        List<ValidationError> mutableErrors = new ArrayList<>();
        mutableErrors.add(new ValidationError(2, "field", "msg"));

        LoadResult result = new LoadResult(mutableSamples, mutableErrors);

        // Мутация исходных списков не должна влиять на LoadResult
        mutableSamples.clear();
        mutableErrors.clear();

        assertEquals(1, result.getSamples().size());
        assertEquals(1, result.getErrors().size());

        // Попытка модификации списков, полученных из геттеров, должна вызывать UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> result.getSamples().add(
                new DataSample("S-2", "dog", "cam", 5, SampleStatus.RAW)
        ));
        assertThrows(UnsupportedOperationException.class, () -> result.getErrors().add(
                new ValidationError(3, "f", "m")
        ));
    }

    @Test
    @DisplayName("Корректная обработка null списков в конструкторе")
    void shouldHandleNullListsGracefully() {
        LoadResult result = new LoadResult(null, null);

        assertNotNull(result.getSamples());
        assertNotNull(result.getErrors());
        assertTrue(result.getSamples().isEmpty());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    @DisplayName("Проверка equals, hashCode и toString")
    void shouldVerifyEqualsHashCodeAndToString() {
        LoadResult r1 = new LoadResult(List.of(), List.of());
        LoadResult r2 = new LoadResult(List.of(), List.of());

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        String str = r1.toString();
        assertTrue(str.contains("samples=0"));
        assertTrue(str.contains("errors=0"));
    }
}
