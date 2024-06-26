package com.blocker.blocker_server.contract.domain;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.commons.BaseEntity;
import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CONTRACT")
@Getter
@NoArgsConstructor
@DynamicInsert
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long contractId;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_state")
    private ContractState contractState;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> board = new ArrayList<>();

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementSign> agreementSigns = new ArrayList<>();

    @Builder
    private Contract(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.contractState = ContractState.NOT_PROCEED;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public static Contract create(User user, String title, String content) {
        return Contract.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
    }

    public void modifyContract(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateStateToProceed() {
        this.contractState = ContractState.PROCEED;
    }

    public void updateStateToNotProceed() {
        this.contractState = ContractState.NOT_PROCEED;
    }

    public void updateStateToConclude() {
        this.contractState = ContractState.CONCLUDE;
    }

}
