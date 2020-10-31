package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {

    List<Request> findAllByRequesterId(UUID requesterId);

    List<Request> findAllByRecipientId(UUID recipientId);

    Optional<Request> findByRequesterIdAndRecipientId(UUID requesterId, UUID recipientId);

    @Query("SELECT r FROM Request r WHERE (r.requesterId = ?1 AND r.recipientId = ?2) or (r.requesterId = ?2 AND r.recipientId = ?1)")
    List<Request> findAllByUserIds(UUID firstUserId, UUID secondUserId);

}
