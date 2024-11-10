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
    private final PromotionStock defaultPromotionStock = new PromotionStock(null, 0, null);

    public Store(Map<Product, Stock> stocks, Map<Product, PromotionStock> promotionStocks) throws IOException {
        this.stocks.putAll(stocks);
        this.promotionStocks.putAll(promotionStocks);
    }

    public void validatePurchase(Order order) {
        validateProduct(order);
        validateStocks(order);
    }

    private void validateProduct(Order order) {
        Product product = findProduct(order.getProductName());

        if (product == null) {
            throw new NotFoundException(ERROR_NOTFOUND_PRODUCT_MESSAGE);
        }
    }

    private void validateStocks(Order order) {
        Product product = findProduct(order.getProductName());

        PromotionStock promotionStock = getPromotionStock(product);
        Stock stock = getStock(product);

        if (order.getQuantity() > promotionStock.getQuantity() + stock.getQuantity()) {
            throw new InvalidQuantityException(ERROR_PURCHASE_QUANTITY_NOT_EXCEED_STOCK_QUANTITY);
        }
    }

    public Product findProduct(String name) {
        for (Product product : stocks.keySet()) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        for (Product product : promotionStocks.keySet()) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public Stock getStock(Product product) {
        return stocks.get(product);
    }

    public int calculateGetMoreFreeQuantity(Order order, LocalDateTime orderTime) {
        PromotionStock promotionStock = getValidPromotionStock(order, orderTime);
        if (promotionStock == null) {
            return 0;
        }
        return calculateRequiredQuantity(order, promotionStock);
    }

    private PromotionStock getValidPromotionStock(Order order, LocalDateTime orderTime) {
        Product product = findProduct(order.getProductName());
        PromotionStock promotionStock = getPromotionStock(product);

        if (!isPromotionValid(promotionStock, orderTime) || !promotionStock.isAvailableReduce(order.getQuantity())) {
            return null;
        }
        return promotionStock;
    }

    public PromotionStock getPromotionStock(Product product) {
        return promotionStocks.getOrDefault(product, defaultPromotionStock);
    }

    private int calculateRequiredQuantity(Order order, PromotionStock promotionStock) {
        Promotion promotion = promotionStock.getPromotion();
        int totalCount = promotion.getBuyCount() + promotion.getFreeCount();
        int remainder = order.getQuantity() % totalCount;

        if (remainder == promotion.getBuyCount()) {
            return promotion.getFreeCount();
        }
        return 0;
    }

    public int calculateRegularPriceQuantity(Order order, LocalDateTime orderTime) {
        Product product = findProduct(order.getProductName());
        PromotionStock promotionStock = getPromotionStock(product);

        if (!isPromotionValid(promotionStock, orderTime)) {
            return 0;
        }

        return calculateNonPromotionQuantity(order, promotionStock);
    }

    private int calculateNonPromotionQuantity(Order order, PromotionStock promotionStock) {
        Promotion promotion = promotionStock.getPromotion();
        if (promotionStock.getQuantity() > order.getQuantity()) {
            return order.getQuantity() - calculateOnlyPromotionQuantity(order.getQuantity(), promotion);
        }
        int availablePromotionQuantity = calculateOnlyPromotionQuantity(
                promotionStock.getQuantity(),
                promotion
        );

        return order.getQuantity() - availablePromotionQuantity;
    }

    public void buyProduct(Order order, LocalDateTime orderTime) {
        Product product = findProduct(order.getProductName());
        PromotionStock promotionStock = getPromotionStock(product);
        Stock stock = getStock(product);

        if (isPromotionValid(promotionStock, orderTime)) {
            applyPromotion(order, promotionStock, stock);
            return;
        }
        stock.reduce(order.getQuantity());
    }

    private void applyPromotion(Order order, PromotionStock promotionStock, Stock stock) {
        int totalPromotionQuantity = calculatePromotionStockQuantity(order.getQuantity(), promotionStock);

        promotionStock.reduce(totalPromotionQuantity);
        int remaining = order.getQuantity() - totalPromotionQuantity;
        stock.reduce(remaining);
    }

    private int calculatePromotionStockQuantity(int orderQuantity, PromotionStock promotionStock) {
        if (orderQuantity > promotionStock.getQuantity()) {
            return promotionStock.getQuantity();
        }
        int promotionSets = calculatePromotionSets(orderQuantity, promotionStock.getPromotion());
        int remaining = orderQuantity - promotionSets;

        return promotionSets + remaining;
    }

    private int calculatePromotionSets(int quantity, Promotion promotion) {
        int totalCountPerSet = promotion.getBuyCount() + promotion.getFreeCount();
        int sets = quantity / totalCountPerSet;
        return sets * totalCountPerSet;
    }

    private boolean isPromotionValid(PromotionStock promotionStock, LocalDateTime orderTime) {
        return promotionStock.getQuantity() != 0 &&
                promotionStock.getPromotion().isValidDate(orderTime);
    }

    private int calculateOnlyPromotionQuantity(int quantity, Promotion promotion) {
        int totalCountPerSet = promotion.getBuyCount() + promotion.getFreeCount();
        int sets = quantity / totalCountPerSet;
        return sets * totalCountPerSet;
    }
}
