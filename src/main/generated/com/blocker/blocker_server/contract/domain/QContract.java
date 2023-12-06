package com.blocker.blocker_server.contract.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContract is a Querydsl query type for Contract
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContract extends EntityPathBase<Contract> {

    private static final long serialVersionUID = -1995503131L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContract contract = new QContract("contract");

    public final com.blocker.blocker_server.commons.QBaseEntity _super = new com.blocker.blocker_server.commons.QBaseEntity(this);

    public final ListPath<com.blocker.blocker_server.sign.domain.AgreementSign, com.blocker.blocker_server.sign.domain.QAgreementSign> agreementSigns = this.<com.blocker.blocker_server.sign.domain.AgreementSign, com.blocker.blocker_server.sign.domain.QAgreementSign>createList("agreementSigns", com.blocker.blocker_server.sign.domain.AgreementSign.class, com.blocker.blocker_server.sign.domain.QAgreementSign.class, PathInits.DIRECT2);

    public final ListPath<com.blocker.blocker_server.board.domain.Board, com.blocker.blocker_server.board.domain.QBoard> board = this.<com.blocker.blocker_server.board.domain.Board, com.blocker.blocker_server.board.domain.QBoard>createList("board", com.blocker.blocker_server.board.domain.Board.class, com.blocker.blocker_server.board.domain.QBoard.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final NumberPath<Long> contractId = createNumber("contractId", Long.class);

    public final EnumPath<ContractState> contractState = createEnum("contractState", ContractState.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath status = _super.status;

    public final StringPath title = createString("title");

    public final com.blocker.blocker_server.user.domain.QUser user;

    public QContract(String variable) {
        this(Contract.class, forVariable(variable), INITS);
    }

    public QContract(Path<? extends Contract> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContract(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContract(PathMetadata metadata, PathInits inits) {
        this(Contract.class, metadata, inits);
    }

    public QContract(Class<? extends Contract> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.blocker.blocker_server.user.domain.QUser(forProperty("user")) : null;
    }

}

