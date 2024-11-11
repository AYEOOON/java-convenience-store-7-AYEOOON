package store.ui;

import camp.nextstep.edu.missionutils.Console;
import store.global.InputValidator;
import store.global.MessageEnum;

public class InputHandler {

    public static String getProductSelection() {
        System.out.println(MessageEnum.INPUT_PROMPT.getMessage());
        return Console.readLine();
    }

    public static boolean getNoPromotionConfirmation(String productName, int quantity) {
        System.out.println(MessageEnum.NO_PROMOTION_CONFIRMATION.format(productName, quantity));
        return InputValidator.getValidatedYesOrNoInput();
    }

    public static boolean getAdditionalPromotionConfirmation(String productName, int quantity) {
        System.out.println(MessageEnum.ADDITIONAL_PROMOTION_CONFIRMATION.format(productName, quantity));
        return InputValidator.getValidatedYesOrNoInput();
    }

    public static boolean getMembershipDiscountConfirmation() {
        System.out.println(MessageEnum.MEMBERSHIP_PROMPT.getMessage());
        return InputValidator.getValidatedYesOrNoInput();
    }

    public static boolean getAdditionalPurchaseConfirmation() {
        System.out.println(MessageEnum.ADDITIONAL_PURCHASE_CONFIRMATION.getMessage());
        return InputValidator.getValidatedYesOrNoInput();
    }
}