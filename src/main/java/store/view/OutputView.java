package store.view;

import java.util.Map;
import store.model.Product;
import store.model.Promotion;
import store.model.PromotionStock;
import store.model.Stock;
import store.model.Store;

public class OutputView {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String CURRENT_PRODUCTS_MESSAGE = "현재 보유하고 있는 상품입니다.\n";

    private static final String NEW_LINE = "\n";
    private static final String EMPTY_STRING = "";
    private static final String OUT_OF_STOCK = "재고 없음";
    private static final String PRODUCT_LINE_FORMAT = "- %s %,d원 %s %s%n";
    private static final String QUANTITY_FORMAT = "%d개";

    public static void printStartMessage() {
        System.out.println(WELCOME_MESSAGE);
        System.out.println(CURRENT_PRODUCTS_MESSAGE);
    }

    public static void printProducts(Map<Product, Stock> stocks, Map<Product, PromotionStock> promotionStocks) {
        stocks.keySet().forEach(product -> {
            PromotionStock promotionStock = promotionStocks.get(product);
            if (promotionStock != null) {
                printProductLine(product, promotionStock, promotionStock.getPromotion());
            }
            printProductLine(product, stocks.get(product), null);
        });
        System.out.println(NEW_LINE);
    }

    private static void printProductLine(Product product, Stock stock, Promotion promotion) {
        System.out.printf(PRODUCT_LINE_FORMAT,
                product.getName(),
                product.getPrice(),
                getQuantity(stock),
                getPromotionName(promotion));
    }

    private static String getQuantity(Stock stock) {
        if (stock.getQuantity() > 0) {
            return String.format(QUANTITY_FORMAT, stock.getQuantity());
        }
        return OUT_OF_STOCK;
    }

    private static String getPromotionName(Promotion promotion) {
        if (promotion != null) {
            return promotion.getName();
        }
        return EMPTY_STRING;
    }
}
