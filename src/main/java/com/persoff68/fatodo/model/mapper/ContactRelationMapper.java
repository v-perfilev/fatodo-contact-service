package com.persoff68.fatodo.model.mapper;

import com.persoff68.fatodo.model.ContactRelation;
import com.persoff68.fatodo.model.dto.ContactRelationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactRelationMapper {

    ContactRelationDTO pojoToDTO(ContactRelation contactRelation);

}
