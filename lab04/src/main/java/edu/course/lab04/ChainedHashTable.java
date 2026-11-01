package edu.course.lab04;

import java.util.Objects;

/**
 * Хеш-таблица с разрешением коллизий методом цепочек (Chained Hash Table).
 * Реализована специально для профильного типа ключей SampleId и значений DataSample
 * (в рамках дополнительной части лабораторной работы 4 без использования обобщений / generics).
 */
public class ChainedHashTable {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static final class Node {
        final SampleId key;
        DataSample value;
        Node next;

        Node(SampleId key, DataSample value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node[] table;
    private int size;
    private final float loadFactor;
    private int threshold;

    public ChainedHashTable() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public ChainedHashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public ChainedHashTable(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Начальная емкость должна быть больше 0: " + initialCapacity);
        }
        if (loadFactor <= 0.0f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Коэффициент загрузки должен быть положительным: " + loadFactor);
        }

        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.table = new Node[capacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.size = 0;
    }

    /**
     * Помещает пару ключ-значение в хеш-таблицу.
     * Если ключ уже присутствует, старое значение заменяется на новое.
     *
     * @param key   ключ образца
     * @param value значение образца
     * @return предыдущее значение или null, если ключ был добавлен впервые
     */
    public DataSample put(SampleId key, DataSample value) {
        if (key == null) {
            throw new IllegalArgumentException("Ключ не может быть null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Значение не может быть null");
        }

        int index = getBucketIndex(key, table.length);
        Node current = table[index];

        while (current != null) {
            if (key.equals(current.key)) {
                DataSample oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }

        // Вставка нового узла в начало цепочки корзины
        table[index] = new Node(key, value, table[index]);
        size++;

        if (size >= threshold) {
            resize();
        }

        return null;
    }

    /**
     * Получает значение по ключу.
     *
     * @param key ключ образца
     * @return значение образца или null, если ключ не найден
     */
    public DataSample get(SampleId key) {
        if (key == null) {
            return null;
        }

        int index = getBucketIndex(key, table.length);
        Node current = table[index];

        while (current != null) {
            if (key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    /**
     * Удаляет значение по ключу.
     *
     * @param key ключ образца
     * @return удаленное значение или null, если ключ отсутствовал
     */
    public DataSample remove(SampleId key) {
        if (key == null) {
            return null;
        }

        int index = getBucketIndex(key, table.length);
        Node current = table[index];
        Node prev = null;

        while (current != null) {
            if (key.equals(current.key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }

        return null;
    }

    /**
     * Проверяет наличие ключа в хеш-таблице.
     */
    public boolean containsKey(SampleId key) {
        return get(key) != null;
    }

    /**
     * Возвращает текущее количество элементов.
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, пуста ли хеш-таблица.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Очищает хеш-таблицу.
     */
    public void clear() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * loadFactor);
        this.size = 0;
    }

    /**
     * Текущая емкость (длина массива корзин).
     */
    public int getCapacity() {
        return table.length;
    }

    private int hash(SampleId key) {
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }

    private int getBucketIndex(SampleId key, int tableLength) {
        int h = hash(key);
        return Math.floorMod(h, tableLength);
    }

    private void resize() {
        int newCapacity = table.length << 1;
        Node[] newTable = new Node[newCapacity];

        for (Node head : table) {
            Node current = head;
            while (current != null) {
                Node next = current.next;
                int newIndex = getBucketIndex(current.key, newCapacity);

                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }

        this.table = newTable;
        this.threshold = (int) (newCapacity * loadFactor);
    }
}
