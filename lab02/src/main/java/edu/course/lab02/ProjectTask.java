package edu.course.lab02;

import java.util.Objects;

/**
 * Задача проекта (доменная модель для направления «Программная инженерия»).
 * Обеспечивает инкапсуляцию состояния и защиту бизнес-инвариантов.
 */
public class ProjectTask {

    private final String id;
    private final String title;
    private TaskStatus status;
    private int estimatedHours;

    //Создает задачу проекта со строковым идентификатором.
    public ProjectTask(String id, String title, TaskStatus status, int estimatedHours) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Идентификатор задачи не может быть null или пустым");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название задачи не может быть null или пустым");
        }
        if (status == null) {
            throw new IllegalArgumentException("Статус задачи не может быть null");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Оценка трудоемкости должна быть строго положительной (> 0): " + estimatedHours);
        }

        this.id = id;
        this.title = title;
        this.status = status;
        this.estimatedHours = estimatedHours;
    }

    //Создает задачу проекта с типизированным идентификатором {@link TaskId}.
    public ProjectTask(TaskId id, String title, TaskStatus status, int estimatedHours) {
        this(
                Objects.requireNonNull(id, "Идентификатор задачи не может быть null").value(),
                title,
                status,
                estimatedHours
        );
    }

    //Изменяет текущий статус задачи.
    public void changeStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Новый статус не может быть null");
        }
        this.status = newStatus;
    }

    //Проверяет, завершена ли задача.
    public boolean isCompleted() {
        return this.status == TaskStatus.COMPLETED;
    }

    //Увеличивает оценку трудоемкости задачи на указанное положительное число часов.
    public void addEstimatedHours(int additionalHours) {
        if (additionalHours <= 0) {
            throw new IllegalArgumentException("Дополнительное число часов должно быть положительным: " + additionalHours);
        }
        this.estimatedHours += additionalHours;
    }

    //Возвращает строковый идентификатор задачи.
    public String getId() {
        return this.id;
    }

    //Возвращает идентификатор задачи в виде Value Object {@link TaskId}.
    public TaskId getTaskId() {
        return new TaskId(this.id);
    }

    //Возвращает название задачи.
    public String getTitle() {
        return this.title;
    }

    //Возвращает текущий статус задачи.
    public TaskStatus getStatus() {
        return this.status;
    }

    //Возвращает оценку трудоемкости в часах.
    public int getEstimatedHours() {
        return this.estimatedHours;
    }
}
