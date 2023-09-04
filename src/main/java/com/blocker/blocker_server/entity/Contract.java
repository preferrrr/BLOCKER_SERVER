package com.blocker.blocker_server.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "CONTRACT")
@Getter
@NoArgsConstructor
@DynamicInsert
public class Contract extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Board board;

    @Builder
    public Contract(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.contractState = ContractState.NOT_CONCLUDED;
    }

    public void modifyContract(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
