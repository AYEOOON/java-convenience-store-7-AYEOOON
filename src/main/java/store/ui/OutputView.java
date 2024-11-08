package store.ui;

import java.text.DecimalFormat;
import java.util.List;
import store.model.Product;
import store.model.Promotion;

public class OutputView {
    public static void welcomeMessage(){
        System.out.println("안녕하세요. W편의점입니다.\n"
                + "현재 보유하고 있는 상품입니다.\n");
    }
    public static void displayProducts(List<Product> products){
        DecimalFormat formatter = new DecimalFormat("#,###");
        for (Product product : products){
            System.out.println("- "+product.getName() + " "
                    + formatter.format(product.getPrice())+"원 "
                    + product.getQuantity()+ "개 "
                    + getPromotionName(product));
        }
    }

    private static String getPromotionName(Product product) {
        Promotion promotion = product.getActivePromotion();
        if (promotion == null || promotion.getName().isEmpty()) {
            return "";
        }
        return promotion.getName();
    }
}
