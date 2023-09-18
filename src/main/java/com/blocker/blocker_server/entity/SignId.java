package com.blocker.blocker_server.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class SignId implements Serializable {

    private Long contractId;
    private String email;

    @Builder
    public SignId(String email, Long contractId) {
        this.email = email;
        this.contractId = contractId;
    }

}
