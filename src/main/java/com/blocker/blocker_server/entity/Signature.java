package com.blocker.blocker_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@Table(name = "SIGNATURE")
@DynamicInsert
public class Signature extends BaseEntity {

    @EmbeddedId
    private SignatureId id;

    @MapsId("email")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user;


}
