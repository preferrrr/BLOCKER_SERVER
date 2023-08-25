package com.blocker.blocker_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SignatureId implements Serializable {

    private String email;

    @Column(name = "signature_address")
    private String signatureAddress;

    @Builder
    public SignatureId(String email, String signatureAddress) {
        this.email = email;
        this.signatureAddress = signatureAddress;
    }

}
