package com.persoff68.fatodo.model.mapper;

import com.persoff68.fatodo.model.ContactRequest;
import com.persoff68.fatodo.model.dto.ContactRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactRequestMapper {

    ContactRequestDTO pojoToDTO(ContactRequest contactRequest);

}
