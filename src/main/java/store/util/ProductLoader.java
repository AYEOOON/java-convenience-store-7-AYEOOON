package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import store.model.Product;

public class ProductLoader {
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
            String promotion = "";
            if (!parts[3].equals("null")) promotion = parts[3].trim();

            products.add(new Product(name, price, quantity, promotion));
        }
        return products;
    }
}