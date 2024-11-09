package store.ui;

import java.text.DecimalFormat;
import java.util.List;
import store.model.Order;
import store.model.Product;
import store.model.Promotion;

public class OutputView {

    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    public static void welcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.\n"
                + "현재 보유하고 있는 상품입니다.\n");
    }

    public static void displayProducts(List<Product> products) {
        products.forEach(OutputView::displayProductStock);
    }

    private static void displayProductStock(Product product) {
        if (hasPromotionStock(product)) {
            displayPromotionStock(product);
        }
        displayGeneralStock(product);
    }

    private static void displayGeneralStock(Product product) {
        if (hasGeneralStock(product)) {
            printProductWithStock(product, product.getGeneralStock(), "");
            return;
        }
        printOutOfStock(product);
    }

    private static void displayPromotionStock(Product product) {
        if (!hasPromotionStock(product)) return;
        printProductWithStock(product, product.getPromotionStock(), getPromotionName(product));
    }

    private static boolean hasPromotionStock(Product product) {
        return product.getPromotionStock() > 0;
    }

    private static boolean hasGeneralStock(Product product) {
        return product.getGeneralStock() > 0;
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
        if (product.getActivePromotion() == null) return "";
        String promotionName = product.getActivePromotion().getName();
        if (promotionName.isEmpty()) return "";
        return " " + promotionName;
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
        order.getOrderItems().forEach((product, quantity) -> {
            int freeItems = calculateFreeItems(product, quantity);
            if (freeItems > 0) {
                System.out.printf("%s\t\t%d\n", product.getName(), freeItems);
            }
        });
    }

    private static int calculateFreeItems(Product product, int quantity) {
        Promotion promotion = product.getActivePromotion();
        if (promotion == null) return 0;
        return (quantity / promotion.getBuy()) * promotion.getGet();
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