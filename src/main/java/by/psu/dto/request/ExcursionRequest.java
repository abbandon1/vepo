package by.psu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExcursionRequest(
        @NotBlank(message = "Имя гида обязательно")
        String guideName,
        @NotBlank(message = "Тип экскурсии обязателен")
        String excursionType,
        @NotNull
        Boolean lunchIncluded,
        @NotBlank(message = "Название обязательное")
        String name,
        @NotNull
        @Positive
        BigDecimal price,
        @NotNull
        LocalDate from,
        @NotNull
        LocalDate to
) {}


/*
{
  "guideName": "Den",
  "excursionType": "tour",
  "lunchIncluded": true,
  "name": "Mir castle",
  "price": 199.99,
  "from": "2026-05-18",
  "to": "2026-05-19"
}
 */