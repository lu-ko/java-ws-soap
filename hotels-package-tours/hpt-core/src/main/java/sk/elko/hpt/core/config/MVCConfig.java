package sk.elko.hpt.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(useDefaultFilters = true, basePackages = { "sk.elko.hpt.core.controller" })
public class MVCConfig extends WebMvcConfigurerAdapter {

}
