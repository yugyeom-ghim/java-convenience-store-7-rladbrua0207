package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.exception.InvalidQuantityException;
import store.exception.NotFoundException;

class StoreTest {

    Map<Product, Stock> stocks = new LinkedHashMap<>();
    Map<Product, PromotionStock> promotionStocks = new LinkedHashMap<>();

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
        Product cola = new Product("콜라", 1000);
        stocks.put(cola, new Stock(cola, 10));
        Store store = new Store(stocks, promotionStocks);
        Order order = new Order("콜라", 11);

        // when & then
        assertThatThrownBy(() -> store.validatePurchase(order))
                .isInstanceOf(InvalidQuantityException.class);
    }

    @Test
    void 정상_구매_재고_차감() throws IOException {
        // given
        Product cola = new Product("콜라", 1000);
        stocks.put(cola, new Stock(cola, 10));
        Store store = new Store(stocks, promotionStocks);
        Order order = new Order("콜라", 5);

        // when
        store.buyProduct(order);

        // then
        Assertions.assertThat(store.getStock(cola).getQuantity()).isEqualTo(5);
    }
} 
