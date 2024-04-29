package com.blocker.blocker_server.board.repository;

import com.blocker.blocker_server.Image.domain.QImage;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.domain.QBoard;
import com.blocker.blocker_server.bookmark.domain.QBookmark;
import com.blocker.blocker_server.contract.domain.QContract;
import com.blocker.blocker_server.user.domain.QUser;
import com.blocker.blocker_server.user.domain.User;
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

    QContract contract = QContract.contract;

    QBookmark bookmark = QBookmark.bookmark;

    @Override
    public List<Board> getBoardList(Pageable pageable) {

        JPAQuery<Board> getBoardListQuery = jpaQueryFactory
                .selectFrom(board)
                .join(board.user, user).fetchJoin()
                .join(board.contract, contract).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        paging(getBoardListQuery, pageable);

        return getBoardListQuery.fetch();

    }

    @Override
    public Optional<Board> getBoardWithImages(Long boardId) {

        JPAQuery<Board> getBoardQuery = jpaQueryFactory
                .selectFrom(board)
                .leftJoin(board.images, image).fetchJoin()
                .join(board.user, user)
                //user를 fetch join하면 필요하지 않은 oneToOne 관계인 signature까지 가져와짐.
                .where(board.boardId.eq(boardId));

        return Optional.ofNullable(
                getBoardQuery.fetchOne()
        );
    }

    @Override
    public List<Board> getBookmarkBoards(User me, Pageable pageable) {
        JPAQuery<Board> getBookmarkBoardsQuery = jpaQueryFactory
                .selectFrom(board)
                .join(board.user, user).fetchJoin()
                .join(board.contract, contract).fetchJoin()
                .join(board.bookmarks, bookmark)
                .where(bookmark.user.email.eq(me.getEmail()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        paging(getBookmarkBoardsQuery, pageable);

        return getBookmarkBoardsQuery.fetch();
    }

    @Override
    public List<Board> getMyBoards(String me, Pageable pageable) {
        JPAQuery<Board> getMyBoardsQuery = jpaQueryFactory
                .selectFrom(board)
                .join(board.user, user).fetchJoin()
                .join(board.contract, contract).fetchJoin()
                .where(board.user.email.eq(me))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        paging(getMyBoardsQuery, pageable);

        return getMyBoardsQuery.fetch();
    }

    private void paging(JPAQuery<Board> query, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }
    }

}
