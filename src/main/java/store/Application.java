package store;

import store.order.OrderSystem;

public class Application {
    public static void main(String[] args) {
        OrderSystem orderSystem = new OrderSystem();
        orderSystem.order();
    }
}
