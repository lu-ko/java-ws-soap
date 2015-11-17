package sk.elko.hpt.core.service.impl;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sk.elko.hpt.core.service.AppVersionService;

public class AppVersionServiceImpl implements AppVersionService {
    private static final Log log = LogFactory.getLog(AppVersionServiceImpl.class);

    private final String PATH = "/sk/elko/hpt/core/application.properties";
    private Properties props;

    public AppVersionServiceImpl() {
        props = new Properties();
        InputStream inputStream = AppVersionServiceImpl.class.getResourceAsStream(PATH);

        try {
            props.load(inputStream);
        } catch (Exception e) {
            log.error("AppVersionServiceImpl - Failed to load " + PATH + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String getBuildTime() {
        return props.getProperty("build.time");
    }

    @Override
    public String getHptCoreVersion() {
        return props.getProperty("hpt.core.version");
    }

    @Override
    public String getHptSchemaVersion() {
        return props.getProperty("hpt.schema.version");
    }

}
