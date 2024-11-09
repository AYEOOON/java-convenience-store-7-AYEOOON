package store.model;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private final Map<Product, Integer> orderItems;
    private int totalAmount;
    private int originalTotalAmount;
    private int membershipDiscount;

    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    public Order(Map<Product, Integer> orderResult) {
        this.orderItems = new HashMap<>(orderResult);
        calculateOriginalTotalAmount();
        this.totalAmount = originalTotalAmount;
    }

    public void calculateOriginalTotalAmount() {
        originalTotalAmount = orderItems.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public void applyDiscount(int discount) {
        totalAmount = originalTotalAmount - discount;
    }

    public void applyMembershipDiscount() {
        int nonPromotionTotal = calculateNonPromotionTotal();
        membershipDiscount = (int) (nonPromotionTotal * MEMBERSHIP_DISCOUNT_RATE);

        if (membershipDiscount > MAX_MEMBERSHIP_DISCOUNT) {
            membershipDiscount = MAX_MEMBERSHIP_DISCOUNT;
        }
        totalAmount -= membershipDiscount;
    }

    private int calculateNonPromotionTotal() {
        return orderItems.entrySet().stream()
                .mapToInt(entry -> {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();

                    if (product.getActivePromotion() == null) {
                        return product.getPrice() * quantity;
                    }
                    return 0;
                })
                .sum();
    }

    public int calculatePromotionDiscount() {
        return orderItems.entrySet().stream()
                .mapToInt(entry -> {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    Promotion promotion = product.getActivePromotion();

                    if (promotion != null) {
                        return promotion.calculateDiscount(product, quantity);
                    }
                    return 0;
                })
                .sum();
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getOriginalTotalAmount() {
        return originalTotalAmount;
    }

    public Map<Product, Integer> getOrderItems() {
        return orderItems;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}