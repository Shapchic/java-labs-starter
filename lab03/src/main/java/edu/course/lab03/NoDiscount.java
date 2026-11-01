package edu.course.lab03;

public class NoDiscount implements DiscountPolicy {

    @Override
    public double apply(double price) {
        if (price < 0.0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной: " + price);
        }
        return price;
    }
}
