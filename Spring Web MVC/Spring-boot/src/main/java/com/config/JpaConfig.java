package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class JpaConfig {
    @Autowired
    Environment environment;
    @Autowired
    com.config.DataSource dataSource;

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
