package store.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductInitializer {

    private static final String ERROR_FILE_NOT_FOUND_MESSAGE = "[ERROR] 파일이 존재하지 않습니다.";

    private static final String PRODUCTS_RESOURCES_FILE_NAME = "products.md";
    private static final String DELIMITER_COMMA = ",";
    private static final int INITIAL_QUANTITY = 0;
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;

    private final Map<Product, Stock> stocks = new LinkedHashMap<>();
    private final Map<Product, PromotionStock> promotionStocks = new LinkedHashMap<>();
    private final Map<String, Promotion> promotionMap;

    public ProductInitializer(Map<String, Promotion> promotionMap) throws IOException {
        this.promotionMap = promotionMap;
        initProducts();
    }

    private void initProducts() throws IOException {
        BufferedReader bufferedReader = initBufferedReader();
        ignoreFirstLine(bufferedReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            createProduct(line);
        }
    }

    private static BufferedReader initBufferedReader() throws IOException {
        try {
            ClassLoader loader = Stock.class.getClassLoader();
            FileInputStream file = new FileInputStream(
                    Objects.requireNonNull(loader.getResource(PRODUCTS_RESOURCES_FILE_NAME)).getFile());
            return new BufferedReader(new InputStreamReader(file));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(ERROR_FILE_NOT_FOUND_MESSAGE);
        }
    }

    private void ignoreFirstLine(BufferedReader bufferedReader) throws IOException {
        bufferedReader.readLine();
    }

    private void createProduct(String line) {
        List<String> options = Arrays.stream(line.split(DELIMITER_COMMA)).toList();
        Product product = createProductFromOptions(options);
        int quantity = Integer.parseInt(options.get(QUANTITY_INDEX));
        saveProductStock(product, findPromotion(options), quantity);
    }

    private Product createProductFromOptions(List<String> options) {
        String name = options.get(NAME_INDEX);
        int price = Integer.parseInt(options.get(PRICE_INDEX));
        return new Product(name, price);
    }

    private Promotion findPromotion(List<String> options) {
        return promotionMap.get(options.get(PROMOTION_INDEX));
    }

    private void saveProductStock(Product product, Promotion promotion, int quantity) {
        if (promotion != null) {
            promotionStocks.put(product, new PromotionStock(product, quantity, promotion));
            if (!stocks.containsKey(product)) {
                stocks.put(product, new Stock(product, INITIAL_QUANTITY));
            }
            return;
        }
        stocks.put(product, new Stock(product, quantity));
    }

    public Map<Product, Stock> getStocks() {
        return stocks;
    }

    public Map<Product, PromotionStock> getPromotionStocks() {
        return promotionStocks;
    }
}
