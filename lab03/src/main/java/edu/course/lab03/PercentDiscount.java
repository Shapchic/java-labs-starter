package edu.course.lab03;

public class PercentDiscount implements DiscountPolicy {

    private final double percent;

    //Создает политику процентной скидки.
    public PercentDiscount(double percent) {
        if (Double.isNaN(percent) || percent < 0.0 || percent > 100.0) {
            throw new IllegalArgumentException("Процент скидки должен находиться в диапазоне от 0 до 100: " + percent);
        }
        this.percent = percent;
    }

    @Override
    public double apply(double price) {
        if (price < 0.0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной: " + price);
        }
        return price * (1.0 - percent / 100.0);
    }

    //Возвращает установленный процент скидки.
    public double getPercent() {
        return this.percent;
    }
}
