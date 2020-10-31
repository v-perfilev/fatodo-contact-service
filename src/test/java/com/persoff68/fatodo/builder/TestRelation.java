package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.Relation;
import lombok.Builder;

import java.util.UUID;

public class TestRelation extends Relation {

    @Builder
    TestRelation(UUID id, UUID firstUserId, UUID secondUserId) {
        super(firstUserId, secondUserId);
        this.id = id;
    }

    public static TestRelationBuilder defaultBuilder() {
        return TestRelation.builder()
                .id(UUID.randomUUID())
                .firstUserId(UUID.randomUUID())
                .secondUserId(UUID.randomUUID());
    }

    public Relation toParent() {
        Relation relation = new Relation();
        relation.setId(getId());
        relation.setFirstUserId(getFirstUserId());
        relation.setSecondUserId(getSecondUserId());
        return relation;
    }

}
