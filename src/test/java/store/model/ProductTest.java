package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product productWithPromotion;
    private Product productWithoutPromotion;
    private Promotion activePromotion;

    @BeforeEach
    public void 초기화() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        // 활성화된 프로모션 객체 생성
        activePromotion = new Promotion("탄산2+1", 2, 1, startDate, endDate);

        // 프로모션이 있는 상품 생성
        productWithPromotion = new Product("콜라", 1000, 10, activePromotion);

        // 프로모션이 없는 상품 생성
        productWithoutPromotion = new Product("물", 500, 10, null);
    }

    @Test
    public void 상품_기본_정보_테스트() {
        // 상품의 기본 정보 확인 (프로모션이 있는 경우)
        assertEquals("콜라", productWithPromotion.getName(), "상품 이름이 일치해야 합니다.");
        assertEquals(1000, productWithPromotion.getPrice(), "상품 가격이 일치해야 합니다.");
        assertEquals(10, productWithPromotion.getQuantity(), "상품 재고 수량이 일치해야 합니다.");
        assertEquals("탄산2+1", productWithPromotion.getActivePromotion().getName(), "상품의 프로모션 이름이 일치해야 합니다.");

        // 상품의 기본 정보 확인 (프로모션이 없는 경우)
        assertEquals("물", productWithoutPromotion.getName(), "상품 이름이 일치해야 합니다.");
        assertEquals(500, productWithoutPromotion.getPrice(), "상품 가격이 일치해야 합니다.");
        assertEquals(10, productWithoutPromotion.getQuantity(), "상품 재고 수량이 일치해야 합니다.");
        assertNull(productWithoutPromotion.getActivePromotion(), "프로모션이 없는 상품의 프로모션 필드는 null이어야 합니다.");
    }

    @Test
    public void 프로모션_적용_테스트() {
        // 프로모션을 상품에 적용
        productWithPromotion.setActivePromotion(activePromotion);

        // 적용된 프로모션 확인
        assertNotNull(productWithPromotion.getActivePromotion(), "활성 프로모션이 적용되어야 합니다.");
        assertEquals("탄산2+1", productWithPromotion.getActivePromotion().getName(), "프로모션 이름이 일치해야 합니다.");
        assertTrue(productWithPromotion.getActivePromotion().isActive(LocalDate.now()), "프로모션이 현재 활성 상태여야 합니다.");
    }

    @Test
    public void 프로모션이_없는_상품의_프로모션_상태_테스트() {
        // 프로모션이 없는 상품에는 activePromotion이 null이어야 합니다.
        assertNull(productWithoutPromotion.getActivePromotion(), "프로모션이 없는 상품은 activePromotion 필드가 null이어야 합니다.");
    }
}

