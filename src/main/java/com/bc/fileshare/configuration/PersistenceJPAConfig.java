package com.bc.fileshare.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:persistence-jdbc.properties"})
//@EnableJpaRepositories(basePackages = "io.swagger.repository")
public class PersistenceJPAConfig {

	@Autowired
	private Environment env;

	@Bean
	public EntityManagerFactory entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(false);
		vendorAdapter.setDatabasePlatform("org.hibernate.dialect.SQLServerDialect");
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setJpaProperties(getHibernateProperties());
		factory.setPackagesToScan("com.bc.fileshare.model");
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public DataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.pass"));
		return dataSource;
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setDataSource(dataSource());
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}

	Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.default_schema", "dbo");
		properties.setProperty("hibernate.ddl-auto", "update");
		properties.setProperty("hibernate.default_catalog", "fileshare");
		//properties.setProperty("hibernate.show_sql","true");
		//properties.setProperty("hibernate.format_sql","true");

		return properties;
	}
}
