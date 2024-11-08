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
        OutputView.welcomeMessage();
        List<Product> products = productService.loadProducts("src/main/resources/products.md");
        OutputView.displayProducts(products);

        getUserOrder(products);
    }

    private Order getUserOrder(List<Product> products){
        try{
            return OrderUtil.convertInputToOrder(InputView.getProductSelection(),products);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return getUserOrder(products);
        }
    }
}
