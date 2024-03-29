package com.blocker.blocker_server.contract.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCancelContract is a Querydsl query type for CancelContract
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCancelContract extends EntityPathBase<CancelContract> {

    private static final long serialVersionUID = 1918588991L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCancelContract cancelContract = new QCancelContract("cancelContract");

    public final com.blocker.blocker_server.commons.QBaseEntity _super = new com.blocker.blocker_server.commons.QBaseEntity(this);

    public final NumberPath<Long> cancelContractId = createNumber("cancelContractId", Long.class);

    public final EnumPath<CancelContractState> cancelContractState = createEnum("cancelContractState", CancelContractState.class);

    public final ListPath<com.blocker.blocker_server.sign.domain.CancelSign, com.blocker.blocker_server.sign.domain.QCancelSign> cancelSigns = this.<com.blocker.blocker_server.sign.domain.CancelSign, com.blocker.blocker_server.sign.domain.QCancelSign>createList("cancelSigns", com.blocker.blocker_server.sign.domain.CancelSign.class, com.blocker.blocker_server.sign.domain.QCancelSign.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final QContract contract;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath status = _super.status;

    public final StringPath title = createString("title");

    public final com.blocker.blocker_server.user.domain.QUser user;

    public QCancelContract(String variable) {
        this(CancelContract.class, forVariable(variable), INITS);
    }

    public QCancelContract(Path<? extends CancelContract> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCancelContract(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCancelContract(PathMetadata metadata, PathInits inits) {
        this(CancelContract.class, metadata, inits);
    }

    public QCancelContract(Class<? extends CancelContract> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contract = inits.isInitialized("contract") ? new QContract(forProperty("contract"), inits.get("contract")) : null;
        this.user = inits.isInitialized("user") ? new com.blocker.blocker_server.user.domain.QUser(forProperty("user")) : null;
    }

}

