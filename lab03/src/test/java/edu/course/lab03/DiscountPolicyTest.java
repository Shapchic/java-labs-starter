package edu.course.lab03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountPolicyTest {

    @Test
    @DisplayName("NoDiscount возвращает исходную цену без изменений")
    void testNoDiscountReturnsOriginalPrice() {
        DiscountPolicy policy = new NoDiscount();
        assertEquals(100.0, policy.apply(100.0), 1e-9);
        assertEquals(0.0, policy.apply(0.0), 1e-9);
        assertEquals(49.99, policy.apply(49.99), 1e-9);
    }

    @Test
    @DisplayName("NoDiscount выбрасывает исключение при отрицательной цене")
    void testNoDiscountNegativePriceThrowsException() {
        DiscountPolicy policy = new NoDiscount();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> policy.apply(-10.0)
        );
        assertEquals("Цена не может быть отрицательной: -10.0", exception.getMessage());
    }

    @Test
    @DisplayName("PercentDiscount корректно рассчитывает скидку 20% от 100 (дает 80)")
    void testPercentDiscountAppliesDiscountCorrectly() {
        DiscountPolicy policy = new PercentDiscount(20.0);
        assertEquals(80.0, policy.apply(100.0), 1e-9);
        assertEquals(20.0, ((PercentDiscount) policy).getPercent(), 1e-9);
    }

    @Test
    @DisplayName("PercentDiscount с 0% возвращает полную стоимость, а со 100% — 0")
    void testPercentDiscountBoundaryPercentages() {
        DiscountPolicy zeroDiscount = new PercentDiscount(0.0);
        assertEquals(100.0, zeroDiscount.apply(100.0), 1e-9);

        DiscountPolicy fullDiscount = new PercentDiscount(100.0);
        assertEquals(0.0, fullDiscount.apply(100.0), 1e-9);
    }

    @Test
    @DisplayName("PercentDiscount выбрасывает исключение при отрицательной цене")
    void testPercentDiscountNegativePriceThrowsException() {
        DiscountPolicy policy = new PercentDiscount(15.0);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> policy.apply(-50.0)
        );
        assertEquals("Цена не может быть отрицательной: -50.0", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -10.0, 100.01, 150.0})
    @DisplayName("PercentDiscount выбрасывает исключение при недопустимом проценте скидки")
    void testPercentDiscountInvalidPercentThrowsException(double invalidPercent) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PercentDiscount(invalidPercent)
        );
    }
}
