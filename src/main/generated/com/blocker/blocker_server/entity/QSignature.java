package com.blocker.blocker_server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSignature is a Querydsl query type for Signature
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSignature extends EntityPathBase<Signature> {

    private static final long serialVersionUID = 385283314L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSignature signature = new QSignature("signature");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QSignatureId id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    //inherited
    public final StringPath status = _super.status;

    public final QUser user;

    public QSignature(String variable) {
        this(Signature.class, forVariable(variable), INITS);
    }

    public QSignature(Path<? extends Signature> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSignature(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSignature(PathMetadata metadata, PathInits inits) {
        this(Signature.class, metadata, inits);
    }

    public QSignature(Class<? extends Signature> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QSignatureId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

