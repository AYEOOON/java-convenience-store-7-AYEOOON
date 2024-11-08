package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import store.model.Product;
import store.model.Promotion;

public class ProductLoader {
    private final List<Promotion> promotions;

    public ProductLoader(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<String> loadProductData(String filePath) throws IOException{
        return Files.readAllLines(Paths.get(filePath));
    }

    public List<Product> parseProducts(List<String> lines){
        List<Product> products = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++){
            String line = lines.get(i);

            String[] parts = line.split(",");
            String name = parts[0].trim();
            int price = Integer.parseInt(parts[1]);
            int quantity = Integer.parseInt(parts[2].trim());
            String promotionName = parts[3].trim();
            Promotion promotion = findPromotionByName(promotionName);
            products.add(new Product(name, price, quantity, promotion));
        }
        return products;
    }

    private Promotion findPromotionByName(String promotionName) {
        if (promotionName.equals("null")) {
            return null;
        }
        return promotions.stream()
                .filter(p -> p.getName().equalsIgnoreCase(promotionName))
                .findFirst()
                .orElse(null);
    }
}