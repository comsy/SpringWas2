package nr.server.cheat.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "nr.server.cheat.domain",
        entityManagerFactoryRef = "cheatEntityManagerFactory",
        transactionManagerRef = "cheatTransactionManager"
)
@RequiredArgsConstructor
public class DataSourceCheatConfig {
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    @Bean
    @ConfigurationProperties(prefix = "datasource.cheat")
    public DataSource cheatDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean cheatEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        var properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
        return builder.dataSource(cheatDataSource())
                .properties(properties)
                .packages("nr.server.cheat.domain")
                .persistenceUnit("cheatEntityManager")
                .build();
    }

    @Bean
    PlatformTransactionManager cheatTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(Objects.requireNonNull(cheatEntityManagerFactory(builder).getObject()));
    }
}
