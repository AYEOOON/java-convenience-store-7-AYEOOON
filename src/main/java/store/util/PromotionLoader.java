package store.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.model.Promotion;

public class PromotionLoader {
    public List<String> loadPromotionData(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    public List<Promotion> parsePromotion(List<String> lines){
        List<Promotion> promotions = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++){
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
