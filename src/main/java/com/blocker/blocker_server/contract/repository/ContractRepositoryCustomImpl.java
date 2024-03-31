package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.domain.ContractState;
import com.blocker.blocker_server.contract.domain.QContract;
import com.blocker.blocker_server.sign.domain.QAgreementSign;
import com.blocker.blocker_server.user.domain.QUser;
import com.blocker.blocker_server.user.domain.User;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryCustomImpl implements ContractRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QContract contract = QContract.contract;

    QAgreementSign agreementSign = QAgreementSign.agreementSign;

    QUser user = QUser.user;

    @Override
    public List<Contract> findNotProceedContractsByUserEmailAndContractState(String email, ContractState state) {

        JPAQuery<Contract> getContractListQuery = jpaQueryFactory
                .selectFrom(contract)
                .where(contract.user.email.eq(email),
                        contract.contractState.eq(state))
                .orderBy(contract.modifiedAt.desc());

        return getContractListQuery.fetch();
    }

    @Override
    public Optional<Contract> findProceedContractWithSignById(Long contractId) {

        JPAQuery<Contract> getContractWithSignQuery = jpaQueryFactory
                .selectFrom(contract)
                .join(contract.agreementSigns, agreementSign).fetchJoin()
                .join(agreementSign.user, user).fetchJoin()
                .where(contract.contractId.eq(contractId));

        return Optional.ofNullable(
                getContractWithSignQuery.fetchOne()
        );
    }

    @Override
    public List<Contract> findProceedOrConcludeContractListByUserEmailAndContractState(String email, ContractState state) {

        JPAQuery<Contract> getContractListQuery = jpaQueryFactory
                .selectFrom(contract)
                .join(contract.agreementSigns, agreementSign)
                .where(agreementSign.user.email.eq(email),
                        contract.contractState.eq(state))
                .orderBy(contract.modifiedAt.desc());

        return getContractListQuery.fetch();
    }
}
