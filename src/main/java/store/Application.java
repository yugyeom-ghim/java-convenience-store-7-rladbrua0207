package store;

import static camp.nextstep.edu.missionutils.DateTimes.now;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.graalvm.collections.Pair;
import store.exception.InvalidQuantityException;
import store.model.Order;
import store.model.ProductInitializer;
import store.model.PromotionInitializer;
import store.model.Store;
import store.view.InputView;
import store.view.OutputView;

public class Application {

    private static final String ERROR_INITIALIZE_MESSAGE = "[ERROR] 파일 초기화 중에 오류가 발생했습니다.";

    public static void main(String[] args)  {
        try {
            PromotionInitializer promotionInitializer = new PromotionInitializer();
            ProductInitializer productInitializer = new ProductInitializer(promotionInitializer.getPromotionMap());
            Store store = new Store(productInitializer.getStocks(), productInitializer.getPromotionStocks());
            startShopping(store);
        } catch (IOException e) {
            OutputView.printErrorMessage(ERROR_INITIALIZE_MESSAGE);
        }
    }

    private static void startShopping(Store store) {
        while (true) {
            try {
                processPurchase(store);
                if (!InputView.inputWantToContinuePurchase()) {
                    break;
                }
            }  catch (IllegalArgumentException | InvalidQuantityException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private static void processPurchase(Store store) {
        store.clearOrder();

        OutputView.printStartMessage();
        OutputView.printProducts(store.getStocks(), store.getPromotionStocks());

        List<Pair<String, Integer>> orderPairs = getOrderPairs();
        orderPairs.forEach(orderPair -> buyOneProduct(store, orderPair.getLeft(), orderPair.getRight()));

        int membershipDiscount = getMembershipDiscount(store);
        OutputView.printReceipt(store, membershipDiscount);
    }

    private static List<Pair<String, Integer>> getOrderPairs() {
        try {
            return InputView.inputPurchaseProductsAndQuantities();
        } catch (IllegalArgumentException e) {
            OutputView.printErrorMessage(e.getMessage());
            return getOrderPairs();
        }
    }

    private static int getMembershipDiscount(Store store) {
        int membershipDiscount = -0;
        try {
            if (InputView.inputApplyMembershipIntent()) {
                membershipDiscount = store.calculateMembershipDiscount();
            }
        } catch (IllegalArgumentException e) {
            OutputView.printErrorMessage(e.getMessage());
            return getMembershipDiscount(store);
        }
        return membershipDiscount;
    }

    private static void buyOneProduct(Store store, String productName, int quantity) {
        Order order = new Order(productName, quantity);
        LocalDateTime orderTime = now();

        store.validatePurchase(order);

        order = addPromotionItemsIfPossible(store, order, orderTime);
        order = adjustRegularPriceItems(store, order, orderTime);

        store.purchaseProduct(order, orderTime);
    }

    private static Order addPromotionItemsIfPossible(Store store, Order order, LocalDateTime orderTime) {
        int freeQuantity = store.calculateGetMoreFreeQuantity(order, orderTime);
        if (freeQuantity <= 0) {
            return order;
        }

        if (InputView.inputWantToReceiveOneMoreFree(order.getProductName())) {
            return createNewOrder(order, order.getQuantity() + freeQuantity);
        }
        return order;
    }

    private static Order adjustRegularPriceItems(Store store, Order order, LocalDateTime orderTime) {
        int regularPriceQuantity = store.calculateAdditionalRegularPriceQuantity(order, orderTime);
        if (regularPriceQuantity <= 0) {
            return order;
        }

        if (!InputView.inputWantToPurchaseNotApplyPromotion(order.getProductName(), regularPriceQuantity)) {
            return createNewOrder(order, order.getQuantity() - regularPriceQuantity);
        }
        return order;
    }

    private static Order createNewOrder(Order originalOrder, int newQuantity) {
        return new Order(originalOrder.getProductName(), newQuantity);
    }
}
