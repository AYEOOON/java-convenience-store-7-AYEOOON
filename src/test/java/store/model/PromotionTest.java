package store.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PromotionTest {

    private Promotion activePromotion;
    private Promotion expiredPromotion;
    private Promotion futurePromotion;

    @BeforeEach
    public void setUp() {
        LocalDate today = LocalDate.now();

        // 오늘 날짜에 활성화된 프로모션
        activePromotion = new Promotion("탄산2+1", 2, 1, today.minusDays(1), today.plusDays(5));

        // 이미 종료된 프로모션
        expiredPromotion = new Promotion("MD추천상품", 1, 1, today.minusDays(10), today.minusDays(1));

        // 아직 시작되지 않은 미래의 프로모션
        futurePromotion = new Promotion("반짝할인", 1, 1, today.plusDays(1), today.plusDays(10));
    }

    @Test
    public void 프로모션_활성화_상태_테스트() {
        // 활성화된 프로모션 확인
        assertTrue(activePromotion.isActive(), "프로모션이 현재 활성화 상태여야 합니다.");

        // 만료된 프로모션 확인
        assertFalse(expiredPromotion.isActive(), "만료된 프로모션은 비활성화 상태여야 합니다.");

        // 미래에 시작되는 프로모션 확인
        assertFalse(futurePromotion.isActive(), "미래의 프로모션은 비활성화 상태여야 합니다.");
    }

    @Test
    public void 프로모션_구매조건_및_혜택_테스트() {
        // 프로모션 조건이 올바른지 확인
        assertEquals(2, activePromotion.getBuy(), "구매 조건이 예상과 일치해야 합니다.");
        assertEquals(1, activePromotion.getGet(), "혜택 조건이 예상과 일치해야 합니다.");
    }

    @Test
    public void 프로모션_적용_유효성_테스트() {
        // 프로모션 이름과 조건이 일치하는지 확인
        assertEquals("탄산2+1", activePromotion.getName(), "프로모션 이름이 일치해야 합니다.");
    }
}