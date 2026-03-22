import exception.TourServiceValidationException;
import first.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        try {
            Client client = new Client(
                    "Иван Иванов",
                    "ivan.ivanov@example.com",
                    "+375291234567",
                    "AB12345678",
                    50
            );

            HotelStay hotel = new HotelStay(
                    2,
                    "Отель Эллион",
                    new BigDecimal("160.00"),
                    LocalDate.now(),
                    LocalDate.now().plusDays(4),
                    3,
                    3,
                    RoomType.DOUBLE
            );

            Excursion excursion = new Excursion(
                    2,
                    "Обзорная экскурсия",
                    new BigDecimal("60.00"),
                    LocalDate.now(),
                    LocalDate.now(),
                    "Москва",
                    1
            );

            Flight flight = new Flight(
                    3,
                    "Рейс Москва–Париж",
                    new BigDecimal("400.00"),
                    LocalDate.now(),
                    LocalDate.now(),
                    "Москва",
                    "Париж",
                    "B2-123",
                    true
            );

            Map<TourService, Integer> services = new HashMap<>();
            services.put(hotel, 2);
            services.put(excursion, 5);
            services.put(flight, 2);

            Booking booking = new Booking(client, services);

            System.out.println("Создано бронирование:");
            System.out.println(booking);

            System.out.println("\nИтоговая цена: " + booking.calculateTotalPrice());

            booking.confirm();
            System.out.println("\nСтатус после подтверждения: " + booking.getStatus());

            booking.complete();
            System.out.println("Статус после завершения: " + booking.getStatus());
            System.out.println("Баллы клиента: " + client.getLoyaltyPoints());

        } catch (TourServiceValidationException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }
    }
}