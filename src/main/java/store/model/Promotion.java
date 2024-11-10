package store.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Promotion {

    private static final int ONE = 1;

    private String name;
    private int buyCount;
    private int freeCount;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buyCount, int freeCount, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyCount = buyCount;
        this.freeCount = freeCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Boolean isValidDate(LocalDateTime now) {
        return (startDate.atStartOfDay().isBefore(now) || startDate.atStartOfDay().isEqual(now))
                && endDate.plusDays(ONE).atStartOfDay().isAfter(now);
    }

    public int getBuyCount() {
        return buyCount;
    }

    public int getFreeCount() {
        return freeCount;
    }
}
