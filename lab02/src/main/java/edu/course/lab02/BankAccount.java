package edu.course.lab02;

/**
 * Учебный класс банковского счета.
 * Демонстрирует инкапсуляцию состояния и защиту инвариантов.
 */
public class BankAccount {

    private int balance;

    //Создает банковский счет с заданным начальным балансом.
    public BankAccount(int initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным: " + initialBalance);
        }
        this.balance = initialBalance;
    }

    //Пополняет счет на указанную сумму.
    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть положительной: " + amount);
        }
        this.balance += amount;
    }

    //Списывает средства со счета
    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма списания должна быть положительной: " + amount);
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException(
                    "Сумма списания (" + amount + ") превышает текущий остаток на счете (" + this.balance + ")"
            );
        }
        this.balance -= amount;
    }

    //Возвращает текущий остаток на счете.
    public int getBalance() {
        return this.balance;
    }
}
