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
public class Signature {

    @EmbeddedId
    private SignatureId id;

    @MapsId("userId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
