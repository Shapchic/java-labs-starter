package edu.course.lab04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты реестра SampleRegistry и обязательные эксперименты")
class SampleRegistryTest {

    private SampleRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new SampleRegistry();
    }

    @Test
    @DisplayName("Эксперимент 1: Два разных экземпляра логически равного ключа находят одно значение")
    void shouldFindSameValueByDifferentInstancesOfLogicallyEqualKey() {
        SampleId key1 = new SampleId("mnist", 101);
        SampleId key2 = new SampleId("mnist", 101);

        // Убеждаемся, что это два разных объекта в куче (разная идентичность)
        assertNotSame(key1, key2);
        // Но они логически равны и имеют одинаковый хеш-код
        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());

        DataSample sample = new DataSample(key1, "digit-5", new double[]{0.1, 0.9});
        registry.put(key1, sample);

        // Поиск по второму экземпляру ключа возвращает сохраненное значение
        assertTrue(registry.containsKey(key2));
        DataSample retrieved = registry.get(key2);
        assertNotNull(retrieved);
        assertSame(sample, retrieved);
    }

    @Test
    @DisplayName("Эксперимент 2: Повторное добавление с равным ключом заменяет значение и не увеличивает размер")
    void shouldReplaceValueAndNotIncreaseSizeWhenAddingWithEqualKey() {
        SampleId key1 = new SampleId("mnist", 202);
        SampleId key2 = new SampleId("mnist", 202);

        DataSample initialSample = new DataSample(key1, "label-old", new double[]{1.0});
        DataSample updatedSample = new DataSample(key2, "label-new", new double[]{2.0});

        registry.put(key1, initialSample);
        assertEquals(1, registry.size());
        assertEquals("label-old", registry.get(key1).getLabel());

        // Добавляем по логически равному ключу key2
        DataSample previous = registry.put(key2, updatedSample);

        // Размер не увеличился, старое значение возвращено методом put
        assertEquals(1, registry.size());
        assertSame(initialSample, previous);

        // Значение по обоим ключам обновилось
        assertSame(updatedSample, registry.get(key1));
        assertSame(updatedSample, registry.get(key2));
        assertEquals("label-new", registry.get(key1).getLabel());
    }

    @Test
    @DisplayName("Эксперимент 3: Два неравных ключа с намеренно одинаковым hashCode сохраняют разные значения")
    void shouldStoreDifferentValuesForUnequalKeysWithIdenticalHashCode() {
        // Строки "FB" и "Ea" в Java обладают одинаковым hashCode (2236)
        assertEquals("FB".hashCode(), "Ea".hashCode());

        SampleId key1 = new SampleId("FB", 777);
        SampleId key2 = new SampleId("Ea", 777);

        // Хеш-коды идентичны (коллизия хешей)
        assertEquals(key1.hashCode(), key2.hashCode());
        // Но сами ключи логически не равны
        assertNotEquals(key1, key2);

        DataSample sample1 = new DataSample(key1, "collision-sample-1");
        DataSample sample2 = new DataSample(key2, "collision-sample-2");

        registry.put(key1, sample1);
        registry.put(key2, sample2);

        // Хеш-таблица успешно разрешила коллизию: размер равен 2
        assertEquals(2, registry.size());
        assertSame(sample1, registry.get(key1));
        assertSame(sample2, registry.get(key2));
    }

    @Test
    @DisplayName("Эксперимент 4: Сравнение ключа с null и объектом другого типа возвращает false")
    void shouldReturnFalseWhenComparedToNullOrOtherTypes() {
        SampleId key = new SampleId("cifar10", 303);

        assertNotEquals(null, key);
        assertFalse(key.equals(null));

        assertNotEquals("cifar10#303", key);
        assertNotEquals(303, key);
        assertFalse(key.equals("cifar10#303"));
    }

    @Test
    @DisplayName("Эксперимент 5: Проблема изменяемого ключа — после изменения поля поиск перестает работать")
    void shouldDemonstrateProblemOfMutableKeyInHashMap() {
        // Создаем изменяемый учебный ключ
        MutableSampleKey mutableKey = new MutableSampleKey("mnist", 999);
        DataSample sample = new DataSample(new SampleId("mnist", 999), "digit-9");

        Map<MutableSampleKey, DataSample> map = new HashMap<>();
        map.put(mutableKey, sample);

        // Изначально объект доступен по ключу
        assertTrue(map.containsKey(mutableKey));
        assertSame(sample, map.get(mutableKey));
        int initialHashCode = mutableKey.hashCode();

        // Изменяем поле ключа (мутация состояния)
        mutableKey.setDataset("cifar10");
        int mutatedHashCode = mutableKey.hashCode();

        // Хеш-код объекта изменился
        assertNotEquals(initialHashCode, mutatedHashCode);

        // ПОИСК ПЕРЕСТАЛ РАБОТАТЬ!
        // HashMap вычисляет корзину по новому hashCode, где объект отсутствует
        assertNull(map.get(mutableKey));
        assertFalse(map.containsKey(mutableKey));

        // Поиск по исходному неизмененному ключу также не сработает,
        // так как equals сравнивает с мутировавшим объектом и вернет false
        MutableSampleKey originalLookupKey = new MutableSampleKey("mnist", 999);
        assertNull(map.get(originalLookupKey));

        // При этом объект остался «застрявшим» в таблице (размер по-прежнему 1)
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("Операция получения: get")
    void shouldGetSampleCorrectly() {
        SampleId id = new SampleId("imagenet", 1);
        DataSample sample = new DataSample(id, "cat");
        registry.put(id, sample);

        assertEquals(sample, registry.get(id));

        SampleId unknownId = new SampleId("imagenet", 999);
        assertNull(registry.get(unknownId));
    }

    @Test
    @DisplayName("Операция удаления: remove уменьшает размер и возвращает удаленный элемент")
    void shouldRemoveSampleCorrectly() {
        SampleId id1 = new SampleId("imagenet", 1);
        SampleId id2 = new SampleId("imagenet", 2);
        DataSample sample1 = new DataSample(id1, "cat");
        DataSample sample2 = new DataSample(id2, "dog");

        registry.put(id1, sample1);
        registry.put(id2, sample2);
        assertEquals(2, registry.size());

        DataSample removed = registry.remove(id1);
        assertSame(sample1, removed);
        assertEquals(1, registry.size());
        assertFalse(registry.containsKey(id1));
        assertTrue(registry.containsKey(id2));

        // Повторное удаление возвращает null
        assertNull(registry.remove(id1));
    }

    @Test
    @DisplayName("Проверка наличия ключа: containsKey")
    void shouldCheckPresenceOfKey() {
        SampleId id = new SampleId("mnist", 5);
        DataSample sample = new DataSample(id, "digit-5");

        assertFalse(registry.containsKey(id));

        registry.put(id, sample);

        assertTrue(registry.containsKey(id));
    }

    @Test
    @DisplayName("Операции размера: size, isEmpty, clear")
    void shouldHandleSizeAndClear() {
        assertTrue(registry.isEmpty());
        assertEquals(0, registry.size());

        SampleId id = new SampleId("mnist", 1);
        registry.put(id, new DataSample(id, "label"));

        assertFalse(registry.isEmpty());
        assertEquals(1, registry.size());

        registry.clear();
        assertTrue(registry.isEmpty());
        assertEquals(0, registry.size());
    }

    @Test
    @DisplayName("Валидация аргументов: запрет null ключей и значений")
    void shouldThrowExceptionWhenPassingNullArguments() {
        SampleId id = new SampleId("mnist", 1);
        DataSample sample = new DataSample(id, "label");

        assertThrows(IllegalArgumentException.class, () -> registry.put(null, sample));
        assertThrows(IllegalArgumentException.class, () -> registry.put(id, null));
    }

    @Test
    @DisplayName("Метод getAll возвращает неизменяемое представление хранилища")
    void shouldReturnUnmodifiableMap() {
        SampleId id = new SampleId("mnist", 1);
        registry.put(id, new DataSample(id, "label"));

        Map<SampleId, DataSample> all = registry.getAll();
        assertEquals(1, all.size());
        assertThrows(UnsupportedOperationException.class, () -> all.remove(id));
    }
}
