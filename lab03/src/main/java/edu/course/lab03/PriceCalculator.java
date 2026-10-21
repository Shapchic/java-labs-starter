package edu.course.lab03;

public class PriceCalculator {

    private DiscountPolicy discountPolicy;

    //Создает калькулятор с заданной политикой скидок.
    public PriceCalculator(DiscountPolicy discountPolicy) {
        if (discountPolicy == null) {
            throw new IllegalArgumentException("Политика скидок не может быть null");
        }
        this.discountPolicy = discountPolicy;
    }

    //Рассчитывает итоговую цену с учетом установленной политики скидок.
    public double calculatePrice(double price) {
        return this.discountPolicy.apply(price);
    }

    //Заменяет текущую политику скидок на новую.
    public void changeDiscountPolicy(DiscountPolicy discountPolicy) {
        if (discountPolicy == null) {
            throw new IllegalArgumentException("Политика скидок не может быть null");
        }
        this.discountPolicy = discountPolicy;
    }

    //Возвращает текущую политику скидок.
    public DiscountPolicy getDiscountPolicy() {
        return this.discountPolicy;
    }
}
