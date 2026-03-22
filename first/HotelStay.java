package first;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HotelStay extends TourService {

    private int stars;
    private int nights;
    private RoomType roomType;

    public HotelStay() {
    }

    public HotelStay(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to,
                     int stars, int nights, RoomType roomType) {
        super(id, name, price, from, to);
        this.stars = stars;
        this.nights = nights;
        this.roomType = roomType;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        BigDecimal base = getPrice().multiply(BigDecimal.valueOf(participants));

        double nightsMultiplier = switch (nights) {
            case 0 -> 1.0;
            case 1 -> 1.2;
            case 2 -> 1.4;
            case 3 -> 1.6;
            default -> 2.0;
        };

        double starsMultiplier = 1.0 + (stars / 10.0);

        return base
                .multiply(BigDecimal.valueOf(nightsMultiplier))
                .multiply(BigDecimal.valueOf(starsMultiplier));
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DecimalFormat money = new DecimalFormat("#0.00");

        return "HotelStay{" +
                "id=" + getId() +
                ", name=\"" + getName() + "\"" +
                ", price=" + (getPrice() != null ? money.format(getPrice()) : "null") +
                ", from=" + (getFrom() != null ? df.format(getFrom()) : "null") +
                ", to=" + (getTo() != null ? df.format(getTo()) : "null") +
                ", stars=" + stars +
                ", nights=" + nights +
                ", roomType=" + (roomType != null ? roomType.name() : "null") +
                "}";
    }
}