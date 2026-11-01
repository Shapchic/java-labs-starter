package edu.course.lab04;

import java.util.Objects;

public final class Book {

    private final String isbn;
    private final String title;

    //Создает объект книги с заданным ISBN и названием.
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

    //Возвращает ISBN книги.
    public String getIsbn() {
        return isbn;
    }

    //Возвращает название книги.
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
