package com.blocker.blocker_server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.blocker.blocker_server.sign.domain.CancelSign;
import com.blocker.blocker_server.sign.domain.SignState;
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

    private static final long serialVersionUID = 2026102365L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCancelSign cancelSign = new QCancelSign("cancelSign");

    public final QCancelContract cancelContract;

    public final QSignId id;

    public final EnumPath<SignState> signState = createEnum("signState", SignState.class);

    public final QUser user;

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
        this.cancelContract = inits.isInitialized("cancelContract") ? new QCancelContract(forProperty("cancelContract"), inits.get("cancelContract")) : null;
        this.id = inits.isInitialized("id") ? new QSignId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

