package sk.elko.hpt.core.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import sk.elko.hpt.core.service.DestinationService;
import sk.elko.hpt.core.service.HotelService;
import sk.elko.hpt.core.service.PackageService;

/**
 * TODO: test improvement: rollback transactions after execution to achieve clean DB<br />
 * TODO: test improvement: add support for parallel tests<br />
 */
@ContextConfiguration(classes = { CoreConfig.class, TestDbConfig.class })
public abstract class AbstractTestContext extends AbstractTestNGSpringContextTests {
    private static final Log log = LogFactory.getLog(AbstractTestContext.class);

    @Autowired
    protected DestinationService destinationService;

    @Autowired
    protected HotelService hotelService;

    @Autowired
    protected PackageService packageService;

    @BeforeClass
    public void setupTest() {
        Assert.assertNotNull(destinationService);
        Assert.assertNotNull(hotelService);
        Assert.assertNotNull(packageService);
        log.info("setupTest - Test context initialized.");
    }

}
