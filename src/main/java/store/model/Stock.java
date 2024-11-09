package store.model;

public class Stock {

    private static final String ERROR_STOCK_QUANTITY_NEGATIVE_MESSAGE = "[ERROR] 재고 수량은 0 미만일 수 없습니다.";

    private static final int ZERO = 0;

    private int quantity;

    public Stock(int quantity) {
        validateQuantity(quantity);

        this.quantity = quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < ZERO) {
            throw new IllegalStateException(ERROR_STOCK_QUANTITY_NEGATIVE_MESSAGE);
        }
    }
}
