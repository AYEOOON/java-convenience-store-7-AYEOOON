package store.global;

import store.model.Order;
import store.model.Product;
import store.model.Promotion;
import store.ui.InputView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderUtil {
    private static final String COMMA_SEPARATOR = ",";
    private static final String HYPHEN = "-";
    private static final String INVALID_ORDER_FORMAT_ERROR = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String PRODUCT_NAME_ERROR = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String INVALID_INPUT_ERROR = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";
    private static final String OUT_OF_STOCK_ERROR = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    public static Order convertInputToOrder(String userInput, List<Product> availableProducts) {
        Map<Product, Integer> orderResult = new HashMap<>();
        Map<Product, Integer> freeItems = new HashMap<>();
        String[] orders = parseUserInput(userInput);

        for (String order : orders) {
            processOrderItem(order, availableProducts, orderResult, freeItems);
        }
        return new Order(orderResult, freeItems);
    }

    private static void processOrderItem(String order, List<Product> availableProducts, Map<Product, Integer> orderResult, Map<Product, Integer> freeItems) {
        String[] oneOrder = getOneOrder(order);
        String productName = oneOrder[0];
        int quantity = getQuantity(oneOrder[1]);

        Product product = findExistingProduct(productName, availableProducts);
        checkStock(product, quantity);
        handlePromotionOrder(product, quantity, orderResult, freeItems);
    }

    private static void handlePromotionOrder(Product product, int quantity, Map<Product, Integer> orderResult, Map<Product, Integer> freeItems) {
        Promotion promotion = product.getActivePromotion();
        if (promotion == null || product.getPromotionStock() >= quantity) {
            processFullPromotionOrder(product, quantity, orderResult, freeItems);
        } else {
            handlePartialPromotionOrder(product, quantity, orderResult, freeItems);
        }
    }

    private static void processFullPromotionOrder(Product product, int quantity, Map<Product, Integer> orderResult, Map<Product, Integer> freeItems) {
        Promotion promotion = product.getActivePromotion();
        if (promotion != null) {
            int sets = quantity / (promotion.getBuy() + promotion.getGet());
            int freeItemsCount = sets * promotion.getGet();
            addToFreeItems(freeItems, product, freeItemsCount);
        }
        reducePromotionStock(product, quantity);
        addToPurchasedItems(orderResult, product, quantity);
    }

    private static void handlePartialPromotionOrder(Product product, int quantity, Map<Product, Integer> orderResult, Map<Product, Integer> freeItems) {
        Promotion promotion = product.getActivePromotion();
        int promotionStock = product.getPromotionStock();
        int promotionSetSize = promotion.getBuy() + promotion.getGet();

        int maxFullPromotionSets = promotionStock / promotionSetSize;
        int maxPromotionQuantity = maxFullPromotionSets * promotionSetSize;
        int applicablePromotionQuantity = Math.min(quantity, maxPromotionQuantity);

        if (applicablePromotionQuantity > 0) {
            reducePromotionStock(product, applicablePromotionQuantity);
            addToPurchasedItems(orderResult, product, applicablePromotionQuantity);

            int completePromotionSets = applicablePromotionQuantity / promotionSetSize;
            int freeItemsCount = completePromotionSets * promotion.getGet();
            addToFreeItems(freeItems, product, freeItemsCount);
        }

        int remainingQuantity = quantity - applicablePromotionQuantity;
        int remainingPromotionStock = promotionStock - applicablePromotionQuantity;

        if (remainingPromotionStock > 0 && remainingPromotionStock < promotionSetSize) {
            boolean confirmFullPrice = InputView.getNoPromotionConfirmation(product.getName(), remainingQuantity);
            if (confirmFullPrice) {
                reducePromotionStock(product, remainingPromotionStock);
                remainingQuantity -= remainingPromotionStock;
                handleNonPromotionalPurchase(product, remainingQuantity,remainingPromotionStock, orderResult);
            }
        } else if (remainingQuantity > 0) {
            handleNonPromotionalPurchase(product, remainingQuantity, remainingPromotionStock,orderResult);
        }
    }

    private static void handleNonPromotionalPurchase(Product product, int remainingQuantity, int remainingPromotionStock, Map<Product, Integer> orderResult) {
        if (remainingQuantity > 0) {
            int generalStock = product.getGeneralStock();
            int quantityToUse = Math.min(remainingQuantity, generalStock);
            reduceGeneralStock(product, remainingQuantity);
            addToPurchasedItems(orderResult, product, quantityToUse+remainingPromotionStock);
        }
    }

    private static void reducePromotionStock(Product product, int quantity) {
        product.reduceStock(quantity, true);
    }

    private static void reduceGeneralStock(Product product, int quantity) {
        product.reduceStock(quantity, false);
    }

    private static void addToPurchasedItems(Map<Product, Integer> purchasedItems, Product product, int quantity) {
        purchasedItems.put(product, purchasedItems.getOrDefault(product, 0) + quantity);
    }

    private static void addToFreeItems(Map<Product, Integer> freeItems, Product product, int quantity) {
        if (quantity > 0) {
            freeItems.put(product, freeItems.getOrDefault(product, 0) + quantity);
        }
    }

    private static String[] parseUserInput(String userInput) {
        if (userInput.contains(" ")) {
            throw new IllegalArgumentException(INVALID_INPUT_ERROR);
        }
        return userInput.replaceAll("\\[|\\]", "").split(COMMA_SEPARATOR);
    }

    private static void checkStock(Product product, int quantity) {
        if ((product.getGeneralStock() + product.getPromotionStock()) < quantity) {
            throw new IllegalArgumentException(OUT_OF_STOCK_ERROR);
        }
    }

    private static String[] getOneOrder(String order) {
        String[] oneOrder = order.split(HYPHEN);
        if (oneOrder.length != 2) {
            throw new IllegalArgumentException(INVALID_ORDER_FORMAT_ERROR);
        }
        return oneOrder;
    }

    private static int getQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_INPUT_ERROR);
        }
    }

    private static Product findExistingProduct(String productName, List<Product> availableProducts) {
        return availableProducts.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NAME_ERROR));
    }
}
