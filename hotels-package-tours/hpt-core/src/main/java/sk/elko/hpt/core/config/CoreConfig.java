package sk.elko.hpt.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sk.elko.hpt.core.service.AppVersionService;
import sk.elko.hpt.core.service.DbService;
import sk.elko.hpt.core.service.DestinationService;
import sk.elko.hpt.core.service.HotelService;
import sk.elko.hpt.core.service.PackageService;
import sk.elko.hpt.core.service.impl.AppVersionServiceImpl;
import sk.elko.hpt.core.service.impl.DbServiceImpl;
import sk.elko.hpt.core.service.impl.DestinationServiceImpl;
import sk.elko.hpt.core.service.impl.HotelServiceImpl;
import sk.elko.hpt.core.service.impl.PackageServiceImpl;

@Configuration
public class CoreConfig {

    @Bean
    public DbService dbService() {
        return new DbServiceImpl();
    }

    @Bean
    public DestinationService destinationService() {
        return new DestinationServiceImpl();
    }

    @Bean
    public HotelService hotelService() {
        return new HotelServiceImpl();
    }

    @Bean
    public PackageService packageService() {
        return new PackageServiceImpl();
    }

    @Bean
    public AppVersionService appVersionService() {
        return new AppVersionServiceImpl();
    }

}
