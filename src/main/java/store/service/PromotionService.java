package store.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import store.model.Promotion;
import store.global.PromotionUtil;

public class PromotionService {
    private final PromotionUtil promotionLoader = new PromotionUtil();

    public List<Promotion> loadPromotion(String filePath) {
        List<Promotion> promotions = promotionLoader.loadPromotions(filePath);
        if (promotions == null) {
            return Collections.emptyList();
        }
        return promotions;
    }

    public List<Promotion> getActivePromotions(List<Promotion> promotions) {
        return promotions.stream()
                .filter(Promotion::isActive)
                .collect(Collectors.toList());
    }
}
