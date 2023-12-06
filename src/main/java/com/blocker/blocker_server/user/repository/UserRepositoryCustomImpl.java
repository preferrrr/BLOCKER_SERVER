package com.blocker.blocker_server.user.repository;

import com.blocker.blocker_server.user.domain.QUser;
import com.blocker.blocker_server.user.domain.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QUser user = QUser.user;

    @Override
    public List<User> searchUsers(String keyword) {

        JPAQuery<User> query = jpaQueryFactory
                .selectFrom(user)
                .distinct()
                .where(likeEmailOrName(keyword));

        List<User> result = query.fetch();

        return result;
    }

    private BooleanBuilder likeEmailOrName(String keyword) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.or(user.email.like("%" + keyword + "%"));
        booleanBuilder.or(user.name.like("%" + keyword + "%"));

        return booleanBuilder;
    }
}
