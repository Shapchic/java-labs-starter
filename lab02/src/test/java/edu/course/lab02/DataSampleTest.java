package edu.course.lab02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
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
    @DisplayName("Создание с null SampleId выбрасывает исключение")
    void testCreateWithNullSampleIdThrowsException() {
        assertThrows(
                NullPointerException.class,
                () -> new DataSample((SampleId) null, "negative", SampleStatus.READY, new double[]{1.0})
        );
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
    void testCreateWithBlankIdThrowsException(String blankId) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample(blankId, "label", SampleStatus.RAW, new double[]{1.0})
        );
    }

    @Test
    @DisplayName("Создание наблюдения с null id выбрасывает исключение")
    void testCreateWithNullIdThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample((String) null, "label", SampleStatus.RAW, new double[]{1.0})
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    @DisplayName("Создание наблюдения с пустой меткой выбрасывает исключение")
    void testCreateWithBlankLabelThrowsException(String blankLabel) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample("SMP-1", blankLabel, SampleStatus.RAW, new double[]{1.0})
        );
    }

    @Test
    @DisplayName("Создание наблюдения с null меткой выбрасывает исключение")
    void testCreateWithNullLabelThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample("SMP-1", null, SampleStatus.RAW, new double[]{1.0})
        );
    }

    @Test
    @DisplayName("Создание наблюдения с null статусом выбрасывает исключение")
    void testCreateWithNullStatusThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample("SMP-1", "label", null, new double[]{1.0})
        );
    }

    @Test
    @DisplayName("Создание наблюдения с null массивом признаков выбрасывает исключение")
    void testCreateWithNullFeaturesThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DataSample("SMP-1", "label", SampleStatus.RAW, null)
        );
    }

    @Test
    @DisplayName("Изменение статуса наблюдения")
    void testChangeStatusSuccess() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.RAW, new double[]{1.0});
        assertEquals(SampleStatus.RAW, sample.getStatus());

        sample.changeStatus(SampleStatus.PROCESSED);
        assertEquals(SampleStatus.PROCESSED, sample.getStatus());

        sample.changeStatus(SampleStatus.READY);
        assertEquals(SampleStatus.READY, sample.getStatus());
        assertTrue(sample.isReady());
    }

    @Test
    @DisplayName("Передача null в changeStatus выбрасывает исключение")
    void testChangeStatusNullThrowsException() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.RAW, new double[]{1.0});
        assertThrows(IllegalArgumentException.class, () -> sample.changeStatus(null));
    }

    @Test
    @DisplayName("isReady возвращает true только для READY")
    void testIsReady() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.RAW, new double[]{1.0});
        assertFalse(sample.isReady());

        sample.changeStatus(SampleStatus.PROCESSED);
        assertFalse(sample.isReady());

        sample.changeStatus(SampleStatus.REJECTED);
        assertFalse(sample.isReady());

        sample.changeStatus(SampleStatus.READY);
        assertTrue(sample.isReady());
    }

    @Test
    @DisplayName("Вычисление среднего значения признаков")
    void testCalculateMean() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.RAW, new double[]{2.0, 4.0, 6.0, 8.0});
        assertEquals(5.0, sample.calculateMean(), 1e-9);
    }

    @Test
    @DisplayName("Вычисление среднего для пустого массива возвращает 0.0")
    void testCalculateMeanEmptyArray() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.RAW, new double[]{});
        assertEquals(0.0, sample.calculateMean(), 1e-9);
    }

    @Test
    @DisplayName("Дополнительная часть: нормализация признаков (Min-Max) создает новый объект")
    void testNormalizeSuccess() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.READY, new double[]{10.0, 20.0, 30.0});
        DataSample normalized = sample.normalize();

        assertNotSame(sample, normalized);
        assertEquals(sample.getId(), normalized.getId());
        assertEquals(sample.getLabel(), normalized.getLabel());
        assertEquals(sample.getStatus(), normalized.getStatus());

        // Исходный объект остался нетронутым
        assertArrayEquals(new double[]{10.0, 20.0, 30.0}, sample.getFeatures(), 1e-9);

        // В новом объекте признаки смасштабированы в [0.0, 1.0]
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, normalized.getFeatures(), 1e-9);
    }

    @Test
    @DisplayName("Нормализация при одинаковых признаках возвращает нули")
    void testNormalizeConstantFeatures() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.RAW, new double[]{5.0, 5.0, 5.0});
        DataSample normalized = sample.normalize();

        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, normalized.getFeatures(), 1e-9);
    }

    @Test
    @DisplayName("Нормализация пустого массива признаков")
    void testNormalizeEmptyFeatures() {
        DataSample sample = new DataSample("SMP-1", "label", SampleStatus.RAW, new double[]{});
        DataSample normalized = sample.normalize();

        assertEquals(0, normalized.getFeatures().length);
    }

    @Test
    @DisplayName("SampleId: равенство двух объектов определяется значением")
    void testSampleIdEquality() {
        SampleId id1 = new SampleId("SMP-100");
        SampleId id2 = new SampleId("SMP-100");
        SampleId id3 = new SampleId("SMP-200");

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
        assertEquals("SMP-100", id1.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t"})
    @DisplayName("SampleId с пустым значением выбрасывает исключение")
    void testSampleIdBlankThrowsException(String blank) {
        assertThrows(IllegalArgumentException.class, () -> new SampleId(blank));
    }

    @Test
    @DisplayName("SampleId с null выбрасывает исключение")
    void testSampleIdNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new SampleId(null));
    }
}
