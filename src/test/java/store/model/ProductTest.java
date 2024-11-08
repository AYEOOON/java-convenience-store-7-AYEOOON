package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    private Product colaWithPromo;
    private Product colaWithoutPromo;
    private Promotion promo;

    @BeforeEach
    void setUp() {
        // 프로모션 생성
        promo = new Promotion("탄산2+1", 2, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(5));

        // 프로모션이 있는 상품 (일반 재고: 5개, 프로모션 재고: 10개)
        colaWithPromo = new Product("콜라", 1000, 5, 10, promo);

        // 프로모션이 없는 일반 상품 (일반 재고: 10개, 프로모션 재고 없음)
        colaWithoutPromo = new Product("사이다", 1000, 10, 0, null);
    }

    @Test
    @DisplayName("프로모션 재고 증가 테스트")
    void 프로모션_재고_증가() {
        // Given
        colaWithPromo.increasePromotionStock(5);

        // Then
        assertEquals(15, colaWithPromo.getPromotionStock(), "프로모션 재고가 올바르게 증가해야 합니다.");
    }

    @Test
    @DisplayName("일반 재고 증가 테스트")
    void 일반_재고_증가() {
        // Given
        colaWithoutPromo.increaseGeneralStock(5);

        // Then
        assertEquals(15, colaWithoutPromo.getGeneralStock(), "일반 재고가 올바르게 증가해야 합니다.");
    }

    @Test
    @DisplayName("프로모션 재고 차감 테스트")
    void 프로모션_재고_차감() {
        // Given & When
        boolean result = colaWithPromo.reduceStock(3, true);

        // Then
        assertTrue(result, "프로모션 재고 차감이 성공해야 합니다.");
        assertEquals(7, colaWithPromo.getPromotionStock(), "프로모션 재고가 올바르게 차감되어야 합니다.");
    }

    @Test
    @DisplayName("일반 재고 차감 테스트")
    void 일반_재고_차감() {
        // Given & When
        boolean result = colaWithoutPromo.reduceStock(3, false);

        // Then
        assertTrue(result, "일반 재고 차감이 성공해야 합니다.");
        assertEquals(7, colaWithoutPromo.getGeneralStock(), "일반 재고가 올바르게 차감되어야 합니다.");
    }

    @Test
    @DisplayName("재고 차감 실패 테스트 (일반 재고 부족)")
    void 재고_차감_실패_일반() {
        // Given & When
        boolean result = colaWithoutPromo.reduceStock(15, false);

        // Then
        assertFalse(result, "일반 재고 차감이 실패해야 합니다.");
        assertEquals(10, colaWithoutPromo.getGeneralStock(), "일반 재고가 변경되지 않아야 합니다.");
    }

    @Test
    @DisplayName("재고 차감 실패 테스트 (프로모션 재고 부족)")
    void 재고_차감_실패_프로모션() {
        // Given & When
        boolean result = colaWithPromo.reduceStock(15, true);

        // Then
        assertFalse(result, "프로모션 재고 차감이 실패해야 합니다.");
        assertEquals(10, colaWithPromo.getPromotionStock(), "프로모션 재고가 변경되지 않아야 합니다.");
    }

    @Test
    @DisplayName("동일한 상품 병합 테스트")
    void 동일한_상품_병합() {
        // Given
        Product newCola = new Product("콜라", 1000, 7, 3, promo);

        // When
        colaWithPromo.increaseGeneralStock(newCola.getGeneralStock());
        colaWithPromo.increasePromotionStock(newCola.getPromotionStock());

        // Then
        assertEquals(12, colaWithPromo.getGeneralStock(), "일반 재고가 올바르게 병합되어야 합니다.");
        assertEquals(13, colaWithPromo.getPromotionStock(), "프로모션 재고가 올바르게 병합되어야 합니다.");
    }
}