package store.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import org.graalvm.collections.Pair;

public class ProductInitializer {

    private static final String ERROR_FILE_NOT_FOUND_MESSAGE = "[ERROR] 파일이 존재하지 않습니다.";

    private static final String PRODUCTS_RESOURCES_FILE_NAME = "products.md";
    private static final String DELIMITER_COMMA = ",";
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;

    private final LinkedHashMap<Product, Stock> productsStock;
    private final LinkedHashMap<Product, Pair<Promotion, Stock>> promotionProductsStock;
    private final LinkedHashMap<String, Promotion> promotionMap;

    public ProductInitializer(
            LinkedHashMap<Product, Stock> productsStock,
            LinkedHashMap<Product, Pair<Promotion, Stock>> promotionProductsStock,
            LinkedHashMap<String, Promotion> promotionMap
    ) {
        this.productsStock = productsStock;
        this.promotionProductsStock = promotionProductsStock;
        this.promotionMap = promotionMap;
    }

    public void initProducts() throws IOException {
        BufferedReader bufferedReader = initBufferedReader();
        ignoreFirstLine(bufferedReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            initProduct(line);
        }
    }

    private static BufferedReader initBufferedReader() throws IOException {
        BufferedReader bufferedReader;
        try {
            ClassLoader loader = Stock.class.getClassLoader();
            FileInputStream file = new FileInputStream(
                    Objects.requireNonNull(loader.getResource(PRODUCTS_RESOURCES_FILE_NAME)).getFile());
            bufferedReader = new BufferedReader(new InputStreamReader(file));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(ERROR_FILE_NOT_FOUND_MESSAGE);
        }
        return bufferedReader;
    }

    private void ignoreFirstLine(BufferedReader bufferedReader) throws IOException {
        bufferedReader.readLine();
    }

    private void initProduct(String line) {
        List<String> productOptions = Arrays.stream(line.split(DELIMITER_COMMA)).toList();

        String name = productOptions.get(NAME_INDEX);
        int price = Integer.parseInt(productOptions.get(PRICE_INDEX));
        int quantity = Integer.parseInt(productOptions.get(QUANTITY_INDEX));
        Promotion promotion = promotionMap.get(productOptions.get(PROMOTION_INDEX));

        loadStock(new Product(name, price), new Stock(quantity), promotion);
    }

    private void loadStock(Product product, Stock stock, Promotion promotion) {
        if (promotion == null) {
            productsStock.put(product, stock);
        }

        if (promotion != null) {
            Pair<Promotion, Stock> promotionQuantity = Pair.create(promotion, stock);
            promotionProductsStock.put(product, promotionQuantity);
        }
    }
}
