package store.service;

import java.io.IOException;
import java.util.List;
import store.model.Product;
import store.model.Promotion;
import store.util.ProductLoader;

public class ProductService {
    private final ProductLoader productLoader = new ProductLoader();
    private final PromotionService promotionService = new PromotionService();

    public List<Product> loadProducts(String productFilePath, String promotionFilePath){
        try{
            List<String> productLines = productLoader.loadProductData(productFilePath);
            List<Product> products = productLoader.parseProducts(productLines);

            List<Promotion> allPromotions = promotionService.loadPromotion(promotionFilePath);
            List<Promotion> activePromotions = promotionService.getActivePromotions(allPromotions);

            applyPromotionsToProducts(products,activePromotions);
            return products;
        }catch (IOException e){
            System.out.println("[ERROR] 파일을 불러오는 중 오류가 발생했습니다.");
            return null;
        }
    }

    private void applyPromotionsToProducts(List<Product> products, List<Promotion> activePromotions) {
        products.forEach(product ->
                activePromotions.stream()
                        .filter(promotion -> product.getPromotion().equals(promotion.getName()))
                        .findFirst()
                        .ifPresent(product::setActivePromotion));
    }
}
