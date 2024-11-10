package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import store.model.Product;
import store.model.Promotion;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    private ProductService productService;
    private final String productFilePath = "src/main/resources/products.md";
    @BeforeEach
    public void 초기화() {
        productService = new ProductService();
    }

    @Test
    public void 상품_목록_로드_테스트() {
        List<Product> products = productService.loadProducts(productFilePath);

        assertNotNull(products, "상품 목록이 null이어서는 안 됩니다.");
        assertFalse(products.isEmpty(), "상품 목록이 비어 있어서는 안 됩니다.");
    }

    @Test
    public void 활성_프로모션이_정확히_적용되는지_테스트() {
        List<Product> products = productService.loadProducts(productFilePath);
        products.forEach(product -> {
            Promotion promotion = product.getActivePromotion();
            if (promotion != null) {
                boolean isActive = promotion.isActive();
                assertEquals(isActive, promotion.isActive(), "프로모션의 활성 상태가 일치해야 합니다.");
            }
        });
    }
}
