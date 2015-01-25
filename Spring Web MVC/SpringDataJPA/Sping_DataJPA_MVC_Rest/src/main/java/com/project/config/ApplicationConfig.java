package com.project.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.Database;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.SharedCacheMode;
//import javax.persistence.ValidationMode;
//import javax.sql.DataSource;
//
//@Configuration
//@EnableJpaRepositories("com.project.repository")
//@EnableTransactionManagement
public class ApplicationConfig {
//
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/springdatajpa");
//        dataSource.setUsername("root");
//        dataSource.setPassword("root");
//        return dataSource();
//    }
//
//    @Bean
//    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//    @Bean
//    public JpaVendorAdapter jpaVendorAdapter() {
//        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
//        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
//        hibernateJpaVendorAdapter.setGenerateDdl(true);
//        return hibernateJpaVendorAdapter;
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//        factoryBean.setDataSource(dataSource());
//        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
//        factoryBean.setPackagesToScan("com.project.model");
//        factoryBean.setValidationMode(ValidationMode.NONE);
//        factoryBean.setPersistenceUnitName("project");
//        factoryBean.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
//        return factoryBean;
//    }
}
