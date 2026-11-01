package edu.course.lab04;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Реестр образцов данных (SampleRegistry) на основе HashMap.
 * Обеспечивает операции добавления, замены, получения, удаления и проверки наличия по составному ключу SampleId.
 */
public class SampleRegistry {

    private final Map<SampleId, DataSample> storage;

    public SampleRegistry() {
        this.storage = new HashMap<>();
    }

    public SampleRegistry(int initialCapacity) {
        this.storage = new HashMap<>(initialCapacity);
    }

    /**
     * Добавить или заменить значение по ключу.
     *
     * @param id     неизменяемый ключ образца
     * @param sample образец данных
     * @return предыдущее значение, ассоциированное с ключом, либо null
     */
    public DataSample put(SampleId id, DataSample sample) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор SampleId не может быть null");
        }
        if (sample == null) {
            throw new IllegalArgumentException("Образец DataSample не может быть null");
        }
        return storage.put(id, sample);
    }

    /**
     * Алиас операции добавления/замены для выразительности API.
     */
    public void addOrReplace(SampleId id, DataSample sample) {
        put(id, sample);
    }

    /**
     * Удобное добавление образца данных с использованием его собственного id.
     */
    public DataSample put(DataSample sample) {
        if (sample == null) {
            throw new IllegalArgumentException("Образец DataSample не может быть null");
        }
        return put(sample.getId(), sample);
    }

    /**
     * Получить значение по ключу.
     *
     * @param id ключ образца
     * @return образец данных или null, если не найден
     */
    public DataSample get(SampleId id) {
        return storage.get(id);
    }

    /**
     * Получить значение в виде Optional.
     */
    public Optional<DataSample> findById(SampleId id) {
        return Optional.ofNullable(get(id));
    }

    /**
     * Удалить значение по ключу.
     *
     * @param id ключ образца
     * @return удаленный образец данных или null, если ключ отсутствовал
     */
    public DataSample remove(SampleId id) {
        return storage.remove(id);
    }

    /**
     * Проверить наличие ключа в реестре.
     *
     * @param id ключ образца
     * @return true, если ключ присутствует
     */
    public boolean containsKey(SampleId id) {
        return storage.containsKey(id);
    }

    /**
     * Проверить наличие ключа (алиас).
     */
    public boolean contains(SampleId id) {
        return containsKey(id);
    }

    /**
     * Узнать текущий размер реестра.
     */
    public int size() {
        return storage.size();
    }

    /**
     * Проверить, пуст ли реестр.
     */
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    /**
     * Очистить реестр.
     */
    public void clear() {
        storage.clear();
    }

    /**
     * Возвращает неизменяемое представление внутреннего хранилища.
     */
    public Map<SampleId, DataSample> getAll() {
        return Collections.unmodifiableMap(storage);
    }
}
