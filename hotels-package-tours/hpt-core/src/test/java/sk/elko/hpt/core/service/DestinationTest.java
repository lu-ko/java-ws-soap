package sk.elko.hpt.core.service;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import sk.elko.hpt.core.bo.Destination;
import sk.elko.hpt.core.common.HptException;
import sk.elko.hpt.core.config.AbstractTestContext;

public class DestinationTest extends AbstractTestContext {

    private final String EXPECTED_STRING = "Destination-" + System.currentTimeMillis();
    private int sizeAll = 0;
    private int sizeWithNoActiveHotel = 0;

    @Test
    public void t01_findBefore() throws HptException {
        List<Destination> all = destinationService.findAll();
        Assert.assertNotNull(all);
        sizeAll = all.size();

        Destination byName = destinationService.findByName(EXPECTED_STRING);
        Assert.assertNull(byName);

        List<Destination> noActive = destinationService.findAllWithNoActiveHotel();
        Assert.assertNotNull(noActive);
        sizeWithNoActiveHotel = noActive.size();
    }

    @Test(dependsOnMethods = "t01_findBefore")
    public void t02_createAndGet() throws HptException {
        Destination destination = new Destination();
        destination.setId(null);
        destination.setName(EXPECTED_STRING);

        Destination returned = destinationService.create(destination);
        Assert.assertNotNull(returned);
        Assert.assertNotNull(returned.getId());

        Destination created = destinationService.get(returned.getId());
        Assert.assertNotNull(created);
        Assert.assertEquals(created.getName(), EXPECTED_STRING);
    }

    @Test(dependsOnMethods = "t02_createAndGet")
    public void t03_findAfter() throws HptException {
        List<Destination> all = destinationService.findAll();
        Assert.assertNotNull(all);
        Assert.assertTrue(sizeAll < all.size());

        Destination byName = destinationService.findByName(EXPECTED_STRING);
        Assert.assertNotNull(byName);

        List<Destination> noActive = destinationService.findAllWithNoActiveHotel();
        Assert.assertNotNull(noActive);
        Assert.assertTrue(sizeWithNoActiveHotel < noActive.size());
    }

    @Test(dependsOnMethods = "t03_findAfter")
    public void t04_createDuplicateName() throws HptException {
        Destination destination = new Destination();
        destination.setId(null);
        destination.setName(EXPECTED_STRING);

        try {
            destinationService.create(destination);
            Assert.fail("DB did not recognized duplicite destination.name!");
        } catch (HptException e) {
            Assert.assertTrue(e.getMessage()
                    .startsWith("Illegal argument: Destination with given name already exists!"), e.getMessage());
        }
    }

    @Test(dependsOnMethods = "t03_findAfter")
    public void t04_createMissingName() throws HptException {
        Destination destination = new Destination();
        destination.setId(null);
        destination.setName(null);

        try {
            destinationService.create(destination);
            Assert.fail("DB did not recognized missing destination.name!");
        } catch (HptException e) {
            Assert.assertTrue(e.getMessage().startsWith("Missing argument: Given destination.name is null!"),
                    e.getMessage());
        }
    }

}
