package store.model;

public class Membership {

    private static final int DISCOUNT_PERCENTAGE = 30;
    private static final int MAXIMUM_DISCOUNTED_PRICE = 8_000;
    private static final int PERCENTAGE_DENOMINATOR = 100;

    private int discountedPrice = 0;

    public int discount(int price) {
        int discountPrice = (price * DISCOUNT_PERCENTAGE) / PERCENTAGE_DENOMINATOR;

        if (MAXIMUM_DISCOUNTED_PRICE < discountPrice + discountedPrice) {
            return MAXIMUM_DISCOUNTED_PRICE - discountedPrice;
        }

        discountedPrice += discountPrice;
        return discountPrice;
    }
}
