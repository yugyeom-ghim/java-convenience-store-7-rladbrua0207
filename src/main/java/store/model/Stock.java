package store.model;

import store.exception.InvalidQuantityException;

public class Stock {

    private static final String ERROR_STOCK_QUANTITY_NEGATIVE_MESSAGE = "[ERROR] 재고 수량은 0 미만일 수 없습니다.";

    private static final int ZERO = 0;

    private final Product product;
    private int quantity;

    public Stock(Product product, int quantity) {
        validateQuantity(quantity);

        this.product = product;
        this.quantity = quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < ZERO) {
            throw new InvalidQuantityException(ERROR_STOCK_QUANTITY_NEGATIVE_MESSAGE);
        }
    }

    public void reduce(int quantity) {
        validateQuantity(this.quantity - quantity);
        this.quantity -= quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailableReduce(int quantity) {
        return this.quantity >= quantity;
    }
}
