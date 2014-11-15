package com.itrane.healthcare.init;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * データベース設定.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.itrane.healthcare.repo")  
public class DbConfig {  
          
        @Resource  
        private Environment env;  
  
        // データソースの設定。
        //　 プロパティの値は src/resources/app.properties から取得
        @Bean  
        public DataSource dataSource() {  
                DriverManagerDataSource dataSource = new DriverManagerDataSource();  
                  
                dataSource.setDriverClassName(env.getRequiredProperty("driverClassName"));  
                dataSource.setUrl(env.getRequiredProperty("url"));  
                dataSource.setUsername(env.getRequiredProperty("username"));  
                dataSource.setPassword(env.getRequiredProperty("password"));  
                return dataSource;  
        }  
        
        //エンティティマネージャ・ファクトリ
        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
            vendorAdapter.setGenerateDdl(true);
            vendorAdapter.setShowSql(false);

            LocalContainerEntityManagerFactoryBean factory =
                    new LocalContainerEntityManagerFactoryBean();
            factory.setJpaVendorAdapter(vendorAdapter);
            factory.setPackagesToScan(env.getRequiredProperty("model.scan.package"));
            factory.setDataSource(dataSource());
            
            //JPAプロパティの設定
            Properties properties = new Properties();
            appendProperties(properties, "eclipselink.ddl-generation");
            //properties.put("eclipselink.ddl-generation", "update-tables");
            appendProperties(properties, "eclipselink.target-database");  
            appendProperties(properties, "eclipselink.logging.level");
            
            properties.put("eclipselink.deploy-on-startup", "true");
            properties.put("eclipselink.ddl-generation.output-mode", "database");
            properties.put("eclipselink.weaving", "static");
            properties.put("eclipselink.weaving.lazy", "true");
            properties.put("eclipselink.weaving.internal", "true");
            properties.put("eclipselink.query-results-cache.type", "WEAK");
            properties.put("eclipselink.jdbc.batch-writing", "JDBC");
            properties.put("eclipselink.jdbc.batch-writing.size", "1000");
            factory.setJpaProperties(properties);
            return factory;
        }
        
        private void appendProperties(Properties p, String key) {
        	p.put(key, env.getRequiredProperty(key));
        }
        
        //トランザクションマネージャ
        @Bean  
        public JpaTransactionManager transactionManager() {  
                JpaTransactionManager transactionManager = new JpaTransactionManager();  
                transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());  
                return transactionManager;  
        } 
        
        // JpaDialect
        @Bean
        public EclipseLinkJpaDialect eclipseLinkJpaDialect() {
           return new EclipseLinkJpaDialect();
        }
  
}  