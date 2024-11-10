package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import org.graalvm.collections.Pair;

public class InputView {

    private static final String INPUT_PURCHASE_PRODUCTS_AND_QUANTITIES_MESSAGE =
            "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String ERROR_FORMAT_MESSAGE = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

    private static final String DELIMITER_COMMA = ",";
    private static final char OPEN_BRACKET = '[';
    private static final char CLOSE_BRACKET = ']';
    private static final String DELIMITER_HYPHEN = "-";
    private static final int PRODUCT_INDEX = 0;
    private static final int QUANTITY_INDEX = 1;

    public static List<Pair<String, String>> inputPurchaseProductsAndQuantities() {
        System.out.println(INPUT_PURCHASE_PRODUCTS_AND_QUANTITIES_MESSAGE);
        String input = Console.readLine();
        validatePurchaseFormat(input);
        return parsePurchaseInput(input);
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

    private static List<Pair<String, String>> parsePurchaseInput(String input) {
        return Arrays.stream(input.split(DELIMITER_COMMA))
                .map(String::trim)
                .map(item -> item.substring(1, item.length() - 1))
                .map(item -> {
                    List<String> productAndQuantity = Arrays.stream(item.split(DELIMITER_HYPHEN)).toList();
                    String product = productAndQuantity.get(PRODUCT_INDEX);
                    String quantity = productAndQuantity.get(QUANTITY_INDEX);
                    return Pair.create(product, quantity);
                })
                .toList();
    }
}
