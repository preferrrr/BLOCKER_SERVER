package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.*;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryCustomImpl implements ContractRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QContract contract = QContract.contract;

    @Override
    public List<Contract> findNotConcludedContracts(User user, ContractState state) {

        JPAQuery<Contract> getContractListQuery = jpaQueryFactory
                .selectFrom(contract)
                .where(contract.user.email.eq(user.getEmail()),
                        contract.contractState.eq(state))
                .orderBy(contract.modifiedAt.desc());

        //TODO : 미체결 계약서 조회 페이징 ?

        List<Contract> result = getContractListQuery.fetch();

        return result;
    }
}
