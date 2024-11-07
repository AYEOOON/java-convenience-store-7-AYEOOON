package store.model;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private final String promotion;
    private Promotion activePromotion;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;

    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPromotion() {
        return promotion;
    }

    public Promotion getActivePromotion() {
        return activePromotion;
    }

    public void setActivePromotion(Promotion activePromotion) {
        this.activePromotion = activePromotion;
    }
}
