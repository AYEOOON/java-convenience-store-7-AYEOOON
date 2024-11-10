package store;


import store.order.OrderManager;

public class Application {
    public static void main(String[] args) {
        OrderManager orderManager = new OrderManager();
        orderManager.order();
    }
}
