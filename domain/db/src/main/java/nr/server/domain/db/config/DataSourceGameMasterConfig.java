package nr.server.domain.db.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import nr.server.domain.db.annotation.RepositoryLogMaster;
import nr.server.domain.db.annotation.RepositoryLogSlave;
import nr.server.domain.db.annotation.RepositorySlave;
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
        basePackages = "nr.server.domain.db.data",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = {RepositorySlave.class, RepositoryLogMaster.class, RepositoryLogSlave.class}
        ),
        entityManagerFactoryRef = "gameMasterEntityManagerFactory",
        transactionManagerRef = "gameMasterTransactionManager"
)
@RequiredArgsConstructor
public class DataSourceGameMasterConfig {
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource.game.master")
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
                .packages("nr.server.domain.db.data.**.data")
                .persistenceUnit("gameMasterEntityManager")
                .build();
    }

    @Bean
    @Primary
    PlatformTransactionManager gameMasterTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(Objects.requireNonNull(gameMasterEntityManagerFactory(builder).getObject()));
    }
}
