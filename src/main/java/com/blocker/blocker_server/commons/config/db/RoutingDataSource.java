package com.blocker.blocker_server.commons.config.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {
    static final String MASTER_DB = "MASTER_DB";
    static final String SLAVE_DB = "SLAVE_DB";
    @Override
    protected Object determineCurrentLookupKey() {

        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? SLAVE_DB : MASTER_DB;
    }
}
