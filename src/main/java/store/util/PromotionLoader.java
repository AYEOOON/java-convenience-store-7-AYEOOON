package store.util;

import store.model.Promotion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionLoader {
    public List<Promotion> loadPromotions(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            return parsePromotions(lines);
        } catch (IOException e) {
            System.out.println("[ERROR] 프로모션 파일을 불러오는 중 오류가 발생했습니다.");
            return new ArrayList<>();
        }
    }

    public List<Promotion> parsePromotions(List<String> lines) {
        List<Promotion> promotions = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");

            String name = parts[0].trim();
            int buy = Integer.parseInt(parts[1].trim());
            int get = Integer.parseInt(parts[2].trim());
            LocalDate startDate = LocalDate.parse(parts[3].trim());
            LocalDate endDate = LocalDate.parse(parts[4].trim());

            promotions.add(new Promotion(name, buy, get, startDate, endDate));
        }
        return promotions;
    }
}