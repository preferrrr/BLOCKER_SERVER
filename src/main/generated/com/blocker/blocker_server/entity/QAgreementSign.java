package com.blocker.blocker_server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.blocker.blocker_server.sign.domain.AgreementSign;
import com.blocker.blocker_server.sign.domain.SignState;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAgreementSign is a Querydsl query type for AgreementSign
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAgreementSign extends EntityPathBase<AgreementSign> {

    private static final long serialVersionUID = 1208040993L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAgreementSign agreementSign = new QAgreementSign("agreementSign");

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

    public QAgreementSign(String variable) {
        this(AgreementSign.class, forVariable(variable), INITS);
    }

    public QAgreementSign(Path<? extends AgreementSign> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAgreementSign(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAgreementSign(PathMetadata metadata, PathInits inits) {
        this(AgreementSign.class, metadata, inits);
    }

    public QAgreementSign(Class<? extends AgreementSign> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contract = inits.isInitialized("contract") ? new QContract(forProperty("contract"), inits.get("contract")) : null;
        this.id = inits.isInitialized("id") ? new QSignId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

