package store.model;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Stocks {

    public static final String ERROR_NOTFOUND_PRODUCT_MESSAGE = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";

    private final Map<Product, Stock> stocks = new LinkedHashMap<>();
    private final Map<Product, PromotionStock> promotionStocks = new LinkedHashMap<>();

    public Stocks() throws IOException {
        PromotionInitializer promotionInitializer = new PromotionInitializer();
        promotionInitializer.initPromotions();

        ProductInitializer productInitializer = new ProductInitializer(
                stocks,
                promotionStocks,
                promotionInitializer.getPromotionMap()
        );
        productInitializer.initProducts();
    }

    public boolean hasProduct(String name) {
        return stocks.containsKey(findProduct(name));
    }

    public Product findProduct(String name) {
        for (Product product : stocks.keySet()) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        throw new IllegalArgumentException(ERROR_NOTFOUND_PRODUCT_MESSAGE);
    }
    public Stock getStock(Product product) {
        return stocks.get(product);
    }
}
