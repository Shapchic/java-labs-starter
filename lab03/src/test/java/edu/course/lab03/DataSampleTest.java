package edu.course.lab03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataSampleTest {

    @Test
    @DisplayName("Успешное создание наблюдения с корректными параметрами")
    void testCreateDataSampleSuccess() {
        double[] features = {1.5, 2.5, 3.5};
        DataSample sample = new DataSample("SMP-1", "positive", SampleStatus.RAW, features);

        assertEquals("SMP-1", sample.getId());
        assertEquals("positive", sample.getLabel());
        assertEquals(SampleStatus.RAW, sample.getStatus());
        assertArrayEquals(features, sample.getFeatures(), 1e-9);
        assertFalse(sample.isReady());
        assertEquals(2.5, sample.calculateMean(), 1e-9);
        assertNotNull(sample.toString());
    }

    @Test
    @DisplayName("Успешное создание наблюдения с типизированным SampleId")
    void testCreateWithSampleIdSuccess() {
        SampleId id = new SampleId("SMP-2");
        DataSample sample = new DataSample(id, "negative", SampleStatus.READY, new double[]{10.0});

        assertEquals("SMP-2", sample.getId());
        assertEquals(id, sample.getSampleId());
        assertTrue(sample.isReady());
    }

    @Test
    @DisplayName("Защитное копирование: мутация внешнего массива признаков не меняет состояние объекта")
    void testDefensiveCopying() {
        double[] original = {1.0, 2.0, 3.0};
        DataSample sample = new DataSample("SMP-1", "cls", SampleStatus.RAW, original);

        // Мутируем массив, переданный в конструктор
        original[0] = 999.0;
        assertEquals(1.0, sample.getFeatures()[0], 1e-9);

        // Мутируем массив, полученный из getter
        double[] getterFeatures = sample.getFeatures();
        getterFeatures[1] = 888.0;
        assertEquals(2.0, sample.getFeatures()[1], 1e-9);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    @DisplayName("Создание наблюдения с пустым id выбрасывает исключение")
    void testBlankIdThrowsException(String blankId) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample(blankId, "label", SampleStatus.RAW, new double[]{1.0})
        );
    }

    @Test
    @DisplayName("Создание наблюдения с null id выбрасывает исключение")
    void testNullIdThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample((String) null, "label", SampleStatus.RAW, new double[]{1.0})
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    @DisplayName("Создание наблюдения с пустой меткой выбрасывает исключение")
    void testBlankLabelThrowsException(String blankLabel) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample("SMP-1", blankLabel, SampleStatus.RAW, new double[]{1.0})
        );
    }

    @Test
    @DisplayName("Создание наблюдения с null меткой выбрасывает исключение")
    void testNullLabelThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample("SMP-1", null, SampleStatus.RAW, new double[]{1.0})
        );
    }

    @Test
    @DisplayName("Создание наблюдения с null статусом выбрасывает исключение")
    void testNullStatusThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample("SMP-1", "label", null, new double[]{1.0})
        );
    }

    @Test
    @DisplayName("Создание наблюдения с null признаками выбрасывает исключение")
    void testNullFeaturesThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample("SMP-1", "label", SampleStatus.RAW, null)
        );
    }

    @Test
    @DisplayName("Изменение статуса наблюдения и проверка готовности (isReady)")
    void testChangeStatus() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.RAW, new double[]{1.0});
        assertFalse(sample.isReady());

        sample.changeStatus(SampleStatus.PROCESSED);
        assertEquals(SampleStatus.PROCESSED, sample.getStatus());
        assertFalse(sample.isReady());

        sample.changeStatus(SampleStatus.READY);
        assertEquals(SampleStatus.READY, sample.getStatus());
        assertTrue(sample.isReady());

        assertThrows(IllegalArgumentException.class, () -> sample.changeStatus(null));
    }

    @Test
    @DisplayName("Расчет среднего для пустого массива признаков возвращает 0.0")
    void testCalculateMeanEmpty() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.RAW, new double[0]);
        assertEquals(0.0, sample.calculateMean(), 1e-9);
    }

    @Test
    @DisplayName("SampleId как Value Object")
    void testSampleIdValueObject() {
        SampleId id1 = new SampleId("SMP-100");
        SampleId id2 = new SampleId("SMP-100");
        SampleId id3 = new SampleId("SMP-200");

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
        assertEquals("SMP-100", id1.value());
        assertEquals("SMP-100", id1.toString());

        assertThrows(IllegalArgumentException.class, () -> new SampleId(null));
        assertThrows(IllegalArgumentException.class, () -> new SampleId(""));
        assertThrows(IllegalArgumentException.class, () -> new SampleId("   "));
    }
}
