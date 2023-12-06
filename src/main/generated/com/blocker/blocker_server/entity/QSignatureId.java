package com.blocker.blocker_server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.blocker.blocker_server.signature.domain.SignatureId;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSignatureId is a Querydsl query type for SignatureId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSignatureId extends BeanPath<SignatureId> {

    private static final long serialVersionUID = 890079661L;

    public static final QSignatureId signatureId = new QSignatureId("signatureId");

    public final StringPath email = createString("email");

    public final StringPath signatureAddress = createString("signatureAddress");

    public QSignatureId(String variable) {
        super(SignatureId.class, forVariable(variable));
    }

    public QSignatureId(Path<? extends SignatureId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSignatureId(PathMetadata metadata) {
        super(SignatureId.class, metadata);
    }

}

