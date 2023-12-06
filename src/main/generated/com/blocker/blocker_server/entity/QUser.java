package com.blocker.blocker_server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.signature.domain.Signature;
import com.blocker.blocker_server.user.domain.User;
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

    private static final long serialVersionUID = -133213359L;

    public static final QUser user = new QUser("user");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final ListPath<AgreementSign, QAgreementSign> agreementSigns = this.<AgreementSign, QAgreementSign>createList("agreementSigns", AgreementSign.class, QAgreementSign.class, PathInits.DIRECT2);

    public final ListPath<Board, QBoard> boards = this.<Board, QBoard>createList("boards", Board.class, QBoard.class, PathInits.DIRECT2);

    public final ListPath<Bookmark, QBookmark> bookmarks = this.<Bookmark, QBookmark>createList("bookmarks", Bookmark.class, QBookmark.class, PathInits.DIRECT2);

    public final ListPath<ChatUser, QChatUser> chatUsers = this.<ChatUser, QChatUser>createList("chatUsers", ChatUser.class, QChatUser.class, PathInits.DIRECT2);

    public final ListPath<Contract, QContract> contracts = this.<Contract, QContract>createList("contracts", Contract.class, QContract.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath picture = createString("picture");

    public final StringPath refreshtokenValue = createString("refreshtokenValue");

    public final ListPath<String, StringPath> roles = this.<String, StringPath>createList("roles", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<Signature, QSignature> signatures = this.<Signature, QSignature>createList("signatures", Signature.class, QSignature.class, PathInits.DIRECT2);

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

