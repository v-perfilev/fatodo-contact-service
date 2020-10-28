package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.ContactRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequest, UUID> {

    List<ContactRequest> findAllByRequesterId(UUID requesterId);

    List<ContactRequest> findAllByRecipientId(UUID recipientId);

    Optional<ContactRequest> findByRequesterIdAndRecipientId(UUID requesterId, UUID recipientId);

    @Query("SELECT r FROM ContactRequest r WHERE (r.requesterId = ?1 AND r.recipientId = ?2) or (r.requesterId = ?2 AND r.recipientId = ?1)")
    List<ContactRequest> findAllByUserIds(UUID firstUserId, UUID secondUserId);

}
