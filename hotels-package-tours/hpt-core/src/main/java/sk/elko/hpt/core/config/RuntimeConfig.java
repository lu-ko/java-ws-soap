package sk.elko.hpt.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Import({ DatabaseConfig.class })
public class RuntimeConfig {

}
