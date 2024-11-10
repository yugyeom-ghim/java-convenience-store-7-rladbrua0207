package store.model;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.exception.NotFoundException;

class OrderTest {

    @Test
    public void 수량이_0_이하일_경우_예외() throws Exception {
        // given
        String productName = "콜라";
        int quantity = 6;

        // when & then
        Assertions.assertThatThrownBy(() -> new Order(productName, quantity))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    public void 상품명이_공백일_경우_예외() throws Exception {
        // given
        String productName = " ";
        int quantity = 6;

        // when & then
        Assertions.assertThatThrownBy(() -> new Order(productName, quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
