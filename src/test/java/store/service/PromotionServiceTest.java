package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.Promotion;
import store.service.PromotionService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PromotionServiceTest {

    private PromotionService promotionService;
    private final String promotionFilePath = "src/main/resources/promotions.md";

    @BeforeEach
    public void 초기화() {
        promotionService = new PromotionService();
    }

    @Test
    public void 프로모션_목록_로드_테스트() {
        List<Promotion> promotions = promotionService.loadPromotion(promotionFilePath);

        assertNotNull(promotions, "프로모션 목록이 null이어서는 안 됩니다.");
        assertTrue(promotions.size() > 0, "프로모션 목록이 비어 있어서는 안 됩니다.");
    }

    @Test
    public void 현재_활성화된_프로모션_필터링_테스트() {
        List<Promotion> promotions = promotionService.loadPromotion(promotionFilePath);
        List<Promotion> activePromotions = promotionService.getActivePromotions(promotions);

        LocalDate today = LocalDate.now();

        // 현재 활성화된 프로모션만 필터링된 것을 확인
        activePromotions.forEach(promotion ->
                assertTrue(promotion.isActive(today), promotion.getName() + " 프로모션이 활성화 상태여야 합니다.")
        );
    }

    @Test
    public void 비활성화된_프로모션_제외_테스트() {
        List<Promotion> promotions = promotionService.loadPromotion(promotionFilePath);
        List<Promotion> activePromotions = promotionService.getActivePromotions(promotions);
        LocalDate today = LocalDate.now();

        // 전체 프로모션에서 활성화되지 않은 프로모션이 activePromotions에 포함되지 않는지 확인
        promotions.stream()
                .filter(promotion -> !promotion.isActive(today))
                .forEach(promotion -> assertFalse(activePromotions.contains(promotion),
                        promotion.getName() + " 프로모션은 비활성화 상태로 activePromotions에 포함되지 않아야 합니다."));
    }
}

