package edu.course.lab04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты доменной модели DataSample")
class DataSampleTest {

    @Test
    @DisplayName("Корректное создание объекта и проверка свойств")
    void shouldCreateDataSampleWithValidArguments() {
        SampleId id = new SampleId("mnist", 1);
        double[] features = {0.1, 0.5, 0.9};

        DataSample sample = new DataSample(id, "digit-7", SampleStatus.READY, features);

        assertEquals(id, sample.getId());
        assertEquals("digit-7", sample.getLabel());
        assertEquals(SampleStatus.READY, sample.getStatus());
        assertArrayEquals(features, sample.getFeatures());
        assertTrue(sample.isReady());
    }

    @Test
    @DisplayName("Защитное копирование массива признаков при создании и получении")
    void shouldEnsureDefensiveCopyingOfFeatures() {
        SampleId id = new SampleId("mnist", 2);
        double[] originalFeatures = {1.0, 2.0, 3.0};

        DataSample sample = new DataSample(id, "digit-1", originalFeatures);

        // Мутация исходного массива снаружи не влияет на DataSample
        originalFeatures[0] = 999.0;
        assertEquals(1.0, sample.getFeatures()[0]);

        // Мутация возвращенного массива снаружи не влияет на DataSample
        double[] retrieved = sample.getFeatures();
        retrieved[1] = 888.0;
        assertEquals(2.0, sample.getFeatures()[1]);
    }

    @Test
    @DisplayName("Смена статуса и проверка состояния")
    void shouldChangeStatusCorrectly() {
        SampleId id = new SampleId("iris", 1);
        DataSample sample = new DataSample(id, "setosa", SampleStatus.RAW, new double[]{5.1, 3.5});

        assertFalse(sample.isReady());
        sample.changeStatus(SampleStatus.READY);
        assertTrue(sample.isReady());
        assertEquals(SampleStatus.READY, sample.getStatus());

        assertThrows(IllegalArgumentException.class, () -> sample.changeStatus(null));
    }

    @Test
    @DisplayName("Валидация аргументов конструктора DataSample")
    void shouldValidateConstructorArguments() {
        SampleId id = new SampleId("mnist", 3);
        double[] features = {0.0};

        assertThrows(IllegalArgumentException.class, () -> new DataSample(null, "label", SampleStatus.READY, features));
        assertThrows(IllegalArgumentException.class, () -> new DataSample(id, null, SampleStatus.READY, features));
        assertThrows(IllegalArgumentException.class, () -> new DataSample(id, "   ", SampleStatus.READY, features));
        assertThrows(IllegalArgumentException.class, () -> new DataSample(id, "label", null, features));
        assertThrows(IllegalArgumentException.class, () -> new DataSample(id, "label", SampleStatus.READY, null));
    }

    @Test
    @DisplayName("Контракты equals и hashCode для DataSample")
    void shouldVerifyEqualsAndHashCode() {
        SampleId id1 = new SampleId("cifar", 10);
        SampleId id2 = new SampleId("cifar", 10);
        SampleId id3 = new SampleId("cifar", 11);

        DataSample s1 = new DataSample(id1, "cat", SampleStatus.READY, new double[]{0.5});
        DataSample s2 = new DataSample(id2, "cat", SampleStatus.READY, new double[]{0.5});
        DataSample s3 = new DataSample(id3, "dog", SampleStatus.READY, new double[]{0.5});

        assertEquals(s1, s1);
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        assertNotEquals(s1, s3);
        assertNotEquals(null, s1);
        assertNotEquals("string", s1);

        assertTrue(s1.toString().contains("cifar"));
        assertTrue(s1.toString().contains("cat"));
    }
}
