package com.blocker.blocker_server.signature.domain;

import com.blocker.blocker_server.commons.BaseEntity;
import com.blocker.blocker_server.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "SIGNATURE")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class Signature extends BaseEntity {

    @EmbeddedId
    private SignatureId id;

    @MapsId("email")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Signature(User user, String signatureAddress) {
        SignatureId id = SignatureId.builder()
                .email(user.getEmail())
                .signatureAddress(signatureAddress)
                .build();
        this.id = id;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public static Signature create(User user, String signatureAddress) {
        return Signature.builder()
                .user(user)
                .signatureAddress(signatureAddress)
                .build();
    }

}
