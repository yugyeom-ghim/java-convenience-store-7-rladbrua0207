package store.model;

import java.io.IOException;
import java.util.LinkedHashMap;
import org.graalvm.collections.Pair;

public class Stocks {

    private final LinkedHashMap<Product, Stock> productsStock = new LinkedHashMap<>();
    private final LinkedHashMap<Product, Pair<Promotion, Stock>> promotionProductsStock = new LinkedHashMap<>();

    public Stocks(LinkedHashMap<String, Promotion> promotionMap) throws IOException {
        ProductInitializer productInitializer = new ProductInitializer(
                productsStock,
                promotionProductsStock,
                promotionMap
        );

        productInitializer.initProducts();
    }
}
