package edu.course.lab04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты ChainedHashTable (Дополнительная часть)")
class ChainedHashTableTest {

    private ChainedHashTable table;

    @BeforeEach
    void setUp() {
        table = new ChainedHashTable(4, 0.75f);
    }

    @Test
    @DisplayName("Базовые операции добавления и получения")
    void shouldPutAndGetSamples() {
        SampleId id1 = new SampleId("mnist", 1);
        SampleId id2 = new SampleId("mnist", 2);
        DataSample sample1 = new DataSample(id1, "one");
        DataSample sample2 = new DataSample(id2, "two");

        assertNull(table.put(id1, sample1));
        assertNull(table.put(id2, sample2));

        assertEquals(2, table.size());
        assertSame(sample1, table.get(id1));
        assertSame(sample2, table.get(id2));
    }

    @Test
    @DisplayName("Поиск по логически равному новому экземпляру ключа")
    void shouldFindSampleByEqualKeyInstance() {
        SampleId keyStored = new SampleId("cifar", 10);
        SampleId keyLookup = new SampleId("cifar", 10);
        assertNotSame(keyStored, keyLookup);

        DataSample sample = new DataSample(keyStored, "cat");
        table.put(keyStored, sample);

        assertTrue(table.containsKey(keyLookup));
        assertSame(sample, table.get(keyLookup));
    }

    @Test
    @DisplayName("Замена значения при повторном добавлении с равным ключом")
    void shouldReplaceValueWhenKeyAlreadyExists() {
        SampleId key1 = new SampleId("iris", 1);
        SampleId key2 = new SampleId("iris", 1);

        DataSample sample1 = new DataSample(key1, "setosa");
        DataSample sample2 = new DataSample(key2, "versicolor");

        assertNull(table.put(key1, sample1));
        assertEquals(1, table.size());

        DataSample old = table.put(key2, sample2);
        assertSame(sample1, old);
        assertEquals(1, table.size());
        assertSame(sample2, table.get(key1));
    }

    @Test
    @DisplayName("Разрешение коллизий методом цепочек")
    void shouldHandleCollisionsGracefully() {
        // "FB" и "Ea" дают одинаковый hashCode
        SampleId key1 = new SampleId("FB", 100);
        SampleId key2 = new SampleId("Ea", 100);
        assertEquals(key1.hashCode(), key2.hashCode());
        assertNotEquals(key1, key2);

        DataSample s1 = new DataSample(key1, "val1");
        DataSample s2 = new DataSample(key2, "val2");

        table.put(key1, s1);
        table.put(key2, s2);

        assertEquals(2, table.size());
        assertSame(s1, table.get(key1));
        assertSame(s2, table.get(key2));

        // Удаление одного из элементов коллизии
        DataSample removed = table.remove(key1);
        assertSame(s1, removed);
        assertEquals(1, table.size());
        assertNull(table.get(key1));
        assertSame(s2, table.get(key2));
    }

    @Test
    @DisplayName("Удаление элементов: из начала цепочки, середины и отсутствующих")
    void shouldRemoveElementsCorrectly() {
        SampleId id1 = new SampleId("set", 1);
        SampleId id2 = new SampleId("set", 2);
        DataSample sample1 = new DataSample(id1, "s1");
        DataSample sample2 = new DataSample(id2, "s2");

        table.put(id1, sample1);
        table.put(id2, sample2);

        assertEquals(sample1, table.remove(id1));
        assertEquals(1, table.size());
        assertFalse(table.containsKey(id1));

        // Удаление несуществующего ключа возвращает null
        assertNull(table.remove(new SampleId("unknown", 999)));
        assertNull(table.remove(null));
        assertEquals(1, table.size());
    }

    @Test
    @DisplayName("Динамическое расширение емкости (resize и rehash)")
    void shouldResizeAndRehashWhenThresholdExceeded() {
        int initialCapacity = table.getCapacity();
        assertEquals(4, initialCapacity);

        // Добавляем элементы для превышения threshold = 4 * 0.75 = 3
        for (int i = 0; i < 20; i++) {
            SampleId id = new SampleId("ds", i);
            table.put(id, new DataSample(id, "label" + i));
        }

        assertEquals(20, table.size());
        assertTrue(table.getCapacity() > initialCapacity);

        // Проверяем, что все 20 элементов успешно находятся после рехеширования
        for (int i = 0; i < 20; i++) {
            SampleId id = new SampleId("ds", i);
            DataSample sample = table.get(id);
            assertNotNull(sample);
            assertEquals("label" + i, sample.getLabel());
        }
    }

    @Test
    @DisplayName("Очистка и проверка пустоты таблицы")
    void shouldClearTable() {
        assertTrue(table.isEmpty());
        SampleId id = new SampleId("test", 1);
        table.put(id, new DataSample(id, "test"));

        assertFalse(table.isEmpty());
        assertEquals(1, table.size());

        table.clear();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
        assertNull(table.get(id));
    }

    @Test
    @DisplayName("Валидация аргументов методов таблицы")
    void shouldValidateArguments() {
        SampleId id = new SampleId("test", 1);
        DataSample sample = new DataSample(id, "test");

        assertThrows(IllegalArgumentException.class, () -> table.put(null, sample));
        assertThrows(IllegalArgumentException.class, () -> table.put(id, null));

        assertNull(table.get(null));
        assertFalse(table.containsKey(null));

        assertThrows(IllegalArgumentException.class, () -> new ChainedHashTable(0));
        assertThrows(IllegalArgumentException.class, () -> new ChainedHashTable(16, -0.5f));
    }
}
