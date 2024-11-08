package store.util;

import store.model.Order;
import store.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderUtil {
    private static final String COMMA_SEPARATOR = ",";
    private static final String HYPHEN = "-";
    private static final String INVALID_ORDER_FORMAT_ERROR = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String PRODUCT_NAME_ERROR = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String INVALID_ECT_ERROR = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";
    private static final String OUT_OF_STOCK_ERROR = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    public static Order convertInputToOrder(String userInput, List<Product> availableProducts) {
        Map<Product, Integer> orderResult = new HashMap<>();
        String[] orders = parseUserInput(userInput);

        for (String order : orders) {
            processOrderItem(order, availableProducts, orderResult);
        }
        return new Order(orderResult);
    }

    private static void processOrderItem(String order, List<Product> availableProducts, Map<Product, Integer> orderResult) {
        String[] oneOrder = getOneOrder(order);
        String productName = oneOrder[0];
        int quantity = getQuantity(oneOrder[1]);

        Product product = findExistingProduct(productName, availableProducts);
        checkStock(product,quantity);

        addToOrder(orderResult, product, quantity);
    }

    private static String[] parseUserInput(String userInput) {
        if (userInput.contains(" ")) {
            throw new IllegalArgumentException(INVALID_ECT_ERROR);
        }
        return userInput.replaceAll("\\[|\\]", "").split(COMMA_SEPARATOR);
    }

    private static void checkStock(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException(OUT_OF_STOCK_ERROR);
        }
    }

    private static void addToOrder(Map<Product, Integer> orderResult, Product product, int quantity) {
        orderResult.put(product, orderResult.getOrDefault(product, 0) + quantity);
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
            throw new IllegalArgumentException(INVALID_ECT_ERROR);
        }
    }

    private static Product findExistingProduct(String productName, List<Product> availableProducts) {
        return availableProducts.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NAME_ERROR));
    }
}
