package com.blocker.blocker_server.contract.domain;

import com.blocker.blocker_server.commons.BaseEntity;
import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CANCEL_CONTRACT")
@Getter
@NoArgsConstructor
@DynamicInsert
public class CancelContract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancel_contract_id")
    private Long cancelContractId;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "contract_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Contract contract;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_state")
    private CancelContractState cancelContractState;


    @OneToMany(mappedBy = "cancelContract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CancelSign> cancelSigns = new ArrayList<>();

    @Builder
    public CancelContract(User user, Contract contract, String title, String content) {
        this.user = user;
        this.contract = contract;
        this.title = title + "의 파기계약서";
        this.content = content + "을 파기하는데 동의합니다.";
        this.cancelContractState = CancelContractState.CANCELING;
    }


    public void updateStateToCanceled() {
        this.cancelContractState = CancelContractState.CANCELED;
    }

}
