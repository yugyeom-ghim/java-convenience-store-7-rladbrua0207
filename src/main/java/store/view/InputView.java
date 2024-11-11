package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import org.graalvm.collections.Pair;

public class InputView {

    private static final String ERROR_FORMAT_MESSAGE = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String ERROR_INVALID_INPUT_MESSAGE = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";
    private static final String INPUT_PURCHASE_PRODUCTS_AND_QUANTITIES_MESSAGE =
            "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String INPUT_RECEIVE_INTENT_ONE_MORE_FREE_MESSAGE =
            "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n";
    private static final String INPUT_PURCHASE_INTENT_NOT_APPLY_PROMOTION_MESSAGE =
            "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n";
    private static final String INPUT_MEMBERSHIP_APPLY_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String INPUT_CONTINUE_PURCHASE_MESSAGE = 
            "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    private static final String DELIMITER_COMMA = ",";
    private static final String DELIMITER_HYPHEN = "-";
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final char OPEN_BRACKET = '[';
    private static final char CLOSE_BRACKET = ']';
    private static final int PRODUCT_INDEX = 0;
    private static final int QUANTITY_INDEX = 1;

    public static List<Pair<String, Integer>> inputPurchaseProductsAndQuantities() {
        System.out.println(INPUT_PURCHASE_PRODUCTS_AND_QUANTITIES_MESSAGE);
        String input = Console.readLine();
        validatePurchaseFormat(input);
        return parsePurchaseInput(input);
    }

    public static boolean inputWantToReceiveOneMoreFree(String productName) {
        System.out.printf(INPUT_RECEIVE_INTENT_ONE_MORE_FREE_MESSAGE, productName);
        String input = Console.readLine();
        validateYerOrNo(input);
        return input.equals(YES);
    }

    public static boolean inputWantToPurchaseNotApplyPromotion(String productName, int quantity) {
        System.out.printf((INPUT_PURCHASE_INTENT_NOT_APPLY_PROMOTION_MESSAGE), productName, quantity);
        String input = Console.readLine();
        validateYerOrNo(input);
        return input.equals(YES);
    }

    public static boolean inputApplyMembershipIntent() {
        System.out.println(INPUT_MEMBERSHIP_APPLY_MESSAGE);
        String input = Console.readLine();
        validateYerOrNo(input);
        return input.equals(YES);
    }

    public static boolean inputWantToContinuePurchase() {
        System.out.println(INPUT_CONTINUE_PURCHASE_MESSAGE);
        String input = Console.readLine();
        validateYerOrNo(input);
        return input.equals(YES);
    }

    private static void validateYerOrNo(String input) {
        if (!(input.equals(YES) || input.equals(NO))) {
            throw new IllegalArgumentException(ERROR_INVALID_INPUT_MESSAGE);
        }
    }

    private static void validatePurchaseFormat(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(ERROR_FORMAT_MESSAGE);
        }

        String[] items = input.split(DELIMITER_COMMA);
        for (String item : items) {
            validatePurchaseItemBrackets(item.trim());
        }
    }

    private static void validatePurchaseItemBrackets(String item) {
        if (item.length() < 2 || item.charAt(0) != OPEN_BRACKET || item.charAt(item.length() - 1) != CLOSE_BRACKET) {
            throw new IllegalArgumentException(ERROR_FORMAT_MESSAGE);
        }
    }

    private static List<Pair<String, Integer>> parsePurchaseInput(String input) {
        return Arrays.stream(input.split(DELIMITER_COMMA))
                .map(String::trim)
                .map(item -> item.substring(1, item.length() - 1))
                .map(item -> {
                    List<String> productAndQuantity = Arrays.stream(item.split(DELIMITER_HYPHEN)).toList();
                    String product = productAndQuantity.get(PRODUCT_INDEX);
                    Integer quantity = Integer.parseInt(productAndQuantity.get(QUANTITY_INDEX));
                    return Pair.create(product, quantity);
                })
                .toList();
    }
}
