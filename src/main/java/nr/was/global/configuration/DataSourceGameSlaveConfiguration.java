package nr.was.global.configuration;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import nr.was.global.annotation.RepositorySlave;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "nr.was.domain",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = {RepositorySlave.class}
        ),
        entityManagerFactoryRef = "gameSlaveEntityManagerFactory",
        transactionManagerRef = "gameSlaveTransactionManager"
)
@RequiredArgsConstructor
public class DataSourceGameSlaveConfiguration {
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.game.slave")
    public DataSource gameSlaveDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean gameSlaveEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        var properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
        return builder.dataSource(gameSlaveDataSource())
                .properties(properties)
                .packages("nr.was.domain")
                .persistenceUnit("gameSlaveEntityManager")
                .build();
    }

    @Bean
    PlatformTransactionManager gameSlaveTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(Objects.requireNonNull(gameSlaveEntityManagerFactory(builder).getObject()));
    }
}