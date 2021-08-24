package nr.was.global.configuration;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import nr.was.global.annotation.RepositoryLogMaster;
import nr.was.global.annotation.RepositoryLogSlave;
import nr.was.global.annotation.RepositoryRedis;
import nr.was.global.annotation.RepositorySlave;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "nr.was.domain",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = {RepositorySlave.class, RepositoryLogMaster.class, RepositoryLogSlave.class, RepositoryRedis.class}
        ),
        entityManagerFactoryRef = "gameMasterEntityManagerFactory",
        transactionManagerRef = "gameMasterTransactionManager"
)
@RequiredArgsConstructor
public class DataSourceGameMasterConfiguration {
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.game.master")
    public DataSource gameMasterDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean gameMasterEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        var properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
        return builder.dataSource(gameMasterDataSource())
                .properties(properties)
                .packages("nr.was.domain")
                .persistenceUnit("gameMasterEntityManager")
                .build();
    }

    @Bean
    @Primary
    PlatformTransactionManager gameMasterTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(Objects.requireNonNull(gameMasterEntityManagerFactory(builder).getObject()));
    }
}
