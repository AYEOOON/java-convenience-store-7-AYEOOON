package store.util;

import camp.nextstep.edu.missionutils.Console;

public class InputValidator {
    private static final String INVALID_INPUT_ERROR = "[ERROR] 올바르지 않은 입력입니다. 다시 입력해 주세요.";
    public static void validateUserInput(String userInput) {
        if (userInput.contains(" ")) {
            throw new IllegalArgumentException(INVALID_INPUT_ERROR);
        }
    }

    public static boolean validateYesOrNoInput(String userInput) {
        validateUserInput(userInput); // 공백 검사
        if (userInput.equalsIgnoreCase("Y")) {
            return true;
        }
        if (userInput.equalsIgnoreCase("N")) {
            return false;
        }
        System.out.println(INVALID_INPUT_ERROR);
        return getValidatedYesOrNoInput();
    }

    public static boolean getValidatedYesOrNoInput() {
        String input = Console.readLine();
        return validateYesOrNoInput(input);
    }
}