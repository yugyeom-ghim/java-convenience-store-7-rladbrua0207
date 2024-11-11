package store.model;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import store.exception.InvalidQuantityException;
import store.exception.NotFoundException;

public class Store {

    private static final String ERROR_PURCHASE_QUANTITY_NOT_EXCEED_STOCK_QUANTITY =
            "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
    public static final String ERROR_NOTFOUND_PRODUCT_MESSAGE = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";

    private final Map<Product, Stock> stocks;
    private final Map<Product, PromotionStock> promotionStocks;
    private final PromotionStock defaultPromotionStock = new PromotionStock(null, 0, null);

    private final Map<Product, Integer> purchases = new LinkedHashMap<>();
    private final Map<Product, Integer> free = new LinkedHashMap<>();
    private final Map<Product, Integer> regularPriceProducts = new LinkedHashMap<>();
    private final Membership membership = new Membership();

    private int price = 0;

    public Store(Map<Product, Stock> stocks, Map<Product, PromotionStock> promotionStocks) {
        this.stocks = stocks;
        this.promotionStocks = promotionStocks;
    }

    // 1. 상품 및 재고 조회
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

    public PromotionStock getPromotionStock(Product product) {
        return promotionStocks.getOrDefault(product, defaultPromotionStock);
    }

    // 2. 유효성 검증
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

    // 3. 프로모션 계산
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

    private int calculateRequiredQuantity(Order order, PromotionStock promotionStock) {
        Promotion promotion = promotionStock.getPromotion();
        int totalCount = promotion.getBuyCount() + promotion.getFreeCount();
        int remainder = order.getQuantity() % totalCount;

        if (remainder == promotion.getBuyCount()) {
            return promotion.getFreeCount();
        }
        return 0;
    }

    public int calculateAdditionalRegularPriceQuantity(Order order, LocalDateTime orderTime) {
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

    private int calculatePromotionStockQuantity(
            int orderQuantity, PromotionStock promotionStock, LocalDateTime orderTime
    ) {
        if (promotionStock == null || promotionStock.getPromotion() == null
                || !promotionStock.getPromotion().isValidDate(orderTime)) {
            return 0;
        }
        return Math.min(orderQuantity, promotionStock.getQuantity());
    }

    private boolean isPromotionValid(PromotionStock promotionStock, LocalDateTime orderTime) {
        return promotionStock != null &&
                promotionStock.getPromotion() != null &&
                promotionStock.getQuantity() != 0 &&
                promotionStock.getPromotion().isValidDate(orderTime);
    }

    private int calculateOnlyPromotionQuantity(int quantity, Promotion promotion) {
        int totalCountPerSet = promotion.getBuyCount() + promotion.getFreeCount();
        int sets = quantity / totalCountPerSet;
        return sets * totalCountPerSet;
    }

    // 4. 구매 처리
    public void purchaseProduct(Order order, LocalDateTime orderTime) {
        Product product = findProduct(order.getProductName());
        PromotionStock promotionStock = getPromotionStock(product);
        Stock stock = getStock(product);

        int promotionQuantity = calculatePromotionStockQuantity(order.getQuantity(), promotionStock, orderTime);
        purchaseWithPromotion(order, product, promotionStock, promotionQuantity);
        purchaseWithRegularPrice(order, product, stock, promotionQuantity);
    }

    private void purchaseWithPromotion(Order order, Product product, PromotionStock promotionStock,
                                       int totalPromotionQuantity) {
        promotionStock.reduce(totalPromotionQuantity);
        recordPromotionPurchase(order, product, totalPromotionQuantity);
    }

    private void purchaseWithRegularPrice(Order order, Product product, Stock stock, int calculatedPromotionQuantity) {
        int regularQuantity = order.getQuantity() - calculatedPromotionQuantity;
        stock.reduce(regularQuantity);
        calculateTotalPrice(order, regularQuantity);
        recordPurchasedProduct(product, regularQuantity);
        recordRegularPriceProduct(product, regularQuantity);
    }

    // 5. 구매 기록
    private void recordPromotionPurchase(Order order, Product product, int totalPromotionQuantity) {
        recordPurchasedProduct(product, totalPromotionQuantity);
        calculateTotalPrice(order, totalPromotionQuantity);

        Promotion promotion = getPromotionStock(product).getPromotion();
        if (promotion != null) {
            int freeCount = promotion.getFreeCount();
            int sets = totalPromotionQuantity / (promotion.getBuyCount() + freeCount);
            int freeQuantity = sets * freeCount;
            recordFreeProduct(product, freeQuantity);
        }
    }

    private void recordPurchasedProduct(Product product, int quantity) {
        int currentQuantity = purchases.getOrDefault(product, 0);
        purchases.put(product, currentQuantity + quantity);
    }

    private void recordFreeProduct(Product product, int quantity) {
        int currentQuantity = free.getOrDefault(product, 0);
        free.put(product, currentQuantity + quantity);
    }

    private void recordRegularPriceProduct(Product product, int quantity) {
        int currentQuantity = regularPriceProducts.getOrDefault(product, 0);
        regularPriceProducts.put(product, currentQuantity + quantity);
    }

    private void calculateTotalPrice(Order order, int quantity) {
        Product product = findProduct(order.getProductName());
        this.price += product.getPrice() * quantity;
    }

    // 6. 할인 계산
    public int calculateMembershipDiscount() {
        int regularPriceTotal = calculateRegularPriceTotal();
        return membership.discount(regularPriceTotal);
    }

    private int calculateRegularPriceTotal() {
        return regularPriceProducts.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public int getPromotionDiscount() {
        return free.keySet().stream()
                .mapToInt(product -> product.getPrice() * free.get(product))
                .sum();
    }

    // 7. 주문 관리
    public void clearOrder() {
        purchases.clear();
        free.clear();
        regularPriceProducts.clear();
        price = 0;
    }

    // 8. Getter
    public Map<Product, Integer> getPurchases() {
        return new LinkedHashMap<>(purchases);
    }

    public Map<Product, Integer> getFreeProducts() {
        return new LinkedHashMap<>(free);
    }

    public int getPrice() {
        return price;
    }

    public Map<Product, Stock> getStocks() {
        return stocks;
    }

    public Map<Product, PromotionStock> getPromotionStocks() {
        return promotionStocks;
    }
}
