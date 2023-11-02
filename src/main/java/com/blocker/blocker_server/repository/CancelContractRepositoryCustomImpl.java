package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.entity.CancelContract;
import com.blocker.blocker_server.entity.QCancelContract;
import com.blocker.blocker_server.entity.QCancelSign;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CancelContractRepositoryCustomImpl implements CancelContractRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QCancelContract cancelContract = QCancelContract.cancelContract;
    QCancelSign cancelSign = QCancelSign.cancelSign;

    @Override
    public Optional<CancelContract> findCancelContractWithSignsById(Long cancelContractId) {

        JPAQuery<CancelContract> getCancelContractQuery = jpaQueryFactory
                .selectFrom(cancelContract)
                .join(cancelContract.cancelSigns, cancelSign).fetchJoin()
                .where(cancelContract.cancelContractId.eq(cancelContractId));

        CancelContract result = getCancelContractQuery.fetchOne();

        return Optional.ofNullable(result);
    }
}
