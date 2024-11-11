package store.service;

import java.io.IOException;
import java.util.List;
import store.model.Product;
import store.model.Promotion;
import store.util.ProductUtil;
import store.util.PromotionUtil;

public class ProductService {
    private final ProductUtil productLoader;

    public ProductService() {
        PromotionUtil promotionLoader = new PromotionUtil();
        List<Promotion> allPromotions = promotionLoader.loadPromotions("src/main/resources/promotions.md");
        this.productLoader = new ProductUtil(allPromotions);
    }

    public List<Product> loadProducts(String productFilePath) {
        try {
            List<String> productLines = productLoader.loadProductData(productFilePath);
            return productLoader.parseProducts(productLines);
        } catch (IOException e) {
            System.out.println("[ERROR] 파일을 불러오는 중 오류가 발생했습니다.");
            return null;
        }
    }
}
