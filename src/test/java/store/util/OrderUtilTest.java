package store.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.Order;
import store.model.Product;
import store.model.Promotion;
import store.global.MessageEnum;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderUtilTest {

    private Product productA;
    private Product productB;
    private Product productC;
    private Promotion promotionA;
    private Promotion promotionB;
    private List<Product> availableProducts;

    @BeforeEach
    void setUp() {
        // 현재 날짜를 프로모션 활성화를 위해 설정
        LocalDate currentDate = LocalDate.of(2024, 4, 1);

        // 프로모션 A: 2개 사면 1개 무료, 활성화
        promotionA = new Promotion("프로모션A", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        // 프로모션 B: 3개 사면 2개 무료, 비활성화
        promotionB = new Promotion("프로모션B", 3, 2, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));

        // 상품 A: 프로모션 A 활성화
        productA = new Product("Apple", 1000, 20, 10, promotionA);

        // 상품 B: 프로모션 B 비활성화
        productB = new Product("Banana", 1500, 15, 0, promotionB);

        // 상품 C: 프로모션 없음
        productC = new Product("Cherry", 500, 30, 0, null);

        availableProducts = Arrays.asList(productA, productB, productC);
    }

    @Test
    void 프로모션이없는경우_정상적으로주문이변환된다() {
        String userInput = "[Cherry-5]";

        Order order = OrderUtil.convertInputToOrder(userInput, availableProducts);

        Map<Product, Integer> purchased = order.getOrderItems();
        Map<Product, Integer> free = order.getFreeItems();

        assertEquals(1, purchased.size());
        assertTrue(purchased.containsKey(productC));
        assertEquals(5, purchased.get(productC));

        assertTrue(free.isEmpty());

        assertEquals(25, productC.getGeneralStock()); // 초기 30개에서 5개 구매
    }

    @Test
    void 존재하지않는상품을입력한경우_예외가발생한다() {
        String userInput = "[Orange-2]";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderUtil.convertInputToOrder(userInput, availableProducts);
        });

        assertEquals(MessageEnum.PRODUCT_NAME_ERROR.getMessage(), exception.getMessage());
    }

    @Test
    void 재고가부족한경우_예외가발생한다() {
        String userInput = "[Apple-40]";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderUtil.convertInputToOrder(userInput, availableProducts);
        });

        assertEquals(MessageEnum.OUT_OF_STOCK_ERROR.getMessage(), exception.getMessage());
    }

    @Test
    void 입력형식이잘못된경우_예외가발생한다() {
        String userInput = "[Apple4]";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderUtil.convertInputToOrder(userInput, availableProducts);
        });

        assertEquals(MessageEnum.INVALID_ORDER_FORMAT_ERROR.getMessage(), exception.getMessage());
    }

    @Test
    void 빈입력이들어온경우_예외가발생한다() {
        String userInput = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderUtil.convertInputToOrder(userInput, availableProducts);
        });

        assertEquals(MessageEnum.INVALID_INPUT_ERROR.getMessage(), exception.getMessage());
    }

    @Test
    void null입력이들어온경우_예외가발생한다() {
        String userInput = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderUtil.convertInputToOrder(userInput, availableProducts);
        });

        assertEquals(MessageEnum.INVALID_INPUT_ERROR.getMessage(), exception.getMessage());
    }
}