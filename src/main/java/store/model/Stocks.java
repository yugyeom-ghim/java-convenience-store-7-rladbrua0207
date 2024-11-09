package store.model;

import java.io.IOException;
import java.util.LinkedHashMap;
import org.graalvm.collections.Pair;

public class Stocks {

    private final LinkedHashMap<Product, Stock> productsStock = new LinkedHashMap<>();
    private final LinkedHashMap<Product, Pair<Promotion, Stock>> promotionProductsStock = new LinkedHashMap<>();

    public Stocks() throws IOException {
        PromotionInitializer promotionInitializer = new PromotionInitializer();
        promotionInitializer.initPromotions();

        ProductInitializer productInitializer = new ProductInitializer(
                productsStock,
                promotionProductsStock,
                promotionInitializer.getPromotionMap()
        );
        productInitializer.initProducts();
    }
}
