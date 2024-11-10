package store.util;

public enum MessageEnum {
    WELCOME_MESSAGE("안녕하세요. W편의점입니다.\n"
            + "현재 보유하고 있는 상품입니다."),
    INPUT_PROMPT("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    MEMBERSHIP_PROMPT("\n멤버십 할인을 받으시겠습니까? (Y/N)"),
    ADDITIONAL_PURCHASE_CONFIRMATION("\n감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"),

    ADDITIONAL_PROMOTION_CONFIRMATION("\n현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    NO_PROMOTION_CONFIRMATION("\n현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"),

    INVALID_INPUT_ERROR("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    OUT_OF_STOCK_ERROR("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_ORDER_FORMAT_ERROR("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요."),
    PRODUCT_NAME_ERROR("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."),
    COMMA_SEPARATOR(","),
    HYPHEN("-");

    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
