package edu.course.lab05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование доменной модели DataSample")
class DataSampleTest {

    @Test
    @DisplayName("Успешное создание корректного экземпляра DataSample")
    void shouldCreateValidDataSample() {
        DataSample sample = new DataSample("S-101", "cat", "camera-1", 128, SampleStatus.READY);

        assertEquals("S-101", sample.getId());
        assertEquals("cat", sample.getLabel());
        assertEquals("camera-1", sample.getSource());
        assertEquals(128, sample.getFeatureCount());
        assertEquals(SampleStatus.READY, sample.getStatus());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Исключение при некорректном идентификаторе (null или пробелы)")
    void shouldThrowExceptionForInvalidId(String invalidId) {
        assertThrows(IllegalArgumentException.class, () ->
                new DataSample(invalidId, "cat", "camera-1", 128, SampleStatus.READY));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Исключение при некорректной метке класса (null или пробелы)")
    void shouldThrowExceptionForInvalidLabel(String invalidLabel) {
        assertThrows(IllegalArgumentException.class, () ->
                new DataSample("S-101", invalidLabel, "camera-1", 128, SampleStatus.READY));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Исключение при некорректном источнике данных (null или пробелы)")
    void shouldThrowExceptionForInvalidSource(String invalidSource) {
        assertThrows(IllegalArgumentException.class, () ->
                new DataSample("S-101", "cat", invalidSource, 128, SampleStatus.READY));
    }

    @Test
    @DisplayName("Исключение при отрицательном количестве признаков")
    void shouldThrowExceptionForNegativeFeatureCount() {
        assertThrows(IllegalArgumentException.class, () ->
                new DataSample("S-101", "cat", "camera-1", -1, SampleStatus.READY));
    }

    @Test
    @DisplayName("Исключение при null статусе")
    void shouldThrowExceptionForNullStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                new DataSample("S-101", "cat", "camera-1", 128, null));
    }

    @Test
    @DisplayName("Проверка контракта equals и hashCode")
    void shouldVerifyEqualsAndHashCode() {
        DataSample sample1 = new DataSample("S-101", "cat", "camera-1", 128, SampleStatus.READY);
        DataSample sample2 = new DataSample("S-101", "cat", "camera-1", 128, SampleStatus.READY);
        DataSample sample3 = new DataSample("S-102", "dog", "camera-2", 64, SampleStatus.RAW);

        assertEquals(sample1, sample2);
        assertEquals(sample1.hashCode(), sample2.hashCode());
        assertNotEquals(sample1, sample3);
        assertNotEquals(sample1, null);
        assertNotEquals(sample1, "some-string");
    }

    @Test
    @DisplayName("Проверка toString")
    void shouldFormatToStringProperly() {
        DataSample sample = new DataSample("S-101", "cat", "camera-1", 128, SampleStatus.READY);
        String str = sample.toString();

        assertTrue(str.contains("S-101"));
        assertTrue(str.contains("cat"));
        assertTrue(str.contains("camera-1"));
        assertTrue(str.contains("128"));
        assertTrue(str.contains("READY"));
    }
}
