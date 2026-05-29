package by.psu;

import by.psu.exception.TourServiceValidationException;
import by.psu.first.*;
import by.psu.first.Client;
import by.psu.first.Excursion;
import by.psu.first.HotelStay;
import by.psu.first.JdbcHelper;
import by.psu.first.Flight;
import by.psu.first.Booking;
import by.psu.first.TourService;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        JdbcHelper jdbcHelper = new JdbcHelper();
        try {

            System.out.println("=== Тестирование работы с БД ===");


            Excursion excursionForDb = new Excursion(
                    null, "Экскурсия в Полоцк", new BigDecimal("75.00"),
                    LocalDate.now(), LocalDate.now(),
                    "Полоцк", 1
            );


            //jdbcHelper.create(excursionForDb);
            System.out.println("1. Экскурсия сохранена в БД.");


            //System.out.println("2. Список экскурсий из базы:");
            //jdbcHelper.getAll().forEach(System.out::println);


            Excursion exc=jdbcHelper.getById(36);

            //if (exc != null) {

            //    exc.setName("Экскурсия в Полоцк");
            //   exc.setPrice(new BigDecimal("150.00"));
            //   exc.setFrom(LocalDate.of(2023, 12, 1));
            //    exc.setTo(LocalDate.of(2023, 12, 2));
            //    exc.setDay(2);
            //    jdbcHelper.update(exc);
            //    System.out.println("Запись с ID обновлена.");
            //} else {
            //    System.out.println("Экскурсия с таким ID не найдена.");
            //}


            System.out.println(jdbcHelper.getById(36));

            System.out.println("2. Список экскурсий из базы:");
            jdbcHelper.getAll().forEach(System.out::println);

            //jdbcHelper.delete(39);
            System.out.println("3. Попытка удаления выполнена.");

            System.out.println("=== Тестирование завершено ===\n");

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
            System.out.println("Итоговая цена экскурсии: " + excursion.calculateTotalPrice(5));

        } catch (TourServiceValidationException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Системная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

