package store.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import store.exception.InvalidQuantityException;
import store.exception.NotFoundException;

public class Store {

    private static final String ERROR_PURCHASE_QUANTITY_NOT_EXCEED_STOCK_QUANTITY =
            "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    public static final String ERROR_NOTFOUND_PRODUCT_MESSAGE = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";

    private final Map<Product, Stock> stocks = new LinkedHashMap<>();
    private final Map<Product, PromotionStock> promotionStocks = new LinkedHashMap<>();

    public Store(Map<Product, Stock> stocks, Map<Product, PromotionStock> promotionStocks) throws IOException {
        this.stocks.putAll(stocks);
        this.promotionStocks.putAll(promotionStocks);
    }

    public void validatePurchase(Order order) {
        findProduct(order.getProductName());
        validateStock(order);
    }


    private void validateStock(Order order) {
        Product product = findProduct(order.getProductName());
        Stock stock = getStock(product);

        if (!stock.isAvailableReduce(order.getQuantity())) {
            throw new InvalidQuantityException(ERROR_PURCHASE_QUANTITY_NOT_EXCEED_STOCK_QUANTITY);
        }
    }

    public Product findProduct(String name) {
        for (Product product : stocks.keySet()) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        throw new NotFoundException(ERROR_NOTFOUND_PRODUCT_MESSAGE);
    }

    public void reduceStock(Product product, int quantity) {
        Stock stock = stocks.get(product);
        if (stock == null) {
            throw new IllegalArgumentException(ERROR_NOTFOUND_PRODUCT_MESSAGE);
        }
        stock.reduce(quantity);
    }

    public Stock getStock(Product product) {
        return stocks.get(product);
    }
}
