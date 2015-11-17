package sk.elko.hpt.core.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.dialect.HSQLDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Testing DB config. Now it is the same DB but tests do not drop/create schema.
 * 
 * @see DbConfig
 */
@Configuration
@EnableJpaRepositories(basePackages = "sk.elko.hpt.core.repository")
@EnableTransactionManagement
public class TestDbConfig {
    private static final Log log = LogFactory.getLog(TestDbConfig.class);

    private final String DB_URL = "jdbc:hsqldb:hsql://localhost/HptDB";
    private final String DB_USERNAME = "sa";
    private final String DB_PASSWORD = "";

    @Bean
    public DataSource dataSource() {
        log.info("dataSource - Initializing TEST HSQL dataSource. Connecting to: " + DB_URL);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(org.hsqldb.jdbcDriver.class.getName());
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        vendorAdapter.setDatabasePlatform(HSQLDialect.class.getName());

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(DbConfig.DB_BO_PACKAGE);
        factory.setDataSource(dataSource());
        factory.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", "none");
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        log.info("transactionManager - JpaTransactionManager initialized.");
        return txManager;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

}