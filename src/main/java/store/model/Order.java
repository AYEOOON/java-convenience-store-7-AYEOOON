package store.model;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private final Map<Product, Integer> orderItems;
    private int totalAmount;

    public Order(Map<Product, Integer> orderResult) {
        this.orderItems = new HashMap<>(orderResult);
        calculateTotalAmount();
    }

    public void calculateTotalAmount(){
        totalAmount = orderItems.entrySet().stream()
                .mapToInt(entry -> calculateItemTotal(entry.getKey(), entry.getValue()))
                .sum();
    }

    private int calculateItemTotal(Product product, int quantity) {
        if (product.getActivePromotion() == null) return quantity * product.getPrice();

        Promotion promotion = product.getActivePromotion();
        if (isInvalidPromotion(promotion)) return quantity * product.getPrice();

        int freeItems = calculateFreeItems(promotion, quantity);
        int payableQuantity = quantity - freeItems;
        return payableQuantity * product.getPrice();
    }

    private boolean isInvalidPromotion(Promotion promotion) {
        return promotion.getBuy() <= 0;
    }

    private int calculateFreeItems(Promotion promotion, int quantity) {
        int sets = quantity / promotion.getBuy();
        return sets * promotion.getGet();
    }

    public Map<Product, Integer> getOrderItems() {
        return orderItems;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
