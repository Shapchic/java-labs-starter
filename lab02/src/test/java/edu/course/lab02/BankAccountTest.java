package edu.course.lab02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankAccountTest {

    @Test
    @DisplayName("Успешное создание счета с неотрицательным балансом")
    void testCreateAccountSuccess() {
        BankAccount account = new BankAccount(100);
        assertEquals(100, account.getBalance());

        BankAccount zeroAccount = new BankAccount(0);
        assertEquals(0, zeroAccount.getBalance());
    }

    @Test
    @DisplayName("Создание счета с отрицательным начальным балансом выбрасывает исключение")
    void testNegativeInitialBalanceThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new BankAccount(-10)
        );
        assertEquals("Начальный баланс не может быть отрицательным: -10", exception.getMessage());
    }

    @Test
    @DisplayName("Успешное пополнение счета на положительную сумму")
    void testDepositSuccess() {
        BankAccount account = new BankAccount(100);
        account.deposit(50);
        assertEquals(150, account.getBalance());
    }

    @Test
    @DisplayName("Пополнение счета на нулевую или отрицательную сумму выбрасывает исключение")
    void testDepositNonPositiveAmountThrowsException() {
        BankAccount account = new BankAccount(100);

        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-25));
        assertEquals(100, account.getBalance());
    }

    @Test
    @DisplayName("Успешное списание средств со счета")
    void testWithdrawSuccess() {
        BankAccount account = new BankAccount(200);
        account.withdraw(50);
        assertEquals(150, account.getBalance());

        // Списание всего остатка
        account.withdraw(150);
        assertEquals(0, account.getBalance());
    }

    @Test
    @DisplayName("Списание суммы, превышающей остаток, выбрасывает исключение")
    void testWithdrawExceedingBalanceThrowsException() {
        BankAccount account = new BankAccount(100);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> account.withdraw(101)
        );
        assertEquals("Сумма списания (101) превышает текущий остаток на счете (100)", exception.getMessage());
        assertEquals(100, account.getBalance());
    }

    @Test
    @DisplayName("Списание нулевой или отрицательной суммы выбрасывает исключение")
    void testWithdrawNonPositiveAmountThrowsException() {
        BankAccount account = new BankAccount(100);

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-30));
        assertEquals(100, account.getBalance());
    }
}
