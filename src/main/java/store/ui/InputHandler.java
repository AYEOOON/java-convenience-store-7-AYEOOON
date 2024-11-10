package store.ui;

import camp.nextstep.edu.missionutils.Console;
import store.util.InputValidator;

public class InputHandler{
    private static final String INPUT_PROMPT = "\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    public static String getProductSelection() {
        System.out.println(INPUT_PROMPT);
        return Console.readLine();
    }

    public static boolean getNoPromotionConfirmation(String productName, int quantity) {
        System.out.printf("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n", productName, quantity);
        return InputValidator.getValidatedYesOrNoInput();
    }

    public static boolean getAdditionalPromotionConfirmation(String productName, int quantity) {
        System.out.printf("\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n", productName, quantity);
        return InputValidator.getValidatedYesOrNoInput();
    }

    public static boolean getMembershipDiscountConfirmation() {
        System.out.println("\n멤버십 할인을 받으시겠습니까? (Y/N)");
        return InputValidator.getValidatedYesOrNoInput();
    }

    public static boolean getAdditionalPurchaseConfirmation() {
        System.out.println("\n감사합니다. 추가로 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return InputValidator.getValidatedYesOrNoInput();
    }
}