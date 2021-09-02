package nr.was.global.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing      // Test 시 JpaMetamodelMappingContext.class 관련 오류 막기 위함.
public class JpaAuditingConfiguration {
}
