package store.order;

import java.util.List;
import store.model.Order;
import store.model.Product;
import store.global.OrderUtil;
import store.service.ProductService;
import store.ui.InputView;
import store.ui.OutputView;

public class OrderSystem {
    private static final ProductService productService = new ProductService();

    public void order() {
        List<Product> products = displayProducts();
        Order order = getUserOrder(products);
        int eventDiscount = applyEventDiscount(order);
        int membershipDiscount = handleMembershipDiscount(order);
        OutputView.displayReceipt(order, eventDiscount, membershipDiscount);
    }

    private List<Product> displayProducts() {
        OutputView.welcomeMessage();
        List<Product> products = productService.loadProducts("src/main/resources/products.md");
        OutputView.displayProducts(products);
        return products;
    }

    private Order getUserOrder(List<Product> products) {
        try {
            return OrderUtil.convertInputToOrder(InputView.getProductSelection(), products);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return getUserOrder(products);
        }
    }

    private int applyEventDiscount(Order order) {
        int eventDiscount = order.calculatePromotionDiscount();
        order.applyDiscount(eventDiscount);
        return eventDiscount;
    }

    private int handleMembershipDiscount(Order order) {
        if (InputView.getMembershipDiscountConfirmation()) {
            order.applyMembershipDiscount();
            return order.getMembershipDiscount();
        }
        return 0;
    }
}