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
    private static final int NAME_INDEX = 0;
    private static final int PRICE_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final int PROMOTION_INDEX = 3;

    private final Map<Product, Stock> stocks = new LinkedHashMap<>();
    private final Map<Product, PromotionStock> promotionStocks = new LinkedHashMap<>();
    private final Map<String, Promotion> promotionMap;

    public ProductInitializer(
            Map<String, Promotion> promotionMap
    ) throws IOException {
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
        Product product = new Product(
                options.get(NAME_INDEX),
                Integer.parseInt(options.get(PRICE_INDEX))
        );
        
        int quantity = Integer.parseInt(options.get(QUANTITY_INDEX));
        Promotion promotion = promotionMap.get(options.get(PROMOTION_INDEX));
        addToStock(product, quantity, promotion);
    }

    private void addToStock(Product product, int quantity, Promotion promotion) {
        if (promotion == null) {
            stocks.put(product, new Stock(product, quantity));
            return;
        }
        promotionStocks.put(
                product, 
                new PromotionStock(product, quantity, promotion)
        );
    }

    public Map<Product, Stock> getStocks() {
        return new LinkedHashMap<>(stocks);
    }

    public Map<Product, PromotionStock> getPromotionStocks() {
        return new LinkedHashMap<>(promotionStocks);
    }
}
