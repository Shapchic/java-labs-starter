package edu.course.lab04;

import java.util.Objects;

/**
 * Неизменяемый класс книги (подготовительная часть лабораторной работы).
 * Логическое равенство книг определяется исключительно их международным стандартным номером (ISBN).
 */
public final class Book {

    private final String isbn;
    private final String title;

    public Book(String isbn, String title) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN не может быть null или пустым");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название книги не может быть null или пустым");
        }
        this.isbn = isbn.trim();
        this.title = title.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
