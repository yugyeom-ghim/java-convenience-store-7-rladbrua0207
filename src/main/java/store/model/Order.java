package store.model;

public class Order {
    private static final String ERROR_QUANTITY_ZERO_OR_NEGATIVE = "[ERROR] 수량은 0보다 커야 합니다.";
    private static final String ERROR_PRODUCT_NAME_BLANK_MESSAGE = "[ERROR] 상품명은 공백일 수 없습니다.";

    private static final int ZERO = 0;

    private final String productName;
    private final int quantity;

    public Order(String productName, int quantity) {
        validateProductName(productName);
        validateQuantity(quantity);

        this.productName = productName;
        this.quantity = quantity;
    }

    private void validateProductName(String productName) {
        if (productName.isBlank()) {
            throw new IllegalArgumentException(ERROR_PRODUCT_NAME_BLANK_MESSAGE);
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= ZERO) {
            throw new IllegalArgumentException(ERROR_QUANTITY_ZERO_OR_NEGATIVE);
        }
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}
