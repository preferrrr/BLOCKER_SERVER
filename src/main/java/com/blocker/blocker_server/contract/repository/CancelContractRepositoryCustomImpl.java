package com.blocker.blocker_server.contract.repository;

import com.blocker.blocker_server.contract.domain.CancelContract;
import com.blocker.blocker_server.contract.domain.CancelContractState;
import com.blocker.blocker_server.contract.domain.QCancelContract;
import com.blocker.blocker_server.sign.domain.QCancelSign;
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
public class CancelContractRepositoryCustomImpl implements CancelContractRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QCancelContract cancelContract = QCancelContract.cancelContract;
    QCancelSign cancelSign = QCancelSign.cancelSign;
    QUser user = QUser.user;

    @Override
    public Optional<CancelContract> findCancelContractWithSignsById(Long cancelContractId) {

        JPAQuery<CancelContract> getCancelContractQuery = jpaQueryFactory
                .selectFrom(cancelContract)
                .join(cancelContract.cancelSigns, cancelSign).fetchJoin()
                .join(cancelSign.user, user).fetchJoin()
                .where(cancelContract.cancelContractId.eq(cancelContractId));

        return Optional.ofNullable(
                getCancelContractQuery.fetchOne()
        );
    }

    @Override
    public List<CancelContract> findCancelContractsByUserAndState(User user, CancelContractState state) {

        JPAQuery<CancelContract> getCancelContractsQuery = jpaQueryFactory
                .selectFrom(cancelContract)
                .distinct()
                .join(cancelContract.cancelSigns, cancelSign)
                .where(cancelSign.user.email.eq(user.getEmail()),
                        cancelContract.cancelContractState.eq(state));

        return getCancelContractsQuery.fetch();
    }
}
