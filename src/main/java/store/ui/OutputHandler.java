package store.ui;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import store.model.Order;
import store.model.Product;
import store.model.Promotion;
import store.global.MessageEnum;
import store.global.ReceiptEnum;

public class OutputHandler {

    private static final DecimalFormat formatter = new DecimalFormat("#,###");

    public static void welcomeMessage() {
        System.out.println(MessageEnum.WELCOME_MESSAGE.getMessage());
    }

    public static void displayProducts(List<Product> products) {
        products.forEach(OutputHandler::displayProductStock);
    }

    private static void displayProductStock(Product product) {
        displayPromotionStock(product);
        displayGeneralStock(product);
    }

    private static void displayGeneralStock(Product product) {
        if (product.getGeneralStock() > 0) {
            printProductWithStock(product, product.getGeneralStock(), "");
        }
        if (product.getGeneralStock() <= 0){
            printProductOutOfStock(product);
        }
    }

    private static void displayPromotionStock(Product product) {
        Promotion promotion = product.getActivePromotion();
        if (product.getPromotionStock() > 0) {
            printProductWithStock(product, product.getPromotionStock(), getPromotionName(product));
        }
        if (promotion != null && product.getPromotionStock() <= 0) {
            printPromotionOutOfStock(product);
        }
    }
    private static void printPromotionOutOfStock(Product product) {
        String promotionInfo = getPromotionName(product);
        String formattedPrice = formatter.format(product.getPrice()) + "원";
        System.out.println("- " + product.getName() + " " + formattedPrice + " 재고 없음" + promotionInfo);
    }

    private static void printProductWithStock(Product product, int stock, String promotionInfo) {
        String formattedPrice = formatter.format(product.getPrice()) + "원";
        System.out.println("- " + product.getName() + " " + formattedPrice + " " + stock + "개" + promotionInfo);
    }

    private static void printProductOutOfStock(Product product) {
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
        System.out.println(ReceiptEnum.HEADER.getMessage());
        System.out.println(ReceiptEnum.ITEM_HEADER.getMessage());
    }

    private static void printOrderItems(Order order) {
        order.getOrderItems().forEach((product, quantity) -> {
            int total = quantity * product.getPrice();
            System.out.printf("%s\t\t%d\t%,d원\n", product.getName(), quantity, total);
        });
    }

    private static void printFreeItems(Order order) {
        System.out.println(ReceiptEnum.FREE_ITEM_HEADER.getMessage());
        Map<Product, Integer> freeItems = order.getFreeItems();
        freeItems.forEach((product, quantity) -> {
            if (quantity > 0) {
                System.out.printf("%s\t\t%d\n", product.getName(), quantity);
            }
        });
    }

    private static void printTotalSummary(Order order, int eventDiscount, int membershipDiscount) {
        System.out.println(ReceiptEnum.TOTAL_SUMMARY.getMessage());
        int totalQuantity = order.getTotalQuantity();
        System.out.printf(ReceiptEnum.TOTAL_AMOUNT.format(totalQuantity, order.getOriginalTotalAmount()));
        System.out.printf(ReceiptEnum.EVENT_DISCOUNT.format(eventDiscount));
        System.out.printf(ReceiptEnum.MEMBERSHIP_DISCOUNT.format(membershipDiscount));
        System.out.printf(ReceiptEnum.FINAL_AMOUNT.format(order.getTotalAmount()));
    }

}