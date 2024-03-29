package com.blocker.blocker_server.sign.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCancelSign is a Querydsl query type for CancelSign
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCancelSign extends EntityPathBase<CancelSign> {

    private static final long serialVersionUID = -725297707L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCancelSign cancelSign = new QCancelSign("cancelSign");

    public final com.blocker.blocker_server.contract.domain.QCancelContract cancelContract;

    public final QSignId id;

    public final EnumPath<SignState> signState = createEnum("signState", SignState.class);

    public final com.blocker.blocker_server.user.domain.QUser user;

    public QCancelSign(String variable) {
        this(CancelSign.class, forVariable(variable), INITS);
    }

    public QCancelSign(Path<? extends CancelSign> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCancelSign(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCancelSign(PathMetadata metadata, PathInits inits) {
        this(CancelSign.class, metadata, inits);
    }

    public QCancelSign(Class<? extends CancelSign> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cancelContract = inits.isInitialized("cancelContract") ? new com.blocker.blocker_server.contract.domain.QCancelContract(forProperty("cancelContract"), inits.get("cancelContract")) : null;
        this.id = inits.isInitialized("id") ? new QSignId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new com.blocker.blocker_server.user.domain.QUser(forProperty("user")) : null;
    }

}

