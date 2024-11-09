package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;

public class InputView {

    public static final String INPUT_PURCHASE_PRODUCTS_AND_QUANTITIES_MESSAGE =
            "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    private static final String DELIMITER_COMMA = ",";

    public static List<String> inputPurchaseProductsAndQuantities() {
        System.out.println(INPUT_PURCHASE_PRODUCTS_AND_QUANTITIES_MESSAGE);
        return Arrays.stream(Console.readLine().split(DELIMITER_COMMA)).toList();
    }
}
