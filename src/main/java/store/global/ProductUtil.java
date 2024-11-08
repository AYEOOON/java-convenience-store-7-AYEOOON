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
            String line = lines.get(i);
            String[] parts = line.split(",");
            products.add(createProduct(parts));
        }
        return products;
    }

    private Product createProduct(String[] parts) {
        String name = parts[0].trim();
        int price = Integer.parseInt(parts[1].trim());
        int quantity = Integer.parseInt(parts[2].trim());
        String promotionName = parts[3].trim();

        Promotion promotion = findPromotionByName(promotionName);
        int generalStock = calculateGeneralStock(promotion, quantity);
        int promotionStock = calculatePromotionStock(promotion, quantity);

        return new Product(name, price, generalStock, promotionStock, promotion);
    }

    private int calculateGeneralStock(Promotion promotion, int quantity) {
        if (promotion == null) {
            return quantity;
        }
        return 0;
    }

    private int calculatePromotionStock(Promotion promotion, int quantity) {
        if (promotion != null) {
            return quantity;
        }
        return 0;
    }

    private Promotion findPromotionByName(String promotionName) {
        if (promotionName.equals("null") || promotionName.isEmpty()) {
            return null;
        }
        return promotions.stream()
                .filter(p -> p.getName().equalsIgnoreCase(promotionName))
                .findFirst()
                .orElse(null);
    }
}