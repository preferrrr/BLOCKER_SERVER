package com.blocker.blocker_server.repository;

import com.blocker.blocker_server.entity.*;
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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QBoard board = QBoard.board;
    QUser user = QUser.user;

    QImage image = QImage.image;

    @Override
    public List<Board> getBoardList(Pageable pageable) {

        JPAQuery<Board> getBoardListQuery = jpaQueryFactory
                .selectFrom(board)
                .distinct()
                .join(board.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            getBoardListQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Board> result = getBoardListQuery.fetch();


        return result;

    }

    @Override
    public Optional<Board> getBoard(Long boardId) {

        JPAQuery<Board> getBoardQuery = jpaQueryFactory
                .selectFrom(board)
                .distinct()
                .leftJoin(board.images, image).fetchJoin()
                .join(board.user, user)
                //user를 fetch join하면 필요하지 않은 oneToOne 관계인 signature까지 가져와짐.
                .where(board.boardId.eq(boardId));

        Board result = getBoardQuery.fetchOne();

        return Optional.ofNullable(result);
    }
}
