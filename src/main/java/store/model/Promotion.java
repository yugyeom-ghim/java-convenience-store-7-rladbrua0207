package store.model;

import java.time.LocalDate;

public class Promotion {

    private String name;
    private int buyCount;
    private int getCount;
    private LocalDate startDate;
    private LocalDate endDate;

    public Promotion(String name, int buyCount, int getCount, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyCount = buyCount;
        this.getCount = getCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
