package store.model;

public class Product {

    private static final String ERROR_PRODUCT_NAME_BLACK_MESSAGE = "[ERROR] 상품명은 공백일 수 없습니다.";
    private static final String ERROR_PRODUCT_PRICE_NEGATIVE_OR_ZERO_MESSAGE = "[ERROR] 상품 가격은 0 이하일 수 없습니다.";

    private static final int ZERO = 0;

    private String name;
    private int price;

    public Product(String name, int price) {
        validateName(name);
        validatePrice(price);

        this.name = name;
        this.price = price;
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException(ERROR_PRODUCT_NAME_BLACK_MESSAGE);
        }
    }

    private void validatePrice(int price) {
        if (price <= ZERO) {
            throw new IllegalArgumentException(ERROR_PRODUCT_PRICE_NEGATIVE_OR_ZERO_MESSAGE);
        }
    }

    public String getName() {
        return name;
    }
}
