package edu.course.lab02;

/**
 * Неизменяемый идентификатор задачи (Value Object / объект-значение).
 * Представляет значение, так как равенство определяется исключительно содержимым поля value,
 * а сам объект не имеет изменяемого жизненного цикла или скрытого изменяемого состояния.
 */
public record TaskId(String value) {

    public TaskId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Идентификатор задачи не может быть null или пустым");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
