package edu.course.lab04;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SampleRegistry {

    private final Map<SampleId, DataSample> storage;

    //Создает реестр на основе HashMap.
    public SampleRegistry() {
        this.storage = new HashMap<>();
    }

    //Создает реестр с указанной начальной емкостью.
    public SampleRegistry(int initialCapacity) {
        this.storage = new HashMap<>(initialCapacity);
    }

    //Добавляет или заменяет значение по ключу.
    public DataSample put(SampleId id, DataSample sample) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор SampleId не может быть null");
        }
        if (sample == null) {
            throw new IllegalArgumentException("Образец DataSample не может быть null");
        }
        return storage.put(id, sample);
    }

    //Получает значение по ключу.
    public DataSample get(SampleId id) {
        return storage.get(id);
    }

    //Удаляет значение по ключу.
    public DataSample remove(SampleId id) {
        return storage.remove(id);
    }

    //Проверяет наличие ключа в реестре.
    public boolean containsKey(SampleId id) {
        return storage.containsKey(id);
    }

    //Возвращает количество элементов в реестре.
    public int size() {
        return storage.size();
    }

    //Проверяет, пуст ли реестр.
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    //Очищает реестр.
    public void clear() {
        storage.clear();
    }

    //Возвращает неизменяемое представление хранилища.
    public Map<SampleId, DataSample> getAll() {
        return Collections.unmodifiableMap(storage);
    }
}
