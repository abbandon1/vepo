package first;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Excursion extends TourService {

    private String where;
    private int day;

    public Excursion() {
        super();
    }

    public Excursion(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to,
                     String where, int day) {
        super(id, name, price, from, to);
        this.where = where;
        this.day = day;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        BigDecimal base = getPrice().multiply(BigDecimal.valueOf(participants));

        return participants > 10
                ? base.subtract(base.divide(BigDecimal.valueOf(10), MathContext.DECIMAL128))
                : base;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DecimalFormat money = new DecimalFormat("#0.00");

        return "Excursion{" +
                "id=" + getId() +
                ", name=\"" + getName() + "\"" +
                ", price=" + (getPrice() != null ? money.format(getPrice()) : "null") +
                ", from=" + (getFrom() != null ? df.format(getFrom()) : "null") +
                ", to=" + (getTo() != null ? df.format(getTo()) : "null") +
                ", where=\"" + where + "\"" +
                ", day=" + day +
                "}";
    }
}