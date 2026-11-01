package edu.course.lab04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты составного ключа SampleId")
class SampleIdTest {

    @Test
    @DisplayName("Два разных экземпляра с одинаковыми dataset и sampleNumber логически равны и имеют одинаковый hashCode")
    void shouldBeEqualWhenDatasetAndSampleNumberMatch() {
        SampleId id1 = new SampleId("mnist", 42);
        SampleId id2 = new SampleId("mnist", 42);

        assertEquals(id1, id2);
        assertEquals(id2, id1);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    @DisplayName("Экземпляры с разными dataset не равны")
    void shouldNotBeEqualWhenDatasetDiffers() {
        SampleId id1 = new SampleId("mnist", 42);
        SampleId id2 = new SampleId("cifar10", 42);

        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("Экземпляры с разными sampleNumber не равны")
    void shouldNotBeEqualWhenSampleNumberDiffers() {
        SampleId id1 = new SampleId("mnist", 42);
        SampleId id2 = new SampleId("mnist", 43);

        assertNotEquals(id1, id2);
    }

    @Test
    @DisplayName("Обязательный эксперимент 4: Сравнение с null и объектом другого типа возвращает false")
    void shouldReturnFalseWhenComparedWithNullOrDifferentType() {
        SampleId id = new SampleId("imagenet", 100);

        // Сравнение с null
        assertNotEquals(null, id);
        assertFalse(id.equals(null));

        // Сравнение с объектом другого типа
        assertNotEquals("imagenet:100", id);
        assertNotEquals(100, id);
        assertFalse(id.equals("imagenet:100"));
        assertFalse(id.equals(new Object()));
    }

    @Test
    @DisplayName("Рефлексивность контракта equals")
    void shouldBeReflexive() {
        SampleId id = new SampleId("coco", 7);
        assertEquals(id, id);
    }

    @Test
    @DisplayName("Валидация аргументов конструктора SampleId")
    void shouldThrowExceptionOnInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> new SampleId(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new SampleId("   ", 1));
        assertThrows(IllegalArgumentException.class, () -> new SampleId("mnist", -1));
    }

    @Test
    @DisplayName("Обязательный эксперимент 3: Два неравных ключа с намеренно одинаковым hashCode")
    void shouldDemonstrateCollisionWithEqualHashCodes() {
        // В Java строки "FB" и "Ea" имеют одинаковый hashCode = 2236
        assertEquals("FB".hashCode(), "Ea".hashCode());

        SampleId key1 = new SampleId("FB", 10);
        SampleId key2 = new SampleId("Ea", 10);

        // Хеш-коды ключей совпадают (намеренная коллизия)
        assertEquals(key1.hashCode(), key2.hashCode());
        // Но сами ключи логически НЕ равны
        assertNotEquals(key1, key2);

        // При помещении в Map оба ключа сохраняются и не перезаписывают друг друга
        Map<SampleId, String> map = new HashMap<>();
        map.put(key1, "Значение 1");
        map.put(key2, "Значение 2");

        assertEquals(2, map.size());
        assertEquals("Значение 1", map.get(key1));
        assertEquals("Значение 2", map.get(key2));
    }

    @Test
    @DisplayName("Проверка геттеров и toString")
    void shouldReturnCorrectPropertiesAndToString() {
        SampleId id = new SampleId("iris", 15);
        assertEquals("iris", id.getDataset());
        assertEquals(15, id.getSampleNumber());
        assertTrue(id.toString().contains("iris"));
        assertTrue(id.toString().contains("15"));
    }
}
