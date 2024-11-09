package store.model;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate start_date;
    private final LocalDate end_date;

    public Promotion(String name, int buy, int get, LocalDate start_date, LocalDate end_date) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public boolean isActive(LocalDate currentDate){
        return ((currentDate.isEqual(start_date) || currentDate.isAfter(start_date))
                && (currentDate.isEqual(end_date) || currentDate.isBefore(end_date)));
    }

    public int calculateDiscount(Product product, int quantity) {
        if (isInvalidPromotion()) return 0;
        int sets = quantity / getBuy();
        int freeItems = sets * getGet();
        return freeItems * product.getPrice();
    }

    private boolean isInvalidPromotion() {
        return buy <= 0 || get <= 0;
    }
}