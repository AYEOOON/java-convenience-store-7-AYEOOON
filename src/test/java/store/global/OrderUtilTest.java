package store.global;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.Order;
import store.model.Product;
import store.model.Promotion;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderUtilTest {
    private List<Product> products;

    @BeforeEach
    void setUp() {
        // 테스트용 상품 목록 생성
        Promotion promo1 = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        products = Arrays.asList(
                new Product("콜라", 1000, 10, 10, promo1),
                new Product("사이다", 1000, 8, 7,null),
                new Product("오렌지주스", 1800, 5, 0,null)
        );
    }

    @Test
    @DisplayName("정상적인 입력으로 주문 생성")
    void 정상적인_입력으로_주문_생성() {
        // Given
        String userInput = "[콜라-3],[사이다-2]";

        // When
        Order order = OrderUtil.convertInputToOrder(userInput, products);

        // Then
        assertEquals(2, order.getOrderItems().size());
        assertEquals(3, order.getOrderItems().get(products.get(0)));
        assertEquals(2, order.getOrderItems().get(products.get(1)));
        assertEquals(4000, order.getTotalAmount()); // 콜라 3(2+1)개, 사이다 2개 = 4000원
    }

    @Test
    void 유효하지_않은_형식으로_입력_시_예외_발생() {
        // Given
        String invalidInput = "[콜라-],[사이다-abc]";

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderUtil.convertInputToOrder(invalidInput, products)
        );
        assertEquals("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.", exception.getMessage());
    }

    @Test
    void 존재하지_않는_상품_입력_시_예외_발생() {
        // Given
        String invalidProductInput = "[콜라-2],[물-3]";

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderUtil.convertInputToOrder(invalidProductInput, products)
        );
        assertEquals("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.", exception.getMessage());
    }

    @Test
    void 재고_초과_주문할_시_예외_발생() {
        // Given
        String invalidProductInput = "[콜라-30]";

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderUtil.convertInputToOrder(invalidProductInput, products)
        );
        assertEquals("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.", exception.getMessage());
    }

    @Test
    void 입력에_공백_포함_시_예외_발생() {
        // Given
        String invalidProductInput = "[ 콜라-1]";

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> OrderUtil.convertInputToOrder(invalidProductInput, products)
        );
        assertEquals("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.", exception.getMessage());
    }
}
