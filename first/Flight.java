package first;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Flight extends TourService {

    private String origin;
    private String destination;
    private String flightNumber;
    private boolean baggageInclude;

    public Flight() {
        super();
    }

    public Flight(Integer id, String name, BigDecimal price, LocalDate from, LocalDate to,
                  String origin, String destination, String flightNumber, boolean baggageInclude) {
        super(id, name, price, from, to);
        this.origin = origin;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.baggageInclude = baggageInclude;
    }

    @Override
    public BigDecimal calculateTotalPrice(int participants) {
        BigDecimal base = getPrice().multiply(BigDecimal.valueOf(participants));
        return baggageInclude
                ? base.multiply(new BigDecimal("1.3"))
                : base;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public boolean isBaggageInclude() {
        return baggageInclude;
    }

    public void setBaggageInclude(boolean baggageInclude) {
        this.baggageInclude = baggageInclude;
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DecimalFormat money = new DecimalFormat("#0.00");

        return "Flight{" +
                "id=" + getId() +
                ", name=\"" + getName() + "\"" +
                ", price=" + (getPrice() != null ? money.format(getPrice()) : "null") +
                ", from=" + (getFrom() != null ? df.format(getFrom()) : "null") +
                ", to=" + (getTo() != null ? df.format(getTo()) : "null") +
                ", origin=\"" + origin + "\"" +
                ", destination=\"" + destination + "\"" +
                ", flightNumber=\"" + flightNumber + "\"" +
                ", baggageInclude=" + baggageInclude +
                "}";
    }
}