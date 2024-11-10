package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    private Product cola;
    private Product energyBar;
    private Product orangeJuice;

    @BeforeEach
    void setUp() {
        cola = new Product("콜라", 1000, 10, 5, null);
        energyBar = new Product("에너지바", 2000, 10, 5, null);
        orangeJuice = new Product("오렌지주스", 1800, 10, 5, null);
    }

    @Test
    void testCalculateOriginalTotalAmount() {
        Map<Product, Integer> orderItems = new HashMap<>();
        orderItems.put(cola, 3);
        orderItems.put(energyBar, 2);

        Order order = new Order(orderItems, new HashMap<>());
        assertThat(order.getOriginalTotalAmount()).isEqualTo(3 * 1000 + 2 * 2000);
    }

    @Test
    void testApplyPromotionDiscount() {
        Map<Product, Integer> orderItems = new HashMap<>();
        Map<Product, Integer> freeItems = new HashMap<>();

        orderItems.put(cola, 5);
        freeItems.put(cola, 2); // 프로모션으로 2개 무료 제공

        Order order = new Order(orderItems, freeItems);
        order.applyPromotionDiscount();

        // 원래 금액: 5 * 1000원, 프로모션 할인: 2 * 1000원
        assertThat(order.getTotalAmount()).isEqualTo((5 * 1000) - (2 * 1000));
    }

    @Test
    void testCalculatePromotionDiscount() {
        Map<Product, Integer> orderItems = new HashMap<>();
        Map<Product, Integer> freeItems = new HashMap<>();

        orderItems.put(orangeJuice, 4);
        freeItems.put(orangeJuice, 1); // 1+1 프로모션으로 1개 무료 제공

        Order order = new Order(orderItems, freeItems);
        int promotionDiscount = order.calculatePromotionDiscount();

        // 프로모션 할인: 1 * 1800원
        assertThat(promotionDiscount).isEqualTo(1 * 1800);
    }

    @Test
    void testNoPromotionOrDiscount() {
        Map<Product, Integer> orderItems = new HashMap<>();
        orderItems.put(cola, 2);

        Order order = new Order(orderItems, new HashMap<>());
        assertThat(order.getTotalAmount()).isEqualTo(2 * 1000);
        assertThat(order.getMembershipDiscount()).isEqualTo(0);
    }
}