package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.entity.QBoard;
import com.blocker.blocker_server.entity.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QBoard board = QBoard.board;
    QUser user = QUser.user;

    @Override
    public List<Board> getBoards(Pageable pageable) {

        JPAQuery<Board> getBoardsQuery = jpaQueryFactory
                .selectFrom(board)
                .distinct()
                .join(board.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            getBoardsQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Board> result = getBoardsQuery.fetch();


        return result;

    }
}
