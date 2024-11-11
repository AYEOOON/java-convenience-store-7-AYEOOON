package store.util;

import store.model.Order;
import store.model.Product;
import store.model.Promotion;
import store.ui.InputHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.global.MessageEnum;

public class OrderUtil {
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
        if (promotion == null || !promotion.isActive()) {
            handleNonPromotionalPurchase(product, quantity, orderResult);
            return;
        }
        if (product.getPromotionStock() < quantity) {
            handlePartialPromotionOrder(product, quantity, orderResult, freeItems);
            return;
        }
        processFullPromotionOrder(product, quantity, orderResult, freeItems);
    }


    private static void processFullPromotionOrder(Product product, int quantity, Map<Product, Integer> orderResult, Map<Product, Integer> freeItems) {
        if (!isPromotionActive(product)) return;
        if (handleAdditionalPurchase(product, quantity, orderResult, freeItems)) return;
        processPromotionSets(product, quantity, orderResult, freeItems);
    }

    private static boolean isPromotionActive(Product product) {
        Promotion promotion = product.getActivePromotion();
        return (promotion != null && promotion.isActive());
    }

    private static boolean handleAdditionalPurchase(Product product, int quantity, Map<Product, Integer> orderResult, Map<Product, Integer> freeItems) {
        if (!shouldAskForAdditionalPurchase(product, quantity)) return false;
        int remainingForPromotion = calculateRemainingForPromotion(product, quantity);
        boolean confirmAdditionalPurchase = askForAdditionalPurchase(product, remainingForPromotion);
        if (confirmAdditionalPurchase) {
            processAdditionalPurchase(product, quantity, remainingForPromotion, orderResult, freeItems);
            return true;
        }
        return false;
    }

    private static boolean shouldAskForAdditionalPurchase(Product product, int quantity) {
        Promotion promotion = product.getActivePromotion();
        return promotion != null && quantity == promotion.getBuy();
    }

    private static int calculateRemainingForPromotion(Product product, int quantity) {
        Promotion promotion = product.getActivePromotion();
        return promotion.getGet() - (quantity % promotion.getBuy());
    }

    private static boolean askForAdditionalPurchase(Product product, int remainingForPromotion) {
        return InputHandler.getAdditionalPromotionConfirmation(product.getName(), remainingForPromotion);
    }

    private static void processAdditionalPurchase(Product product, int quantity, int remainingForPromotion, Map<Product, Integer> orderResult, Map<Product, Integer> freeItems) {
        int totalQuantity = quantity + remainingForPromotion;
        addToPurchasedItems(orderResult, product, totalQuantity);
        addToFreeItems(freeItems, product, remainingForPromotion);
        reducePromotionStock(product, totalQuantity);
    }

    private static void processPromotionSets(Product product, int quantity, Map<Product, Integer> orderResult, Map<Product, Integer> freeItems) {
        Promotion promotion = product.getActivePromotion();
        int sets = quantity / (promotion.getBuy() + promotion.getGet());
        int freeItemsCount = sets * promotion.getGet();
        if (product.getPromotionStock() <= (promotion.getGet()+promotion.getBuy())){
            handlePartialPromotionOrder(product,quantity,orderResult,freeItems);
            return;
        }
        addToFreeItems(freeItems, product, freeItemsCount);
        addToPurchasedItems(orderResult, product, quantity);
        reducePromotionStock(product, quantity);
    }

    private static void handlePartialPromotionOrder(Product product, int quantity, Map<Product, Integer> orderResult, Map<Product, Integer> freeItems) {
        if (isNonPromotional(product)) {
            handleNonPromotionalPurchase(product, quantity, orderResult);
            return;
        }
        int applicablePromotionQuantity = processPromotionStock(product, quantity, orderResult);
        handleFreeItems(product, applicablePromotionQuantity, freeItems);
        int remainingQuantity = quantity - applicablePromotionQuantity;
        if (remainingQuantity > 0) handleRemainingQuantity(product, remainingQuantity, orderResult);
    }

    private static boolean isNonPromotional(Product product) {
        Promotion promotion = product.getActivePromotion();
        return (promotion == null || !promotion.isActive());
    }

    private static int processPromotionStock(Product product, int quantity, Map<Product, Integer> orderResult) {
        int applicablePromotionQuantity = calculateMaxPromotionQuantity(product, quantity);
        if (applicablePromotionQuantity > 0) {
            applyPromotionStock(product, applicablePromotionQuantity, orderResult);
        }
        return applicablePromotionQuantity;
    }

    private static int calculateMaxPromotionQuantity(Product product, int quantity) {
        Promotion promotion = product.getActivePromotion();
        int promotionStock = product.getPromotionStock();
        int promotionSetSize = promotion.getBuy() + promotion.getGet();

        int maxFullPromotionSets = promotionStock / promotionSetSize;
        int maxPromotionQuantity = maxFullPromotionSets * promotionSetSize;
        return Math.min(quantity, maxPromotionQuantity);
    }

    private static void applyPromotionStock(Product product, int applicablePromotionQuantity, Map<Product, Integer> orderResult) {
        reducePromotionStock(product, applicablePromotionQuantity);
        addToPurchasedItems(orderResult, product, applicablePromotionQuantity);
    }

    private static void handleFreeItems(Product product, int applicablePromotionQuantity, Map<Product, Integer> freeItems) {
        Promotion promotion = product.getActivePromotion();
        int promotionSetSize = promotion.getBuy() + promotion.getGet();
        int completePromotionSets = applicablePromotionQuantity / promotionSetSize;
        int freeItemsCount = completePromotionSets * promotion.getGet();
        addToFreeItems(freeItems, product, freeItemsCount);
    }

    private static void handleRemainingQuantity(Product product, int remainingQuantity, Map<Product, Integer> orderResult) {
        boolean confirmFullPrice = InputHandler.getNoPromotionConfirmation(product.getName(), remainingQuantity);
        if (!confirmFullPrice) return;
        int remainingPromotionStock = product.getPromotionStock() % (product.getActivePromotion().getBuy() + product.getActivePromotion().getGet());
        if (remainingPromotionStock > 0) {
            reducePromotionStock(product, remainingPromotionStock);
            addToPurchasedItems(orderResult, product, remainingPromotionStock);
            remainingQuantity -= remainingPromotionStock;
        }
        handleNonPromotionalPurchase(product, remainingQuantity, orderResult);
    }

    private static void handleNonPromotionalPurchase(Product product, int remainingQuantity, Map<Product, Integer> orderResult) {
        if (remainingQuantity <= 0) return;
        reduceGeneralStock(product, remainingQuantity);
        addToPurchasedItems(orderResult, product, remainingQuantity);
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
        if (userInput == null || userInput.isEmpty()) {
            throw new IllegalArgumentException(MessageEnum.INVALID_INPUT_ERROR.getMessage());
        }
        userInput = userInput.replaceAll("\\s", "").replaceAll("\\[|\\]", "");
        return userInput.split(MessageEnum.COMMA_SEPARATOR.getMessage());
    }

    private static void checkStock(Product product, int quantity) {
        if ((product.getGeneralStock() + product.getPromotionStock()) < quantity) {
            throw new IllegalArgumentException(MessageEnum.OUT_OF_STOCK_ERROR.getMessage());
        }
    }

    private static String[] getOneOrder(String order) {
        String[] oneOrder = order.split(MessageEnum.HYPHEN.getMessage());
        if (oneOrder.length != 2) {
            throw new IllegalArgumentException(MessageEnum.INVALID_ORDER_FORMAT_ERROR.getMessage());
        }
        return oneOrder;
    }

    private static int getQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(MessageEnum.INVALID_INPUT_ERROR.getMessage());
        }
    }

    private static Product findExistingProduct(String productName, List<Product> availableProducts) {
        return availableProducts.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(MessageEnum.PRODUCT_NAME_ERROR.getMessage()));
    }
}