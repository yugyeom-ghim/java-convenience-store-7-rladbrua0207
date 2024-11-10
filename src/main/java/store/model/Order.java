package store.model;

import java.util.Arrays;
import java.util.List;
public class Order {
    private static final String ERROR_QUANTITY_NOT_NUMBER = "[ERROR] 수량은 숫자여야 합니다.";
    private static final String ERROR_QUANTITY_ZERO_OR_NEGATIVE = "[ERROR] 수량은 0보다 커야 합니다.";
    private static final String ERROR_PRODUCT_NAME_BLANK_MESSAGE = "[ERROR] 상품명은 공백일 수 없습니다.";

    private static final String DELIMITER_HYPHEN = "-";
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int QUANTITY_INDEX = 1;

    private final String productName;
    private final int quantity;

    public Order(String orderInfo) {
        List<String> parts = Arrays.stream(orderInfo.split(DELIMITER_HYPHEN)).toList();
        validateProductName(parts.get(PRODUCT_NAME_INDEX));
        validateQuantity(parts.get(QUANTITY_INDEX));

        this.productName = parts.get(PRODUCT_NAME_INDEX);
        this.quantity = Integer.parseInt(parts.get(QUANTITY_INDEX));
    }

    private void validateProductName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException(ERROR_PRODUCT_NAME_BLANK_MESSAGE);
        }
    }

    private void validateQuantity(String quantityStr) {
        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new IllegalArgumentException(ERROR_QUANTITY_ZERO_OR_NEGATIVE);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_QUANTITY_NOT_NUMBER);
        }
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}
