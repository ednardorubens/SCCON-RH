package br.com.sccon.rh.dto;

import br.com.sccon.rh.domain.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {

    @Mapping(target = "id", ignore = true)
    Person updatePersonFromPersonDB(Person person, @MappingTarget Person personDB);

}
