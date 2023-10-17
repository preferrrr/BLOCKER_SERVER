package com.blocker.blocker_server.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
    public Signature(String email, String signatureAddress, User user) {
        SignatureId id = SignatureId.builder()
                .email(email)
                .signatureAddress(signatureAddress)
                .build();
        this.id = id;
        this.user = user;
    }

}
