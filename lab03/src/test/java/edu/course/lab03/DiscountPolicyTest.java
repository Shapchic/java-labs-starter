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
    void testNoDiscountAppliesCorrectly() {
        DiscountPolicy policy = new NoDiscount();
        assertEquals(100.0, policy.apply(100.0), 1e-9);
        assertEquals(0.0, policy.apply(0.0), 1e-9);
        assertEquals(250.75, policy.apply(250.75), 1e-9);
    }

    @Test
    @DisplayName("NoDiscount выбрасывает исключение при отрицательной цене")
    void testNoDiscountRejectsNegativePrice() {
        DiscountPolicy policy = new NoDiscount();
        assertThrows(IllegalArgumentException.class, () -> policy.apply(-1.0));
        assertThrows(IllegalArgumentException.class, () -> policy.apply(-100.0));
    }

    @Test
    @DisplayName("PercentDiscount 20% от цены 100 дает 80 (требование ТЗ)")
    void testPercentDiscountSpecificationExample() {
        DiscountPolicy policy = new PercentDiscount(20.0);
        assertEquals(80.0, policy.apply(100.0), 1e-9);
    }

    @Test
    @DisplayName("PercentDiscount с 0% возвращает исходную стоимость")
    void testPercentDiscountZeroPercent() {
        DiscountPolicy policy = new PercentDiscount(0.0);
        assertEquals(500.0, policy.apply(500.0), 1e-9);
    }

    @Test
    @DisplayName("PercentDiscount со 100% возвращает 0.0")
    void testPercentDiscountHundredPercent() {
        DiscountPolicy policy = new PercentDiscount(100.0);
        assertEquals(0.0, policy.apply(500.0), 1e-9);
    }

    @Test
    @DisplayName("PercentDiscount с дробным процентом скидки")
    void testPercentDiscountFractional() {
        DiscountPolicy policy = new PercentDiscount(12.5);
        assertEquals(175.0, policy.apply(200.0), 1e-9);
    }

    @Test
    @DisplayName("PercentDiscount выбрасывает исключение при отрицательной цене")
    void testPercentDiscountRejectsNegativePrice() {
        DiscountPolicy policy = new PercentDiscount(10.0);
        assertThrows(IllegalArgumentException.class, () -> policy.apply(-0.01));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -10.0, 100.01, 150.0})
    @DisplayName("PercentDiscount выбрасывает исключение при проценте вне [0, 100]")
    void testPercentDiscountRejectsInvalidPercent(double invalidPercent) {
        assertThrows(IllegalArgumentException.class, () -> new PercentDiscount(invalidPercent));
    }
}
