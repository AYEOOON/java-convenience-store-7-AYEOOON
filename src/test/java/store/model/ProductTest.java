package store.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    private Product colaWithPromo;
    private Product colaWithoutPromo;
    private Promotion promo1;

    @BeforeEach
    public void setUp() {
        // 프로모션 생성
        promo1 = new Promotion("탄산2+1", 2, 1, LocalDate.now().minusDays(1), LocalDate.now().plusDays(5));

        // 프로모션이 있는 상품 (일반 재고: 5개, 프로모션 재고: 10개)
        colaWithPromo = new Product("콜라", 1000, 5, 10, promo1);

        // 프로모션이 없는 일반 상품 (일반 재고: 10개, 프로모션 재고 없음)
        colaWithoutPromo = new Product("사이다", 1000, 10, 0, null);
    }

    @Test
    @DisplayName("프로모션 재고 차감 테스트")
    void 프로모션_재고_차감() {
        // 프로모션 재고에서 차감
        assertTrue(colaWithPromo.reduceStock(2, true));
        assertEquals(8, colaWithPromo.getPromotionStock());

        // 일반 재고에서 차감
        assertTrue(colaWithPromo.reduceStock(3, false));
        assertEquals(2, colaWithPromo.getGeneralStock());
    }

    @Test
    @DisplayName("일반 재고 차감 테스트")
    void 일반_재고_차감() {
        // 일반 재고에서 차감
        assertTrue(colaWithoutPromo.reduceStock(3, false));
        assertEquals(7, colaWithoutPromo.getGeneralStock());

        // 재고 부족 시 차감 실패
        assertFalse(colaWithoutPromo.reduceStock(10, false));
    }
}