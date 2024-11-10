package store.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PromotionTest {

    @ParameterizedTest
    @CsvSource(value = {"2024-11-10T00:00:00", "2024-11-12T23:59:59"})
    public void 프로모션_기간일_경우_TRUE(String localDateTime) {
        // given
        String promotionName = "프로모션";
        int buyCount = 2;
        int freeCount = 1;
        LocalDate startDate = LocalDate.of(2024, 11, 10);
        LocalDate endDate = LocalDate.of(2024, 11, 12);

        Promotion promotion = new Promotion(promotionName, buyCount, freeCount, startDate, endDate);
        // when & then
        Assertions.assertThat(promotion.isValidDate(LocalDateTime.parse(localDateTime))).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"2024-11-09T23:59:59", "2024-11-13T00:00:00"})
    public void 프로모션_기간이_아닐_경우_FALSE(String localDateTime) {
        // given
        String promotionName = "프로모션";
        int buyCount = 2;
        int freeCount = 1;
        LocalDate startDate = LocalDate.of(2024, 11, 10);
        LocalDate endDate = LocalDate.of(2024, 11, 12);

        Promotion promotion = new Promotion(promotionName, buyCount, freeCount, startDate, endDate);
        // when & then
        Assertions.assertThat(promotion.isValidDate(LocalDateTime.parse(localDateTime))).isFalse();
    }
}
