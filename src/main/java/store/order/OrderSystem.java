package store.order;

import java.util.List;
import store.model.Product;
import store.service.ProductService;
import store.ui.OutputView;

public class OrderSystem {
    private final ProductService productService = new ProductService();
    public void order(){

        OutputView.welcomeMessage();
        String productFilePath = "src/main/resources/products.md";
        String promotionFilepath = "src/main/resources/promotions.md";
        List<Product> products = productService.loadProducts(productFilePath,promotionFilepath);

        OutputView.showProducts(products);
    }
}
