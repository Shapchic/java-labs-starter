package edu.course.lab04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты подготовительной части: класс Book и контракты equals/hashCode")
class BookTest {

    @Test
    @DisplayName("Значение, помещенное в HashMap по одному экземпляру Book, находится по другому экземпляру с тем же ISBN")
    void shouldFindValueInHashMapByDifferentInstanceWithSameIsbn() {
        Book book1 = new Book("978-0-13-468599-1", "Effective Java (2nd Edition)");
        Book book2 = new Book("978-0-13-468599-1", "Effective Java (3rd Edition)");

        Map<Book, String> library = new HashMap<>();
        library.put(book1, "Полка A-12");

        // Книги имеют разное название, но одинаковый ISBN, поэтому логически равны
        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());

        // Поиск по другому экземпляру должен успешно возвращать сохраненное значение
        assertTrue(library.containsKey(book2));
        assertEquals("Полка A-12", library.get(book2));
    }

    @Test
    @DisplayName("Книги с разными ISBN не равны и не перезаписывают друг друга в HashMap")
    void shouldNotBeEqualWhenIsbnDiffers() {
        Book book1 = new Book("978-0-13-468599-1", "Effective Java");
        Book book2 = new Book("978-0-596-00920-5", "Head First Design Patterns");

        assertNotEquals(book1, book2);

        Map<Book, String> library = new HashMap<>();
        library.put(book1, "Полка A-12");
        library.put(book2, "Полка B-05");

        assertEquals(2, library.size());
        assertEquals("Полка A-12", library.get(book1));
        assertEquals("Полка B-05", library.get(book2));
    }

    @Test
    @DisplayName("Проверка контракта equals: рефлексивность, симметричность, сравнение с null и чужим типом")
    void shouldFollowEqualsContract() {
        Book book = new Book("978-0-13-468599-1", "Effective Java");
        Book sameBook = new Book("978-0-13-468599-1", "Effective Java");
        Book anotherBook = new Book("978-0-201-63361-0", "Design Patterns");

        // Рефлексивность
        assertEquals(book, book);

        // Симметричность
        assertEquals(book, sameBook);
        assertEquals(sameBook, book);

        // Сравнение с null возвращает false
        assertNotEquals(null, book);

        // Сравнение с объектом другого типа возвращает false
        assertNotEquals("978-0-13-468599-1", book);
        assertNotEquals(book, "978-0-13-468599-1");

        // Неравенство
        assertNotEquals(book, anotherBook);
    }

    @Test
    @DisplayName("Валидация аргументов конструктора Book")
    void shouldValidateConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new Book(null, "Title"));
        assertThrows(IllegalArgumentException.class, () -> new Book("   ", "Title"));
        assertThrows(IllegalArgumentException.class, () -> new Book("978-0-13", null));
        assertThrows(IllegalArgumentException.class, () -> new Book("978-0-13", "   "));
    }

    @Test
    @DisplayName("Проверка геттеров и toString")
    void shouldReturnCorrectPropertiesAndToString() {
        Book book = new Book("978-0-13-468599-1", "Effective Java");
        assertEquals("978-0-13-468599-1", book.getIsbn());
        assertEquals("Effective Java", book.getTitle());
        assertTrue(book.toString().contains("978-0-13-468599-1"));
        assertTrue(book.toString().contains("Effective Java"));
    }
}
