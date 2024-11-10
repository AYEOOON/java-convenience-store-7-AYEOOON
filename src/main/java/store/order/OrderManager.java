package store.order;

import java.util.List;
import store.model.Order;
import store.model.Product;
import store.global.OrderUtil;
import store.service.ProductService;
import store.ui.InputHandler;
import store.ui.OutputHandler;

public class OrderManager{
    private static final ProductService productService = new ProductService();

    public void order() {
        List<Product> products = displayProducts();
        while(true) {
            Order order = getUserOrder(products);
            int eventDiscount = applyEventDiscount(order);
            int membershipDiscount = handleMembershipDiscount(order);
            OutputHandler.displayReceipt(order, eventDiscount, membershipDiscount);
            if(!InputHandler.getAdditionalPurchaseConfirmation()) break;
            OutputHandler.welcomeMessage();
            OutputHandler.displayProducts(products);
        }
    }

    private List<Product> displayProducts() {
        OutputHandler.welcomeMessage();
        List<Product> products = productService.loadProducts("src/main/resources/products.md");
        OutputHandler.displayProducts(products);
        return products;
    }

    private Order getUserOrder(List<Product> products) {
        try {
            return OrderUtil.convertInputToOrder(InputHandler.getProductSelection(), products);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return getUserOrder(products);
        }
    }

    private int applyEventDiscount(Order order) {
        int eventDiscount = order.calculatePromotionDiscount();
        order.applyPromotionDiscount();
        return eventDiscount;
    }

    private int handleMembershipDiscount(Order order) {
        if (InputHandler.getMembershipDiscountConfirmation()) {
            order.applyMembershipDiscount();
            return order.getMembershipDiscount();
        }
        return 0;
    }
}