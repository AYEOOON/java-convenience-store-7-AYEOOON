package store.util;

import camp.nextstep.edu.missionutils.Console;

public class InputValidator {
    public static void validateUserInput(String userInput) {
        if (userInput.contains(" ")) {
            throw new IllegalArgumentException(MessageEnum.INVALID_INPUT_ERROR.getMessage());
        }
    }

    public static boolean checkYesOrNo(String input, String answer){
        return input.equalsIgnoreCase(answer);
    }

    public static boolean validateYesOrNoInput(String userInput) {
        validateUserInput(userInput);
        if (checkYesOrNo(userInput, "Y")) {
            return true;
        }
        if (checkYesOrNo(userInput, "N")) {
            return false;
        }
        throw new IllegalArgumentException(MessageEnum.INVALID_INPUT_ERROR.getMessage());
    }

    public static boolean getValidatedYesOrNoInput() {
        while (true) {
            try {
                String input = Console.readLine();
                return validateYesOrNoInput(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}