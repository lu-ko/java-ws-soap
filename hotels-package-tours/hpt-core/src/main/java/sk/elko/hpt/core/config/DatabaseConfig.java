package sk.elko.hpt.core.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Production DB config.
 */
@Configuration
@PropertySource("classpath:sk/elko/hpt/core/application.properties")
@EnableJpaRepositories(basePackages = "sk.elko.hpt.core.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    private static final Log log = LogFactory.getLog(DatabaseConfig.class);

    public static final String DB_BO_PACKAGE = "sk.elko.hpt.core.bo";

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        log.info("Initializing DataSource - Connecting to: " + env.getProperty("db.url"));

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver_class"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));

        return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(env.getProperty("hibernate.show_sql", Boolean.class));
        vendorAdapter.setDatabasePlatform(env.getProperty("hibernate.dialect"));

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(DB_BO_PACKAGE);
        factory.setDataSource(dataSource());

        factory.getJpaPropertyMap().put("hibernate.connection.charSet", env.getProperty("hibernate.connection.charSet"));
        factory.getJpaPropertyMap().put("hibernate.connection.characterEncoding", env.getProperty("hibernate.connection.characterEncoding"));
        factory.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

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
