package io.mirukman.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//Bean 생성을 위한 Configuration
@Configuration
@ComponentScan(basePackages = { "io.mirukman" })
@MapperScan(basePackages = { "io.mirukman.mapper" })
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableScheduling
public class RootConfig {
    
    //jdbc, HikariCP 설정
    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        //hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
        //hikariConfig.setJdbcUrl("jdbc:mariadb://localhost:3306/book");
        hikariConfig.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        hikariConfig.setJdbcUrl("jdbc:log4jdbc:mariadb://localhost:3306/book");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("root");
    
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        
		return dataSource;
    }
    
    //MyBatis 설정
    @Bean
    public SqlSessionFactory SqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource());
        return (SqlSessionFactory) sqlSessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }
   
}