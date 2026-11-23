package edu.course.lab05;

public final class DataLoadingException extends RuntimeException {

    //Создает исключение с сообщением и исходной причиной.
    public DataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    //Создает исключение с сообщением.
    public DataLoadingException(String message) {
        super(message);
    }
}
