package com.blocker.blocker_server.sign.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSignId is a Querydsl query type for SignId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSignId extends BeanPath<SignId> {

    private static final long serialVersionUID = 837385526L;

    public static final QSignId signId = new QSignId("signId");

    public final NumberPath<Long> contractId = createNumber("contractId", Long.class);

    public final StringPath email = createString("email");

    public QSignId(String variable) {
        super(SignId.class, forVariable(variable));
    }

    public QSignId(Path<? extends SignId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSignId(PathMetadata metadata) {
        super(SignId.class, metadata);
    }

}

