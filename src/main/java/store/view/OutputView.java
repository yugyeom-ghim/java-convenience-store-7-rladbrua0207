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

    private static final String STORE_HEADER = "==============W 편의점================";
    private static final String PRODUCT_HEADER = "상품명\t\t수량\t금액";
    private static final String GIFT_HEADER = "=============증\t정===============";
    private static final String FOOTER = "====================================";
    private static final String NEW_LINE = "\n";
    private static final String EMPTY_STRING = "";
    private static final String OUT_OF_STOCK = "재고 없음";
    private static final String PURCHASE_FORMAT = "%s\t\t%d\t%,d%n";
    private static final String GIFT_FORMAT = "%s\t\t%d%n";
    private static final String TOTAL_AMOUNT_FORMAT = "총구매액\t\t\t%,d%n";
    private static final String PROMOTION_DISCOUNT_FORMAT = "행사할인\t\t\t-%,d%n";
    private static final String MEMBERSHIP_DISCOUNT_FORMAT = "멤버십할인\t\t\t-%,d%n";
    private static final String FINAL_AMOUNT_FORMAT = "내실돈\t\t\t%,d%n";
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

    public static void printReceipt(Store store, int membershipDiscount) {
        System.out.println(STORE_HEADER);
        printPurchases(store.getPurchases());

        if (!store.getFreeProducts().isEmpty()) {
            System.out.println(GIFT_HEADER);
            printGifts(store.getFreeProducts());
        }

        System.out.println(FOOTER);
        printAmounts(store.getPrice(), store.getPromotionDiscount(), membershipDiscount);
    }

    private static void printPurchases(Map<Product, Integer> purchases) {
        System.out.println(PRODUCT_HEADER);
        purchases.forEach((product, quantity) ->
                System.out.printf(PURCHASE_FORMAT,
                        product.getName(),
                        quantity,
                        product.getPrice() * quantity)
        );
    }

    private static void printGifts(Map<Product, Integer> gifts) {
        gifts.forEach((product, quantity) ->
                System.out.printf(GIFT_FORMAT, product.getName(), quantity)
        );
    }

    private static void printAmounts(int totalAmount, int promotionDiscount, int membershipDiscount) {
        System.out.printf(TOTAL_AMOUNT_FORMAT, totalAmount);
        if (promotionDiscount > 0) {
            System.out.printf(PROMOTION_DISCOUNT_FORMAT, promotionDiscount);
        }
        System.out.printf(MEMBERSHIP_DISCOUNT_FORMAT, membershipDiscount);
        System.out.printf(FINAL_AMOUNT_FORMAT, totalAmount - promotionDiscount - membershipDiscount);
    }

    public static void printErrorMessage(String errorMessage) {
        System.out.println(errorMessage);
    }
}
