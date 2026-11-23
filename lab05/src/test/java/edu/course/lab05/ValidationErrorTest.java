package edu.course.lab05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование класса ошибки валидации ValidationError")
class ValidationErrorTest {

    @Test
    @DisplayName("Корректное сохранение свойств и доступ через геттеры")
    void shouldStorePropertiesCorrectly() {
        ValidationError error = new ValidationError(5, "featureCount", "Неверный формат числа");

        assertEquals(5, error.getLineNumber());
        assertEquals("featureCount", error.getField());
        assertEquals("Неверный формат числа", error.getMessage());

        // Проверка record-style методов доступа
        assertEquals(5, error.lineNumber());
        assertEquals("featureCount", error.field());
        assertEquals("Неверный формат числа", error.message());
    }

    @Test
    @DisplayName("Выброс исключения при неположительном номере строки")
    void shouldThrowExceptionForInvalidLineNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                new ValidationError(0, "field", "message"));
        assertThrows(IllegalArgumentException.class, () ->
                new ValidationError(-2, "field", "message"));
    }

    @Test
    @DisplayName("Корректная обработка null значений field и message")
    void shouldHandleNullFieldAndMessageGracefully() {
        ValidationError error = new ValidationError(1, null, null);

        assertEquals("unknown", error.getField());
        assertEquals("", error.getMessage());
    }

    @Test
    @DisplayName("Проверка equals, hashCode и toString")
    void shouldVerifyEqualsHashCodeAndToString() {
        ValidationError err1 = new ValidationError(2, "id", "empty");
        ValidationError err2 = new ValidationError(2, "id", "empty");
        ValidationError err3 = new ValidationError(3, "id", "empty");

        assertEquals(err1, err2);
        assertEquals(err1.hashCode(), err2.hashCode());
        assertNotEquals(err1, err3);
        assertNotEquals(err1, null);

        String str = err1.toString();
        assertTrue(str.contains("2"));
        assertTrue(str.contains("id"));
        assertTrue(str.contains("empty"));
    }
}
