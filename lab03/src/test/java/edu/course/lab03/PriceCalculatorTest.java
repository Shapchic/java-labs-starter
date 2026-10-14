package edu.course.lab03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceCalculatorTest {

    @Test
    @DisplayName("PriceCalculator с NoDiscount рассчитывает цену без изменений")
    void testCalculateWithNoDiscount() {
        DiscountPolicy policy = new NoDiscount();
        PriceCalculator calculator = new PriceCalculator(policy);

        assertEquals(150.0, calculator.calculatePrice(150.0), 1e-9);
        assertEquals(150.0, calculator.calculate(150.0), 1e-9);
        assertSame(policy, calculator.getDiscountPolicy());
    }

    @Test
    @DisplayName("PriceCalculator с PercentDiscount рассчитывает цену со скидкой")
    void testCalculateWithPercentDiscount() {
        DiscountPolicy policy = new PercentDiscount(20.0);
        PriceCalculator calculator = new PriceCalculator(policy);

        assertEquals(80.0, calculator.calculatePrice(100.0), 1e-9);
        assertEquals(80.0, calculator.calculate(100.0), 1e-9);
        assertSame(policy, calculator.getDiscountPolicy());
    }

    @Test
    @DisplayName("Тест замены политики скидок в PriceCalculator без изменения самого калькулятора")
    void testPolicySubstitution() {
        DiscountPolicy noDiscount = new NoDiscount();
        DiscountPolicy twentyPercent = new PercentDiscount(20.0);
        DiscountPolicy halfOff = new PercentDiscount(50.0);

        PriceCalculator calculator = new PriceCalculator(noDiscount);
        assertEquals(100.0, calculator.calculatePrice(100.0), 1e-9);

        // Замена политики на 20%
        calculator.changeDiscountPolicy(twentyPercent);
        assertEquals(80.0, calculator.calculatePrice(100.0), 1e-9);
        assertSame(twentyPercent, calculator.getDiscountPolicy());

        // Замена политики на 50%
        calculator.changeDiscountPolicy(halfOff);
        assertEquals(50.0, calculator.calculatePrice(100.0), 1e-9);
        assertSame(halfOff, calculator.getDiscountPolicy());
    }

    @Test
    @DisplayName("Конструктор PriceCalculator выбрасывает исключение при передаче null")
    void testConstructorNullThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PriceCalculator(null)
        );
        assertEquals("Политика скидок не может быть null", exception.getMessage());
    }

    @Test
    @DisplayName("changeDiscountPolicy выбрасывает исключение при передаче null")
    void testChangePolicyNullThrowsException() {
        PriceCalculator calculator = new PriceCalculator(new NoDiscount());
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.changeDiscountPolicy(null)
        );
    }
}
