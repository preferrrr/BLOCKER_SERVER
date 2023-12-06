package com.blocker.blocker_server.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 69447127L;

    public static final QUser user = new QUser("user");

    public final com.blocker.blocker_server.commons.QBaseEntity _super = new com.blocker.blocker_server.commons.QBaseEntity(this);

    public final ListPath<com.blocker.blocker_server.sign.domain.AgreementSign, com.blocker.blocker_server.sign.domain.QAgreementSign> agreementSigns = this.<com.blocker.blocker_server.sign.domain.AgreementSign, com.blocker.blocker_server.sign.domain.QAgreementSign>createList("agreementSigns", com.blocker.blocker_server.sign.domain.AgreementSign.class, com.blocker.blocker_server.sign.domain.QAgreementSign.class, PathInits.DIRECT2);

    public final ListPath<com.blocker.blocker_server.board.domain.Board, com.blocker.blocker_server.board.domain.QBoard> boards = this.<com.blocker.blocker_server.board.domain.Board, com.blocker.blocker_server.board.domain.QBoard>createList("boards", com.blocker.blocker_server.board.domain.Board.class, com.blocker.blocker_server.board.domain.QBoard.class, PathInits.DIRECT2);

    public final ListPath<com.blocker.blocker_server.bookmark.domain.Bookmark, com.blocker.blocker_server.bookmark.domain.QBookmark> bookmarks = this.<com.blocker.blocker_server.bookmark.domain.Bookmark, com.blocker.blocker_server.bookmark.domain.QBookmark>createList("bookmarks", com.blocker.blocker_server.bookmark.domain.Bookmark.class, com.blocker.blocker_server.bookmark.domain.QBookmark.class, PathInits.DIRECT2);

    public final ListPath<com.blocker.blocker_server.chat.domain.ChatUser, com.blocker.blocker_server.chat.domain.QChatUser> chatUsers = this.<com.blocker.blocker_server.chat.domain.ChatUser, com.blocker.blocker_server.chat.domain.QChatUser>createList("chatUsers", com.blocker.blocker_server.chat.domain.ChatUser.class, com.blocker.blocker_server.chat.domain.QChatUser.class, PathInits.DIRECT2);

    public final ListPath<com.blocker.blocker_server.contract.domain.Contract, com.blocker.blocker_server.contract.domain.QContract> contracts = this.<com.blocker.blocker_server.contract.domain.Contract, com.blocker.blocker_server.contract.domain.QContract>createList("contracts", com.blocker.blocker_server.contract.domain.Contract.class, com.blocker.blocker_server.contract.domain.QContract.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath picture = createString("picture");

    public final StringPath refreshtokenValue = createString("refreshtokenValue");

    public final ListPath<String, StringPath> roles = this.<String, StringPath>createList("roles", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<com.blocker.blocker_server.signature.domain.Signature, com.blocker.blocker_server.signature.domain.QSignature> signatures = this.<com.blocker.blocker_server.signature.domain.Signature, com.blocker.blocker_server.signature.domain.QSignature>createList("signatures", com.blocker.blocker_server.signature.domain.Signature.class, com.blocker.blocker_server.signature.domain.QSignature.class, PathInits.DIRECT2);

    //inherited
    public final StringPath status = _super.status;

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

