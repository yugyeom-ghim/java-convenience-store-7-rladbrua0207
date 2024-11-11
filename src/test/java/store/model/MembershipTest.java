package store.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MembershipTest {

    private static final int MAXIMUM_DISCOUNTED_PRICE = 8_000;

    @Test
    public void 멤버십_할인_30퍼센트() throws Exception {
        // given
        Membership membership = new Membership();

        // when
        int discount = membership.discount(1000);

        // then
        Assertions.assertThat(discount).isEqualTo(300);
    }

    @Test
    public void 멤버십_할인_최대_8000() throws Exception {
        // given
        Membership membership = new Membership();

        int maximumDiscountablePrice = (MAXIMUM_DISCOUNTED_PRICE * 100) / 30 + 1;
        // when
        int discount = membership.discount(maximumDiscountablePrice);

        // then
        Assertions.assertThat(discount).isEqualTo(8000);
    }
}
