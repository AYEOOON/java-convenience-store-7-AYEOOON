package store.ui;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String INPUT_PROMPT = "\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String INVALID_INPUT_ERROR = "[ERROR] 올바르지 않은 입력입니다. 다시 입력해 주세요.";

    public static String getProductSelection() {
        System.out.println(INPUT_PROMPT);
        return Console.readLine();
    }

    public static boolean getPromotionConfirmation(String productName, int quantity) {
        System.out.printf("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n", productName, quantity);
        return getYesOrNoInput();
    }

    public static boolean getMembershipDiscountConfirmation() {
        System.out.println("\n멤버십 할인을 받으시겠습니까? (Y/N)");
        return getYesOrNoInput();
    }

    public static boolean getAdditionalPurchaseConfirmation() {
        System.out.println("\n감사합니다. 추가로 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return getYesOrNoInput();
    }

    private static boolean getYesOrNoInput() {
        String input = Console.readLine();
        if (!input.equals("Y") && !input.equals("N") && input.contains(" ")) {
            System.out.println("[ERROR] 올바르지 않은 입력입니다. 다시 입력해 주세요.");
            return getYesOrNoInput();
        }
        return input.equals("Y");
    }
}