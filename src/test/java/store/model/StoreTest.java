package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.exception.InvalidQuantityException;
import store.exception.NotFoundException;

class StoreTest {

    private Map<Product, Stock> stocks = new LinkedHashMap<>();
    private Map<Product, PromotionStock> promotionStocks = new LinkedHashMap<>();

    private Promotion promotion;
    private String productName;

    @BeforeEach
    public void init() {
        productName = "콜라";

        String promotionName = "프로모션";
        int buyCount = 2;
        int freeCount = 1;
        LocalDate startDate = LocalDate.of(2024, 11, 10);
        LocalDate endDate = LocalDate.of(2024, 11, 12);
        promotion = new Promotion(promotionName, buyCount, freeCount, startDate, endDate);
    }

    @Test
    void 존재하지_않는_상품_구매_예외() throws IOException {
        // given
        Store store = new Store(stocks, promotionStocks);
        Order order = new Order("존재하지않는상품", 1);

        // when & then
        assertThatThrownBy(() -> store.validatePurchase(order))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 재고_초과_구매_예외() throws IOException {
        // given
        Product product = new Product(productName, 1000);
        stocks.put(product, new Stock(product, 10));
        Store store = new Store(stocks, promotionStocks);
        Order order = new Order(productName, 11);

        // when & then
        assertThatThrownBy(() -> store.validatePurchase(order))
                .isInstanceOf(InvalidQuantityException.class);
    }

    @Test
    void 정상_구매_재고_차감() throws IOException {
        // given
        Product product = new Product(productName, 1000);
        stocks.put(product, new Stock(product, 10));
        Store store = new Store(stocks, promotionStocks);
        Order order = new Order(productName, 5);
        LocalDateTime orderTime = null;

        // when
        store.purchaseProduct(order, orderTime);

        // then
        Assertions.assertThat(store.getStock(product).getQuantity()).isEqualTo(5);
    }


    @Test
    void 프로모션_구매_재고_차감() throws IOException {
        // given
        Product product = new Product(productName, 1000);

        PromotionStock promotionStock = new PromotionStock(product, 10, promotion);
        promotionStocks.put(product, promotionStock);

        stocks.put(product, new Stock(product, 10));

        Store store = new Store(stocks, promotionStocks);
        Order order = new Order(productName, 6);
        LocalDateTime orderTime = LocalDate.of(2024, 11, 11).atStartOfDay();

        // when
        store.purchaseProduct(order, orderTime);

        // then
        Assertions.assertThat(store.getPromotionStock(product).getQuantity()).isEqualTo(4);
    }

    @Test
    void 프로모션_및_일반_재고_차감() throws IOException {
        // given
        Product product = new Product(productName, 1000);

        stocks.put(product, new Stock(product, 10));

        PromotionStock promotionStock = new PromotionStock(product, 10, promotion);
        promotionStocks.put(product, promotionStock);

        Store store = new Store(stocks, promotionStocks);
        Order order = new Order(productName, 14);
        LocalDateTime orderTime = LocalDate.of(2024, 11, 11).atStartOfDay();

        // when
        store.purchaseProduct(order, orderTime);

        // then
        Assertions.assertThat(store.getPromotionStock(product).getQuantity()).isEqualTo(0);
        Assertions.assertThat(store.getStock(product).getQuantity()).isEqualTo(6);
    }

    @Test
    void 프로모션_추가적인_무료_개수_1개() {
        // given
        Product product = new Product(productName, 1000);

        PromotionStock promotionStock = new PromotionStock(product, 6, promotion);
        promotionStocks.put(product, promotionStock);

        Store store = new Store(stocks, promotionStocks);
        Order order = new Order(productName, 5);
        LocalDateTime orderTime = LocalDate.of(2024, 11, 11).atStartOfDay();

        // when
        int freeQuantity = store.calculateGetMoreFreeQuantity(order, orderTime);

        // then
        Assertions.assertThat(freeQuantity).isEqualTo(1);
    }

    @ParameterizedTest
    @CsvSource(value = {"3", "4", "6"})
    void 프로모션_추가적인_무료_개수_0개(int orderQuantity) {
        // given
        Product product = new Product(productName, 1000);

        PromotionStock promotionStock = new PromotionStock(product, 6, promotion);
        promotionStocks.put(product, promotionStock);

        Store store = new Store(stocks, promotionStocks);
        LocalDateTime orderTime = LocalDate.of(2024, 11, 11).atStartOfDay();

        // when
        Order order = new Order(productName, orderQuantity);
        int freeQuantity = store.calculateGetMoreFreeQuantity(order, orderTime);

        // then
        Assertions.assertThat(freeQuantity).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource(value = {"7, 10, 4", "7, 9, 3", "7, 8, 2", "7, 7, 1", "10, 7, 1"})
    void 프로모션_적용하여_계산할때_혜텍없이_결제하는_수(
            int promotionQuantity, int orderQuantity, int expectedRegularPriceQuantity
    ) {
        // given
        Product product = new Product(productName, 1000);

        PromotionStock promotionStock = new PromotionStock(product, promotionQuantity, promotion);
        promotionStocks.put(product, promotionStock);

        Stock stock = stocks.put(product, new Stock(product, 100));
        stocks.put(product, stock);

        Store store = new Store(stocks, promotionStocks);
        LocalDateTime orderTime = LocalDate.of(2024, 11, 11).atStartOfDay();

        // when
        Order order = new Order(productName, orderQuantity);
        int regularPriceQuantity = store.calculateAdditionalRegularPriceQuantity(order, orderTime);

        // then
        Assertions.assertThat(regularPriceQuantity).isEqualTo(expectedRegularPriceQuantity);
    }
}
