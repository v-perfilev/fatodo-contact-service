package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.ContactRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRelationRepository extends JpaRepository<ContactRelation, UUID> {

    List<ContactRelation> findAllByFirstUserId(UUID firstUserId);

    void deleteByFirstUserIdAndSecondUserId(UUID firstUserId, UUID secondUserId);

}
