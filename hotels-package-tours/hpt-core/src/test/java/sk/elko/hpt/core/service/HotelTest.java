package sk.elko.hpt.core.service;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import sk.elko.hpt.core.bo.Destination;
import sk.elko.hpt.core.bo.Hotel;
import sk.elko.hpt.core.common.HptException;
import sk.elko.hpt.core.config.AbstractTestContext;

public class HotelTest extends AbstractTestContext {

    private final String EXPECTED_STRING = "Hotel-" + System.currentTimeMillis();
    private int sizeAll = 0;
    private Destination destination = null;

    @BeforeClass
    public void prepareDestination() throws HptException {
        Destination destToCreate = new Destination();
        destToCreate.setId(null);
        destToCreate.setName("Destination for " + EXPECTED_STRING);
        destination = destinationService.create(destToCreate);
        Assert.assertNotNull(destination);
        Assert.assertNotNull(destination.getId());
    }

    @Test
    public void t01_findBefore() {
        List<Hotel> all = hotelService.findAll();
        Assert.assertNotNull(all);
        sizeAll = all.size();

        Hotel byName = hotelService.findByName(EXPECTED_STRING);
        Assert.assertNull(byName);

        List<Hotel> byDest = hotelService.findByDestination(destination.getId());
        Assert.assertNotNull(byDest);
        Assert.assertTrue(byDest.isEmpty());
    }

    @Test(dependsOnMethods = "t01_findBefore")
    public void t02_createAndGet() throws HptException {
        Assert.assertNotNull(destination);
        Assert.assertNotNull(destination.getId());

        Hotel hotel = new Hotel();
        hotel.setId(null);
        hotel.setName(EXPECTED_STRING);
        hotel.setActive(true);
        hotel.setDestination(destination);

        Hotel returned = hotelService.create(hotel);
        Assert.assertNotNull(returned);
        Assert.assertNotNull(returned.getId());

        Hotel created = hotelService.get(returned.getId());
        Assert.assertNotNull(created);
        Assert.assertEquals(created.getName(), EXPECTED_STRING);
        Assert.assertEquals(created.getActive(), true);
        Assert.assertNotNull(created.getDestination());
        Assert.assertEquals(created.getDestination().getId(), destination.getId());
    }

    @Test(dependsOnMethods = "t02_createAndGet")
    public void t03_findAfter() {
        List<Hotel> all = hotelService.findAll();
        Assert.assertNotNull(all);
        Assert.assertTrue(sizeAll < all.size());

        Hotel byName = hotelService.findByName(EXPECTED_STRING);
        Assert.assertNotNull(byName);

        List<Hotel> byDest = hotelService.findByDestination(destination.getId());
        Assert.assertNotNull(byDest);
        Assert.assertFalse(byDest.isEmpty());

        for (Hotel found : byDest) {
            Assert.assertNotNull(found);
            Assert.assertNotNull(found.getId());
            Assert.assertNotNull(found.getDestination());
            Assert.assertEquals(found.getDestination().getId(), destination.getId());
        }
    }

    @Test(dependsOnMethods = "t03_findAfter")
    public void t04_createDuplicateName() throws HptException {
        Hotel hotel = new Hotel();
        hotel.setId(null);
        hotel.setName(EXPECTED_STRING);
        hotel.setActive(true);
        hotel.setDestination(destination);

        try {
            hotelService.create(hotel);
            Assert.fail("DB did not recognized duplicite hotel.name!");
        } catch (HptException e) {
            Assert.assertTrue(e.getMessage().startsWith("Illegal argument: Hotel with given name already exists!"),
                    e.getMessage());
        }
    }

    @Test(dependsOnMethods = "t03_findAfter")
    public void t04_createMissingDestinationId() throws HptException {
        Hotel hotel = new Hotel();
        hotel.setId(null);
        hotel.setName(EXPECTED_STRING + "another");
        hotel.setActive(true);
        hotel.setDestination(new Destination());

        try {
            hotelService.create(hotel);
            Assert.fail("DB did not recognized missing hotel.destination.id!");
        } catch (HptException e) {
            Assert.assertTrue(e.getMessage().startsWith("Missing argument: Given hotel.destination.id is null!"),
                    e.getMessage());
        }
    }

}
