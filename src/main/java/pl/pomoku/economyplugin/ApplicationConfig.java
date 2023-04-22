package pl.pomoku.economyplugin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static pl.pomoku.economyplugin.EconomyPlugin.plugin;


@Configuration
@EnableAsync
@EnableJpaRepositories(basePackages = {"pl.pomoku.economyplugin.repository"})
@EnableTransactionManagement
public class ApplicationConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(getMySQLDataSource());
        factoryBean.setPackagesToScan("pl.pomoku.economyplugin.*");
        factoryBean.setPersistenceUnitName("JpaDB");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "true");

        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        factoryBean.setJpaProperties(properties);

        return factoryBean;
    }

    @Bean
    public DataSource getMySQLDataSource() {
        String url = "jdbc:" + plugin.getConfig().getString("database.driver")
                + "://" + plugin.getConfig().getString("database.host")
                + ":" + plugin.getConfig().getString("database.port") + "/"
                + plugin.getConfig().getString("database.database");
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(plugin.getConfig().getString("database.username"));
        dataSource.setPassword(plugin.getConfig().getString("database.password"));
        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
