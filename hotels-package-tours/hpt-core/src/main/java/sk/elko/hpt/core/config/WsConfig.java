package sk.elko.hpt.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Configuration
@ComponentScan(basePackages = { "sk.elko.hpt.core.ws" })
@ImportResource("classpath:/sk/elko/hpt/core/config/ws/wsdl-config.xml")
public class WsConfig {

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        SaajSoapMessageFactory factory = new SaajSoapMessageFactory();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("soapVersion", "org.springframework.ws.soap.SoapVersion.SOAP_12");
        factory.setSoapVersion(SoapVersion.SOAP_12);
        return factory;
    }
}
