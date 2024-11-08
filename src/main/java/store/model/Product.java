package store.model;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private Promotion activePromotion;

    public Product(String name, int price, int quantity, Promotion activePromotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.activePromotion = activePromotion;
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

    public Promotion getActivePromotion() {
        return activePromotion;
    }

    public void setActivePromotion(Promotion activePromotion) {
        this.activePromotion = activePromotion;
    }
}