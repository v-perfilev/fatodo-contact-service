package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RelationRepository extends JpaRepository<Relation, UUID> {

    int countByFirstUserId(UUID firstUserId);

    List<Relation> findAllByFirstUserId(UUID firstUserId);

    @Query("SELECT r FROM Relation r WHERE (r.firstUserId = ?1 AND r.secondUserId = ?2)"
            + " or (r.firstUserId = ?2 AND r.secondUserId = ?1)")
    List<Relation> findAllByUserIds(UUID firstUserId, UUID secondUserId);

}
