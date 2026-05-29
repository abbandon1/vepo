package by.psu.controller;

import by.psu.dto.request.ExcursionRequest;
import by.psu.dto.response.ExcursionResponse;
import by.psu.first.Excursion;
import by.psu.first.JdbcHelper;
import by.psu.mapper.ExcursionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "API для экскурсий")
@RestController
@RequestMapping("/api/v1/excursions")
@RequiredArgsConstructor
public class ExcursionController {

    private final JdbcHelper jdbcHelper;
    private final ExcursionMapper excursionMapper;

    @Operation(summary = "Создаёт экскурсию")
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ExcursionResponse create(@RequestBody @Valid ExcursionRequest request){
        Excursion entity = excursionMapper.toEntity(request);
        entity.setDay(1);
        jdbcHelper.create(entity);
        return excursionMapper.toResponse(entity);
    }

    @Operation(summary = "Предоставляет экскурсию по ID")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ExcursionResponse getById(@PathVariable Integer id){
        Excursion entity = jdbcHelper.getById(id);
        if (entity == null) {
            throw new RuntimeException("Экскурсия не найдена!");
        }
        return excursionMapper.toResponse(entity);
    }

    @Operation(summary = "Предоставляет полный список экскурсий")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<ExcursionResponse> getAll() {
        return jdbcHelper.getAll().stream()
                .map(excursionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Обновляет экскурсию по ID")
    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ExcursionResponse update(@PathVariable Integer id, @RequestBody @Valid ExcursionRequest request) {
        Excursion existingEntity = jdbcHelper.getById(id);
        if (existingEntity == null) {
            throw new RuntimeException("Экскурсия для обновления не найдена!");
        }

        Excursion entityToUpdate = excursionMapper.toEntity(request);
        entityToUpdate.setId(id);
        entityToUpdate.setDay(1); // Поддерживаем duration_day=1 для совместимости с БД

        jdbcHelper.update(entityToUpdate);
        return excursionMapper.toResponse(entityToUpdate);
    }

    @Operation(summary = "Удаляет экскурсию по ID")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        // Проверяем, есть ли запись, чтобы выбросить ошибку до удаления (опционально)
        Excursion existingEntity = jdbcHelper.getById(id);
        if (existingEntity == null) {
            throw new RuntimeException("Экскурсия для удаления не найдена!");
        }

        jdbcHelper.delete(id); // Вызываем метод delete из вашего JdbcHelper
    }
}


