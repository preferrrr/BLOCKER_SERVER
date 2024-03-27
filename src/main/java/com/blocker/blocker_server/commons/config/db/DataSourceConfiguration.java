package com.blocker.blocker_server.commons.config.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfiguration {

    static final String MASTER_DB = "MASTER_DB";
    static final String SLAVE_DB = "SLAVE_DB";

    @Bean(name = MASTER_DB)
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = SLAVE_DB)
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @DependsOn({MASTER_DB, SLAVE_DB})
    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(
            @Qualifier(MASTER_DB) DataSource masterDataSource,
            @Qualifier(SLAVE_DB) DataSource slaveDataSource) {

        RoutingDataSource routingDataSource = new RoutingDataSource();

        Map<Object, Object> datasourceMap = new HashMap();
        datasourceMap.put(MASTER_DB, masterDataSource);
        datasourceMap.put(SLAVE_DB, slaveDataSource);

        Map<Object, Object> immutableDataSourceMap = Collections.unmodifiableMap(datasourceMap);

        routingDataSource.setTargetDataSources(immutableDataSourceMap);

        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    @DependsOn({"routingDataSource"})
    @Bean
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }
}
