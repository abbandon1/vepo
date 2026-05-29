package by.psu.first;

import by.psu.exception.TourServiceValidationException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;

public class Excursion extends TourService {

    private String guideName;
    private String excursionType;
    private boolean lunchIncluded;
    private String where; // используется для обратной совместимости с JDBC
    private int day;      // используется для обратной совместимости с JDBC

    public Excursion() {
        super();
    }

    public Excursion(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to,
                     String guideName, int day) {
        super(id, name, price, from, to);
        this.guideName = guideName;
        this.where = guideName;
        this.day = day;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        BigDecimal base = getPrice().multiply(BigDecimal.valueOf(participants));
        return participants > 10
                ? base.subtract(base.divide(BigDecimal.valueOf(10), MathContext.DECIMAL128))
                : base;
    }

    // Геттеры и сеттеры
    public String getGuideName() { return guideName; }
    public void setGuideName(String guideName) { this.guideName = guideName; this.where = guideName; }
    public String getExcursionType() { return excursionType; }
    public void setExcursionType(String excursionType) { this.excursionType = excursionType; }
    public boolean isLunchIncluded() { return lunchIncluded; }
    public void setLunchIncluded(boolean lunchIncluded) { this.lunchIncluded = lunchIncluded; }
    public String getWhere() { return where; }
    public void setWhere(String where) { this.where = where; this.guideName = where; }
    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }
}
