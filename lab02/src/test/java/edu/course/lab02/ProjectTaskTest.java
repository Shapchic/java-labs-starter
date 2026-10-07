package edu.course.lab02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectTaskTest {

    @Test
    @DisplayName("Успешное создание задачи с корректными параметрами")
    void testCreateTaskSuccess() {
        ProjectTask task = new ProjectTask("TASK-1", "Настроить CI/CD", TaskStatus.NEW, 8);

        assertEquals("TASK-1", task.getId());
        assertEquals("Настроить CI/CD", task.getTitle());
        assertEquals(TaskStatus.NEW, task.getStatus());
        assertEquals(8, task.getEstimatedHours());
        assertFalse(task.isCompleted());
    }

    @Test
    @DisplayName("Успешное создание задачи с использованием TaskId")
    void testCreateTaskWithTaskIdSuccess() {
        TaskId taskId = new TaskId("TASK-2");
        ProjectTask task = new ProjectTask(taskId, "Написать тесты", TaskStatus.IN_PROGRESS, 4);

        assertEquals("TASK-2", task.getId());
        assertEquals(taskId, task.getTaskId());
        assertEquals("Написать тесты", task.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(4, task.getEstimatedHours());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    @DisplayName("Создание задачи с пустым или пробельным id выбрасывает исключение")
    void testCreateTaskWithBlankIdThrowsException(String blankId) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectTask(blankId, "Задача", TaskStatus.NEW, 5)
        );
    }

    @Test
    @DisplayName("Создание задачи с null id выбрасывает исключение")
    void testCreateTaskWithNullIdThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectTask((String) null, "Задача", TaskStatus.NEW, 5)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    @DisplayName("Создание задачи с пустым или пробельным названием выбрасывает исключение")
    void testCreateTaskWithBlankTitleThrowsException(String blankTitle) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectTask("TASK-1", blankTitle, TaskStatus.NEW, 5)
        );
    }

    @Test
    @DisplayName("Создание задачи с null title выбрасывает исключение")
    void testCreateTaskWithNullTitleThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectTask("TASK-1", null, TaskStatus.NEW, 5)
        );
    }

    @Test
    @DisplayName("Создание задачи с null status выбрасывает исключение")
    void testCreateTaskWithNullStatusThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectTask("TASK-1", "Задача", null, 5)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    @DisplayName("Создание задачи с неположительной оценкой трудоемкости выбрасывает исключение")
    void testCreateTaskWithNonPositiveEstimatedHoursThrowsException(int invalidHours) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ProjectTask("TASK-1", "Задача", TaskStatus.NEW, invalidHours)
        );
    }

    @Test
    @DisplayName("Успешное изменение статуса задачи")
    void testChangeStatusSuccess() {
        ProjectTask task = new ProjectTask("TASK-1", "Реализовать фичу", TaskStatus.NEW, 10);
        assertFalse(task.isCompleted());

        task.changeStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertFalse(task.isCompleted());

        task.changeStatus(TaskStatus.COMPLETED);
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        assertTrue(task.isCompleted());

        task.changeStatus(TaskStatus.CANCELLED);
        assertEquals(TaskStatus.CANCELLED, task.getStatus());
        assertFalse(task.isCompleted());
    }

    @Test
    @DisplayName("Передача null в changeStatus выбрасывает исключение")
    void testChangeStatusNullThrowsException() {
        ProjectTask task = new ProjectTask("TASK-1", "Реализовать фичу", TaskStatus.NEW, 10);
        assertThrows(IllegalArgumentException.class, () -> task.changeStatus(null));
        assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    @DisplayName("Увеличение оценки трудоемкости на положительное число часов")
    void testAddEstimatedHoursSuccess() {
        ProjectTask task = new ProjectTask("TASK-1", "Рефакторинг", TaskStatus.NEW, 5);

        task.addEstimatedHours(3);
        assertEquals(8, task.getEstimatedHours());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5})
    @DisplayName("Увеличение оценки трудоемкости на неположительное число часов выбрасывает исключение")
    void testAddEstimatedHoursNonPositiveThrowsException(int invalidHours) {
        ProjectTask task = new ProjectTask("TASK-1", "Рефакторинг", TaskStatus.NEW, 5);

        assertThrows(IllegalArgumentException.class, () -> task.addEstimatedHours(invalidHours));
        assertEquals(5, task.getEstimatedHours());
    }

    @Test
    @DisplayName("Тестирование неизменяемого идентификатора TaskId как Value Object")
    void testTaskIdValueObject() {
        TaskId id1 = new TaskId("ID-123");
        TaskId id2 = new TaskId("ID-123");
        TaskId id3 = new TaskId("ID-999");

        // Эквивалентность по значению
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, id3);
        assertEquals("ID-123", id1.value());
        assertEquals("ID-123", id1.toString());

        // Валидация
        assertThrows(IllegalArgumentException.class, () -> new TaskId(null));
        assertThrows(IllegalArgumentException.class, () -> new TaskId(""));
        assertThrows(IllegalArgumentException.class, () -> new TaskId("   "));
    }
}
