package store.ui;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import store.model.Order;
import store.model.Product;
import store.model.Promotion;

public class OutputHandler {

    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    public static void welcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");
    }

    public static void displayProducts(List<Product> products) {
        products.forEach(OutputHandler::displayProductStock);
    }

    private static void displayProductStock(Product product) {
        if (product.getPromotionStock() == 0 && product.getGeneralStock() == 0) {
            printOutOfStock(product);
        } else {
            displayPromotionStock(product);
            displayGeneralStock(product);
        }
    }

    private static void displayGeneralStock(Product product) {
        if (product.getGeneralStock() > 0) {
            printProductWithStock(product, product.getGeneralStock(), "");
        } else {
            printOutOfStock(product);
        }
    }

    private static void displayPromotionStock(Product product) {
        if (product.getPromotionStock() > 0) {
            printProductWithStock(product, product.getPromotionStock(), getPromotionName(product));
        } else if (product.getActivePromotion() != null) {
            String promotionInfo = getPromotionName(product);
            String formattedPrice = formatter.format(product.getPrice()) + "원";
            System.out.println("- " + product.getName() + " " + formattedPrice + " 재고 없음" + promotionInfo);
        }
    }

    private static void printProductWithStock(Product product, int stock, String promotionInfo) {
        String formattedPrice = formatter.format(product.getPrice()) + "원";
        System.out.println("- " + product.getName() + " " + formattedPrice + " " + stock + "개" + promotionInfo);
    }

    private static void printOutOfStock(Product product) {
        String formattedPrice = formatter.format(product.getPrice()) + "원";
        System.out.println("- " + product.getName() + " " + formattedPrice + " 재고 없음");
    }

    private static String getPromotionName(Product product) {
        Promotion promotion = product.getActivePromotion();
        if (promotion == null) return "";
        String promotionName = promotion.getName();
        return promotionName.isEmpty() ? "" : " " + promotionName;
    }

    public static void displayReceipt(Order order, int eventDiscount, int membershipDiscount) {
        printHeader();
        printOrderItems(order);
        printFreeItems(order);
        printTotalSummary(order, eventDiscount, membershipDiscount);
    }

    private static void printHeader() {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");
    }

    private static void printOrderItems(Order order) {
        order.getOrderItems().forEach((product, quantity) -> {
            int total = quantity * product.getPrice();
            System.out.printf("%s\t\t%d\t%,d원\n", product.getName(), quantity, total);
        });
    }

    private static void printFreeItems(Order order) {
        System.out.println("=============증\t정===============");
        Map<Product, Integer> freeItems = order.getFreeItems();
        freeItems.forEach((product, quantity) -> {
            if (quantity > 0) {
                System.out.printf("%s\t\t%d\n", product.getName(), quantity);
            }
        });
    }

    private static void printTotalSummary(Order order, int eventDiscount, int membershipDiscount) {
        System.out.println("====================================");
        System.out.printf("총구매액\t\t\t%,d원\n", order.getOriginalTotalAmount());
        System.out.printf("행사할인\t\t\t-%,d원\n", eventDiscount);
        System.out.printf("멤버십할인\t\t\t-%,d원\n", membershipDiscount);
        int finalAmount = order.getTotalAmount();
        System.out.printf("내실돈\t\t\t%,d원\n", finalAmount);
    }
}