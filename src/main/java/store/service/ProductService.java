package store.service;

import java.io.IOException;
import java.util.List;
import store.model.Product;
import store.util.ProductLoader;

public class ProductService {
    private final ProductLoader loader = new ProductLoader();

    public List<Product> loadProducts(String filePath){
        try{
            List<String> lines = loader.loadProductData(filePath);
            return loader.parseProducts(lines);
        }catch (IOException e){
            System.out.println("[ERROR] 파일을 불러오는 중 오류가 발생했습니다.");
            return null;
        }
    }
}
