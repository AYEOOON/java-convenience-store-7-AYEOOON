package store.global;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import store.model.Product;
import store.model.Promotion;

public class ProductUtil {
    private final List<Promotion> promotions;

    public ProductUtil(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<String> loadProductData(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    public List<Product> parseProducts(List<String> lines) {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            processProductLine(products, lines.get(i));
        }
        return products;
    }

    private void processProductLine(List<Product> products, String line) {
        String[] parts = line.split(",");
        String name = parts[0].trim();
        int price = Integer.parseInt(parts[1].trim());
        int quantity = Integer.parseInt(parts[2].trim());
        Promotion promotion = findPromotionByName(parts[3].trim());

        Product existingProduct = findProductByName(products, name);
        if (existingProduct == null) {
            addNewProduct(products, name, price, quantity, promotion);
            return;
        }
        updateExistingProduct(existingProduct, quantity, promotion);
    }

    private void addNewProduct(List<Product> products, String name, int price, int quantity, Promotion promotion) {
        int generalStock = calculateGeneralStock(promotion, quantity);
        int promotionStock = calculatePromotionStock(promotion, quantity);
        products.add(new Product(name, price, generalStock, promotionStock, promotion));
    }

    private void updateExistingProduct(Product existingProduct, int quantity, Promotion promotion) {
        if (promotion != null) {
            existingProduct.setActivePromotion(promotion);
            existingProduct.increasePromotionStock(quantity);
            return;
        }
        existingProduct.increaseGeneralStock(quantity);
    }

    private int calculateGeneralStock(Promotion promotion, int quantity) {
        if (promotion == null) return quantity;
        return 0;
    }

    private int calculatePromotionStock(Promotion promotion, int quantity) {
        if (promotion != null) return quantity;
        return 0;
    }

    private Promotion findPromotionByName(String promotionName) {
        if (promotionName.equals("null") || promotionName.isEmpty()) return null;
        return promotions.stream()
                .filter(p -> p.getName().equalsIgnoreCase(promotionName))
                .findFirst()
                .orElse(null);
    }

    private Product findProductByName(List<Product> products, String name) {
        return products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}