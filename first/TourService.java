package first;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class TourService {

    private Integer id;
    private String name;
    private BigDecimal price;
    private LocalDate from;
    private LocalDate to;

    public TourService() {
    }

    public TourService(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.from = from;
        this.to = to;
    }

    public abstract BigDecimal calculateTotalPrice(int participants);

    public boolean isAvailableOn(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DecimalFormat money = new DecimalFormat("#0.00");

        return "TourService{" +
                "id=" + id +
                ", name=\"" + name + "\"" +
                ", price=" + (price != null ? money.format(price) : "null") +
                ", from=" + (from != null ? df.format(from) : "null") +
                ", to=" + (to != null ? df.format(to) : "null") +
                "}";
    }
}