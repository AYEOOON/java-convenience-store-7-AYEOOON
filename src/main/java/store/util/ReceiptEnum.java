package store.util;

public enum ReceiptEnum {
    HEADER("==============W 편의점================"),
    ITEM_HEADER("상품명\t\t수량\t금액"),
    FREE_ITEM_HEADER("=============증\t정==============="),
    TOTAL_SUMMARY("===================================="),
    TOTAL_AMOUNT("총구매액\t\t%d\t%,d원\n"),
    EVENT_DISCOUNT("행사할인\t\t\t-%,d원\n"),
    MEMBERSHIP_DISCOUNT("멤버십할인\t\t\t-%,d원\n"),
    FINAL_AMOUNT("내실돈\t\t\t%,d원\n");

    private final String message;

    ReceiptEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}