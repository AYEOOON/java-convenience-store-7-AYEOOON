package store.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import store.model.Promotion;
import store.util.PromotionLoader;

public class PromotionService {
    private final PromotionLoader promotionLoader = new PromotionLoader();

    public List<Promotion> loadPromotion(String filePath) {
        try {
            List<String> lines = promotionLoader.loadPromotionData(filePath);
            return promotionLoader.parsePromotion(lines);
        } catch (IOException e) {
            System.out.println("[ERROR] 프로모션 파일을 불러오는 중 오류가 발생했습니다.");
            return null;
        }
    }

    public List<Promotion> getActivePromotions(List<Promotion> promotions){
        LocalDate today = LocalDate.now();
        return promotions.stream()
                .filter(promotion -> promotion.isActive(today))
                .collect(Collectors.toList());
    }
}
