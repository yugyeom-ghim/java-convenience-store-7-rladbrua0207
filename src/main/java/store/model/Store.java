package store.model;

import java.io.IOException;
import store.exception.InvalidQuantityException;
import store.exception.NotFoundException;

public class Store {

    private static final String ERROR_PURCHASE_QUANTITY_NOT_EXCEED_STOCK_QUANTITY =
            "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final Stocks stocks;

    public Store() throws IOException {
        this.stocks = new Stocks();
    }

    public void validatePurchase(Order order) {
        validateProduct(order.getProductName());
        validateStock(order);
    }

    private void validateProduct(String productName) {
        if (!stocks.hasProduct(productName)) {
            throw new NotFoundException(Stocks.ERROR_NOTFOUND_PRODUCT_MESSAGE);
        }
    }

    private void validateStock(Order order) {
        Product product = stocks.findProduct(order.getProductName());
        Stock stock = stocks.getStock(product);

        if (!stock.isAvailableReduce(order.getQuantity())) {
            throw new InvalidQuantityException(ERROR_PURCHASE_QUANTITY_NOT_EXCEED_STOCK_QUANTITY);
        }
    }
}
