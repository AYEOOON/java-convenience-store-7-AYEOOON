package store.model;

import java.util.Objects;

public class Product {
    private final String name;
    private final int price;
    private int generalStock;
    private int promotionStock;
    private Promotion activePromotion;

    public Product(String name, int price, int generalStock, int promotionStock, Promotion activePromotion) {
        this.name = name;
        this.price = price;
        this.generalStock = generalStock;
        this.promotionStock = promotionStock;
        this.activePromotion = activePromotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getGeneralStock() {
        return generalStock;
    }

    public int getPromotionStock() {
        return promotionStock;
    }

    public Promotion getActivePromotion() {
        return activePromotion;
    }

    public void setActivePromotion(Promotion activePromotion) {
        this.activePromotion = activePromotion;
    }

    public boolean reduceStock(int quantity, boolean isPromotion) {
        if (isPromotion) {
            if (promotionStock >= quantity) {
                promotionStock -= quantity;
                return true;
            }
        }
        if (generalStock >= quantity) {
            generalStock -= quantity;
            return true;
        }
        return false;
    }

    public void increasePromotionStock(int quantity) {
        promotionStock += quantity;
    }

    public void increaseGeneralStock(int quantity) {
        generalStock += quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
