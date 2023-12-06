package com.blocker.blocker_server.sign.domain;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "CANCEL_SIGN")
@Getter
@NoArgsConstructor
@DynamicInsert
public class CancelSign {

    @EmbeddedId
    private SignId id;

    @MapsId("email")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("contractId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancel_contract_id")
    private CancelContract cancelContract;

    @Enumerated(EnumType.STRING)
    @Column(name = "sign_state")
    private SignState signState;



    @Builder
    public CancelSign(User user, CancelContract cancelContract) {
        SignId id = SignId.builder()
                .contractId(cancelContract.getCancelContractId())
                .email(user.getEmail())
                .build();

        this.id = id;
        this.cancelContract = cancelContract;
        this.user = user;
        this.signState = SignState.N;
    }

    public void sign() {
        this.signState = SignState.Y;
    }

}
