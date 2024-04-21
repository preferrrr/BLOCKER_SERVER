package com.blocker.blocker_server.board.repository;

import com.blocker.blocker_server.board.domain.QBoard;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.contract.domain.QContract;
import com.blocker.blocker_server.user.domain.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QBoard board = QBoard.board;
    QUser user = QUser.user;
    QContract contract = QContract.contract;


    public List<GetBoardListResponseDto> getBoardListDtoByQuery(Pageable pageable) {

        JPAQuery<GetBoardListResponseDto> getBoardListDtoQuery = jpaQueryFactory
                .select(Projections.fields(GetBoardListResponseDto.class,
                        board.boardId,
                        board.title,
                        board.user.name,
                        board.content,
                        board.representImage,
                        board.view,
                        board.bookmarkCount,
                        board.contract.contractState,
                        board.createdAt,
                        board.modifiedAt))
                .from(board)
                .join(board.user, user)
                .join(board.contract, contract)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        sort(getBoardListDtoQuery, pageable);

        return getBoardListDtoQuery.fetch();

    }


    private void sort(JPAQuery<?> query, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }
    }
}
