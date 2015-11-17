package sk.elko.hpt.core.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import sk.elko.hpt.core.common.JodaDialect;

@Configuration
public class ThymeleafConfig {
    private static final Log log = LogFactory.getLog(ThymeleafConfig.class);

    @Bean
    public JodaDialect jodaDialect() {
        return new JodaDialect();
    }

    @Bean
    public ServletContextTemplateResolver webTemplateResolver() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix("/web_content/html/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(2);
        resolver.setCacheable(false); // set to 'true' in production!
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addTemplateResolver(webTemplateResolver());
        engine.addDialect(jodaDialect());
        return engine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        log.info("thymeleafViewResolver - ThymeleafViewResolver initialized.");
        return resolver;
    }
}
