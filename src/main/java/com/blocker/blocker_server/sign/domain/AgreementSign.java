package com.blocker.blocker_server.sign.domain;

import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.commons.BaseEntity;
import com.blocker.blocker_server.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Table(name = "AGREEMENT_SIGN")
@Getter
@NoArgsConstructor
@DynamicInsert
public class AgreementSign extends BaseEntity {

    @EmbeddedId
    private SignId id;

    @MapsId("email")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("contractId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Enumerated(EnumType.STRING)
    @Column(name = "sign_state")
    private SignState signState;

    @Builder
    private AgreementSign(User user, Contract contract) {
        SignId id = SignId.builder()
                .contractId(contract.getContractId())
                .email(user.getEmail())
                .build();

        this.id = id;
        this.contract = contract;
        this.user = user;
        this.signState = SignState.N;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public static AgreementSign create(User user, Contract contract) {
        return AgreementSign.builder()
                .user(user)
                .contract(contract)
                .build();
    }

    public void sign() {
        this.signState = SignState.Y;
    }

    public void cancel() {
        this.signState = SignState.N;
    }

}
