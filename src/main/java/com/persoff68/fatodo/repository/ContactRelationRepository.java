package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.config.aop.cache.annotation.CacheEvictMethod;
import com.persoff68.fatodo.model.ContactRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRelationRepository extends JpaRepository<ContactRelation, UUID> {

    List<ContactRelation> findAllByFirstUserId(UUID firstUserId);

    @CacheEvictMethod(cacheName = "relations-by-id", key = "#firstUserId")
    void deleteByFirstUserIdAndSecondUserId(UUID firstUserId, UUID secondUserId);

    @Query("SELECT r FROM ContactRelation r WHERE (r.firstUserId = ?1 AND r.secondUserId = ?2) or (r.firstUserId = ?2 AND r.secondUserId = ?1)")
    List<ContactRelation> findAllByUserIds(UUID firstUserId, UUID secondUserId);

    @Override
    @CacheEvictMethod(cacheName = "relations-by-id", key = "#contactRelationList.firstUserId")
    @NonNull
    <S extends ContactRelation> List<S> saveAll(@NonNull Iterable<S> contactRelationList);

}
