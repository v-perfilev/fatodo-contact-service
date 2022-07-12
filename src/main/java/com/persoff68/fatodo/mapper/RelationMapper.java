package com.persoff68.fatodo.mapper;

import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.dto.RelationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RelationMapper {

    RelationDTO pojoToDTO(Relation relation);

}
