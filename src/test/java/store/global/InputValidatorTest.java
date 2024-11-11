package store.global;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import store.global.InputValidator;
import store.global.MessageEnum;

public class InputValidatorTest {

    @Test
    void 공백을포함한입력이들어오면예외를던진다() {
        String invalidInput = "Y N";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            InputValidator.validateUserInput(invalidInput);
        });
        assertEquals(MessageEnum.INVALID_INPUT_ERROR.getMessage(), exception.getMessage());
    }

    @Test
    void 대문자Y를입력하면_true를반환한다() {
        String input = "Y";
        boolean result = InputValidator.validateYesOrNoInput(input);
        assertTrue(result);
    }

    @Test
    void 소문자y를입력하면_true를반환한다() {
        String input = "y";
        boolean result = InputValidator.validateYesOrNoInput(input);
        assertTrue(result);
    }

    @Test
    void 대문자N을입력하면_false를반환한다() {
        String input = "N";
        boolean result = InputValidator.validateYesOrNoInput(input);
        assertFalse(result);
    }

    @Test
    void 소문자n을입력하면_false를반환한다() {
        String input = "n";
        boolean result = InputValidator.validateYesOrNoInput(input);
        assertFalse(result);
    }

    @Test
    void Y또는N이아닌입력이들어오면예외를던진다() {
        String invalidInput = "Maybe";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            InputValidator.validateYesOrNoInput(invalidInput);
        });
        assertEquals(MessageEnum.INVALID_INPUT_ERROR.getMessage(), exception.getMessage());
    }

    @Test
    void 빈문자열입력시예외를던진다() {
        String invalidInput = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            InputValidator.validateYesOrNoInput(invalidInput);
        });
        assertEquals(MessageEnum.INVALID_INPUT_ERROR.getMessage(), exception.getMessage());
    }

    @Test
    void null입력시예외를던진다() {
        String invalidInput = null;

        assertThrows(NullPointerException.class, () -> {
            InputValidator.validateYesOrNoInput(invalidInput);
        });
    }

    @Test
    void 입력이Y또는N일때_true_or_false를반환한다() {
        assertTrue(InputValidator.checkYesOrNo("Y", "Y"));
        assertTrue(InputValidator.checkYesOrNo("y", "Y"));
        assertFalse(InputValidator.checkYesOrNo("N", "Y"));
        assertFalse(InputValidator.checkYesOrNo("n", "Y"));
    }
}
