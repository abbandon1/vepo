package first;

import exception.TourServiceValidationException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Booking {

    private final String bookingId;
    private final Client client;
    private final Map<TourService, Integer> serviceParticipants;
    private final LocalDate bookingDate;
    private BookingStatus status;

    public Booking(Client client, Map<TourService, Integer> serviceParticipants) {

        if (client == null) {
            throw new TourServiceValidationException("Client cannot be null");
        }
        if (serviceParticipants == null || serviceParticipants.isEmpty()) {
            throw new TourServiceValidationException("Service map cannot be null or empty");
        }

        for (Map.Entry<TourService, Integer> entry : serviceParticipants.entrySet()) {
            TourService service = entry.getKey();
            Integer participants = entry.getValue();

            if (service == null) {
                throw new TourServiceValidationException("Service cannot be null");
            }
            if (participants == null || participants <= 0) {
                throw new TourServiceValidationException("Participants must be > 0");
            }
            if (!service.isAvailableOn(LocalDate.now())) {
                throw new TourServiceValidationException("Service " + service.getName() + " is not available today");
            }

            validateHotelStayParticipants(service, participants);
        }

        this.client = client;
        this.serviceParticipants = new HashMap<>(serviceParticipants);
        this.bookingId = generateBookingId();
        this.bookingDate = LocalDate.now();
        this.status = BookingStatus.PENDING;
    }

    private void validateHotelStayParticipants(TourService service, int participants) {
        if (service instanceof HotelStay hotelStay) {
            int max = switch (hotelStay.getRoomType()) {
                case SINGLE -> 1;
                case DOUBLE -> 2;
                case FAMILY -> 4;
            };
            if (participants > max) {
                throw new TourServiceValidationException(
                        "Too many participants for room type " + hotelStay.getRoomType()
                );
            }
        }
    }

    private String generateBookingId() {
        long timestamp = System.currentTimeMillis();
        int random = new Random().nextInt(9000) + 1000;
        return "BK" + timestamp + random;
    }

    public String getBookingId() {
        return bookingId;
    }

    public Client getClient() {
        return client;
    }

    public Map<TourService, Integer> getServiceParticipants() {
        return new HashMap<>(serviceParticipants);
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void addService(TourService service, int participants) {
        if (service == null) {
            throw new TourServiceValidationException("Service cannot be null");
        }
        if (participants <= 0) {
            throw new TourServiceValidationException("Participants must be > 0");
        }
        if (!service.isAvailableOn(LocalDate.now())) {
            throw new TourServiceValidationException("Service is not available today");
        }

        validateHotelStayParticipants(service, participants);

        serviceParticipants.put(service, participants);
    }

    public void removeService(TourService service) {
        serviceParticipants.remove(service);
    }

    public void updateParticipants(TourService service, int newCount) {
        if (!serviceParticipants.containsKey(service)) {
            throw new TourServiceValidationException("Service not found in booking");
        }
        if (newCount <= 0) {
            throw new TourServiceValidationException("Participants must be > 0");
        }

        validateHotelStayParticipants(service, newCount);

        serviceParticipants.put(service, newCount);
    }

    public BigDecimal calculateTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;

        for (Map.Entry<TourService, Integer> entry : serviceParticipants.entrySet()) {
            sum = sum.add(entry.getKey().calculateTotalPrice(entry.getValue()));
        }

        BigDecimal discount = client.getDiscountRate();
        return sum.subtract(sum.multiply(discount));
    }

    public void confirm() {
        if (status != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking can be confirmed only from PENDING");
        }
        status = BookingStatus.CONFIRMED;
    }

    public void complete() {
        if (status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Booking can be completed only from CONFIRMED");
        }
        status = BookingStatus.COMPLETED;

        BigDecimal total = calculateTotalPrice();
        int points = total.multiply(new BigDecimal("0.10")).intValue();
        client.addLoyaltyPoints(points);
    }

    public void cancel() {
        if (status != BookingStatus.PENDING && status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Booking can be cancelled only from PENDING or CONFIRMED");
        }
        status = BookingStatus.CANCELLED;
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DecimalFormat money = new DecimalFormat("#0.00");

        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", client=" + client +
                ", serviceParticipants=" + serviceParticipants +
                ", bookingDate=" + df.format(bookingDate) +
                ", status=" + status +
                '}';
    }
}