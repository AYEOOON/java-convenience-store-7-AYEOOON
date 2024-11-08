package store.ui;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String INPUT_PROMPT = "\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    public static String getProductSelection() {
        System.out.println(INPUT_PROMPT);
        return Console.readLine();
    }
}