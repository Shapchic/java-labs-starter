package edu.course.lab05;

import java.util.Objects;

public final class ValidationError {

    private final int lineNumber;
    private final String field;
    private final String message;

    //Создает ошибку валидации для указанной строки и поля.
    public ValidationError(int lineNumber, String field, String message) {
        if (lineNumber < 1) {
            throw new IllegalArgumentException("Номер строки должен быть положительным: " + lineNumber);
        }
        this.lineNumber = lineNumber;
        this.field = field != null ? field : "unknown";
        this.message = message != null ? message : "";
    }

    //Возвращает номер строки в файле.
    public int getLineNumber() {
        return lineNumber;
    }

    //Возвращает имя некорректного поля.
    public String getField() {
        return field;
    }

    //Возвращает текст сообщения об ошибке.
    public String getMessage() {
        return message;
    }

    //Возвращает номер строки.
    public int lineNumber() {
        return lineNumber;
    }

    //Возвращает имя поля.
    public String field() {
        return field;
    }

    //Возвращает сообщение об ошибке.
    public String message() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationError that = (ValidationError) o;
        return lineNumber == that.lineNumber &&
                Objects.equals(field, that.field) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, field, message);
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "lineNumber=" + lineNumber +
                ", field='" + field + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
