package by.psu.mapper;

import by.psu.dto.request.ExcursionRequest;
import by.psu.dto.response.ExcursionResponse;
import by.psu.first.Excursion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.ERROR;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ERROR)
public interface ExcursionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "where", ignore = true)
    @Mapping(target = "day", ignore = true)
    Excursion toEntity(ExcursionRequest request);

    ExcursionResponse toResponse(Excursion excursion);
}


