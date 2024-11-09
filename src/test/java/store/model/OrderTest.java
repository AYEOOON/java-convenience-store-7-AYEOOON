package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTest {
    private Product colaWithPromo;
    private Product energyBarWithoutPromo;
    private Product vitaminWaterWithoutPromo;
    private Promotion promo1;

    @BeforeEach
    public void setUp() {
        // 프로모션 생성
        promo1 = new Promotion("탄산2+1", 2, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(5));

        // 상품 생성
        colaWithPromo = new Product("콜라", 1000, 10, 10, promo1);
        energyBarWithoutPromo = new Product("에너지바", 2000, 10, 0, null);
        vitaminWaterWithoutPromo = new Product("비타민워터", 1500, 20, 0, null);
    }

    @Test
    @DisplayName("멤버십 할인 적용 테스트 - 프로모션이 없는 상품에만 할인 적용")
    void 멤버십_할인_적용_테스트() {
        // Given: 주문 생성
        Map<Product, Integer> orderItems = Map.of(
                colaWithPromo, 3, // 콜라 3개 (프로모션 적용)
                energyBarWithoutPromo, 5, // 에너지바 5개 (프로모션 없음)
                vitaminWaterWithoutPromo, 4 // 비타민워터 4개 (프로모션 없음)
        );
        Order order = new Order(orderItems);

        // 행사 할인 적용 (콜라의 프로모션 할인)
        int eventDiscount = order.calculatePromotionDiscount();
        order.applyDiscount(eventDiscount);

        // When: 멤버십 할인 적용
        order.applyMembershipDiscount();

        // Then: 총 금액 및 할인 확인
        assertEquals(19000, order.getOriginalTotalAmount(), "총 구매액 확인");
        assertEquals(1000, eventDiscount, "행사 할인 확인");
        assertEquals(4800, order.getMembershipDiscount(), "멤버십 할인 확인");
        assertEquals(13200, order.getTotalAmount(), "최종 결제 금액 확인");
    }

    @Test
    @DisplayName("멤버십 할인 최대 한도 테스트 - 8,000원 초과 시 제한")
    void 멤버십_할인_최대_한도_테스트() {
        // Given: 주문 생성 (고가 상품 추가)
        Map<Product, Integer> orderItems = Map.of(
                energyBarWithoutPromo, 10,
                vitaminWaterWithoutPromo, 20
        );
        Order order = new Order(orderItems);

        // When: 멤버십 할인 적용
        order.applyMembershipDiscount();

        // Then: 멤버십 할인 최대 한도 확인
        assertEquals(8000, order.getMembershipDiscount(), "멤버십 할인 최대 한도 확인");
        assertEquals(50000, order.getOriginalTotalAmount(), "총 구매액 확인");
        assertEquals(42000, order.getTotalAmount(), "최종 결제 금액 확인");
    }
}
