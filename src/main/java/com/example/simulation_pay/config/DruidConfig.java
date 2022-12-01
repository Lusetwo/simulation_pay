package com.example.simulation_pay.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

@SuppressWarnings({"all","unchecked"})
@Configuration
public class DruidConfig {
    private static final Logger log = LoggerFactory.getLogger(DruidConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.dbcp2.initial-size}")
    private int initialSize;

    @Value("${spring.datasource.dbcp2.min-idle}")
    private int minIdle;

    @Value("${spring.datasource.dbcp2.max-total}")
    private int maxtolal;

    @Value("${spring.datasource.dbcp2.max-wait-millis}")
    private int maxWait;

    @Value("${spring.datasource.dbcp2.time-between-eviction-runs-millis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.dbcp2.min-evictable-idle-time-millis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.dbcp2.validation-query}")
    private String validationQuery;

    @Value("${spring.datasource.dbcp2.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.dbcp2.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.dbcp2.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.dbcp2.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.dbcp2.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.dbcp2.filters}")
    private String filters;

    @Value("{spring.datasource.dbcp2.connectionProperties}")
    private String connectionProperties;

    @Bean(initMethod = "init", destroyMethod = "close")
    @Primary
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxtolal);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/zaipei/druid/*");
        /**白名单*/
        reg.addInitParameter("allow", "127.0.0.1");
        //reg.addInitParameter("allow","dingcan.qudone.com");
        reg.addInitParameter("allow","paysystem.qudone.com");
        reg.addInitParameter("loginUsername", "root");
        reg.addInitParameter("loginPassword", "root");
        reg.addInitParameter("resetEnable", "false");
        return reg;
    }

    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean druidStatFilter = new FilterRegistrationBean();
        druidStatFilter.setFilter(new WebStatFilter());
        druidStatFilter.addUrlPatterns("/*");
        druidStatFilter.addInitParameter("DruidWebStatFilter", "/*");
        druidStatFilter.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/monitor/druid/*");
        druidStatFilter.addInitParameter("profileEnable", "true");
        druidStatFilter.addInitParameter("principalCookieName", "USER_COOKIE");
        druidStatFilter.addInitParameter("principalSessionName", "USER_SESSION");
        return druidStatFilter;
    }
}
