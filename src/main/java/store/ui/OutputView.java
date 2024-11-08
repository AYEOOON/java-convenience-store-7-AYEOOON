package store.ui;

import java.text.DecimalFormat;
import java.util.List;
import store.model.Product;
import store.model.Promotion;

public class OutputView {
    public static void welcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
    }

    public static void displayProducts(List<Product> products) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        for (Product product : products) {
            String formattedPrice = formatter.format(product.getPrice()) + "원";
            String stockInfo = getStockInfo(product);
            String promotionInfo = getPromotionName(product);

            System.out.println("- " + product.getName() + " " + formattedPrice + " " + stockInfo + promotionInfo);
        }
    }

    private static String getStockInfo(Product product) {
        int generalStock = product.getGeneralStock();
        int promotionStock = product.getPromotionStock();

        if (generalStock == 0 && promotionStock == 0) {
            return "재고 없음";
        }
        int totalStock = generalStock + promotionStock;
        return totalStock + "개";
    }

    private static String getPromotionName(Product product) {
        Promotion promotion = product.getActivePromotion();
        if (promotion == null || promotion.getName().isEmpty()) {
            return "";
        }
        return " " + promotion.getName();
    }
}
