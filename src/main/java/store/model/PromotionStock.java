package store.model;

public class PromotionStock extends Stock {

    private final Promotion promotion;

    public PromotionStock(Product product, int quantity, Promotion promotion) {
        super(product, quantity);
        this.promotion = promotion;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
