package com.blocker.blocker_server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSign is a Querydsl query type for Sign
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSign extends EntityPathBase<Sign> {

    private static final long serialVersionUID = -133282493L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSign sign = new QSign("sign");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QContract contract;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QSignId id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<SignState> signState = createEnum("signState", SignState.class);

    //inherited
    public final StringPath status = _super.status;

    public final QUser user;

    public QSign(String variable) {
        this(Sign.class, forVariable(variable), INITS);
    }

    public QSign(Path<? extends Sign> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSign(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSign(PathMetadata metadata, PathInits inits) {
        this(Sign.class, metadata, inits);
    }

    public QSign(Class<? extends Sign> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contract = inits.isInitialized("contract") ? new QContract(forProperty("contract"), inits.get("contract")) : null;
        this.id = inits.isInitialized("id") ? new QSignId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

