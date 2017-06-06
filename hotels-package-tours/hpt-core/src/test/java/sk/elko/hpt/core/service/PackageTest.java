package sk.elko.hpt.core.service;

import java.time.LocalDateTime;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import sk.elko.hpt.core.bo.Destination;
import sk.elko.hpt.core.bo.Hotel;
import sk.elko.hpt.core.bo.Package;
import sk.elko.hpt.core.common.HptException;
import sk.elko.hpt.core.config.AbstractTestContext;

public class PackageTest extends AbstractTestContext {

    private final double EXPECTED_PRICE = System.currentTimeMillis();
    private final LocalDateTime EXPECTED_ARRIVAL = LocalDateTime.now().plusDays(3);
    private final LocalDateTime EXPECTED_DEPARTURE = EXPECTED_ARRIVAL.plusDays(10);
    private int sizeAll = 0;
    private Hotel hotel = null;

    @BeforeClass
    public void prepareHotelAndDestination() throws HptException {
        Assert.assertNotNull(destinationService);
        Assert.assertNotNull(hotelService);
        Assert.assertNotNull(packageService);

        Destination destToCreate = new Destination();
        destToCreate.setId(null);
        destToCreate.setName("Destination for Hotel for Package-" + EXPECTED_PRICE);
        Destination destination = destinationService.create(destToCreate);
        Assert.assertNotNull(destination);
        Assert.assertNotNull(destination.getId());

        Hotel hotelToCreate = new Hotel();
        hotelToCreate.setId(null);
        hotelToCreate.setName("Hotel for Package-" + EXPECTED_PRICE);
        hotelToCreate.setActive(true);
        hotelToCreate.setDestination(destination);

        hotel = hotelService.create(hotelToCreate);
        Assert.assertNotNull(hotel);
        Assert.assertNotNull(hotel.getId());
        Assert.assertNull(hotel.getLowestPrice());
    }

    @Test
    public void t01_findBefore() {
        List<Package> all = packageService.findAll();
        Assert.assertNotNull(all);
        sizeAll = all.size();

        Hotel byHotel = hotelService.get(hotel.getId());
        Assert.assertNotNull(byHotel);
        Assert.assertNotNull(byHotel.getPackages());
        Assert.assertTrue(byHotel.getPackages().isEmpty());
    }

    @Test(dependsOnMethods = "t01_findBefore")
    public void t02_createAndGet() throws HptException {
        Assert.assertNotNull(hotel);
        Assert.assertNotNull(hotel.getId());

        Package pckg = new Package();
        pckg.setId(null);
        pckg.setPrice(EXPECTED_PRICE);
        pckg.setActive(true);
        pckg.setArrival(EXPECTED_ARRIVAL);
        pckg.setDeparture(EXPECTED_DEPARTURE);
        pckg.setHotel(hotel);

        Package returned = packageService.create(pckg);
        Assert.assertNotNull(returned);
        Assert.assertNotNull(returned.getId());

        Package created = packageService.get(returned.getId());
        Assert.assertNotNull(created);
        Assert.assertEquals(created.getPrice(), EXPECTED_PRICE);
        Assert.assertEquals(created.getActive(), true);
        Assert.assertEquals(created.getArrival(), EXPECTED_ARRIVAL);
        Assert.assertEquals(created.getDeparture(), EXPECTED_DEPARTURE);
        Assert.assertNotNull(created.getHotel());
        Assert.assertEquals(created.getHotel().getId(), hotel.getId());

        hotel = hotelService.get(hotel.getId());
        Assert.assertNotNull(hotel);
        Assert.assertNotNull(hotel.getId());
        Assert.assertNotNull(hotel.getLowestPrice());
        Assert.assertEquals(hotel.getLowestPrice().doubleValue(), EXPECTED_PRICE);
    }

    @Test(dependsOnMethods = "t02_createAndGet")
    public void t03_findAfter() {
        List<Package> all = packageService.findAll();
        Assert.assertNotNull(all);
        Assert.assertTrue(sizeAll < all.size());

        Hotel byHotel = hotelService.get(hotel.getId());
        Assert.assertNotNull(byHotel);
        Assert.assertNotNull(byHotel.getPackages());
        Assert.assertFalse(byHotel.getPackages().isEmpty());

        for (Package found : byHotel.getPackages()) {
            Assert.assertNotNull(found);
            Assert.assertNotNull(found.getId());
            Assert.assertNotNull(found.getHotel());
            Assert.assertEquals(found.getHotel().getId(), hotel.getId());
        }
    }

    @Test(dependsOnMethods = "t03_findAfter")
    public void t04_createIllegalPrice() throws HptException {
        Package pckg = new Package();
        pckg.setId(null);
        pckg.setPrice(-1);
        pckg.setActive(true);
        pckg.setArrival(EXPECTED_ARRIVAL);
        pckg.setDeparture(EXPECTED_DEPARTURE);
        pckg.setHotel(hotel);

        try {
            packageService.create(pckg);
            Assert.fail("DB did not recognized illegal package.price!");
        } catch (HptException e) {
            Assert.assertTrue(e.getMessage().startsWith("Illegal argument: Given package.price is negative!"),
                    e.getMessage());
        }
    }

    @Test(dependsOnMethods = "t03_findAfter")
    public void t04_createMissingHotelId() throws HptException {
        Package pckg = new Package();
        pckg.setId(null);
        pckg.setPrice(EXPECTED_PRICE);
        pckg.setActive(true);
        pckg.setArrival(EXPECTED_ARRIVAL);
        pckg.setDeparture(EXPECTED_DEPARTURE);
        pckg.setHotel(new Hotel());

        try {
            packageService.create(pckg);
            Assert.fail("DB did not recognized missing package.hotel.id!");
        } catch (HptException e) {
            Assert.assertTrue(e.getMessage().startsWith("Missing argument: Given package.hotel.id is null!"),
                    e.getMessage());
        }
    }

    @Test(dependsOnMethods = "t04_createMissingHotelId")
    public void t05_changePrice_checkHotelLowestPrice() throws HptException {
        final double LOWEST_PRICE = 100;

        Package pckg = new Package();
        pckg.setId(null);
        pckg.setPrice(LOWEST_PRICE);
        pckg.setActive(true);
        pckg.setArrival(EXPECTED_ARRIVAL);
        pckg.setDeparture(EXPECTED_DEPARTURE);
        pckg.setHotel(hotel);

        Package returned = packageService.create(pckg);
        Assert.assertNotNull(returned);
        Assert.assertNotNull(returned.getId());

        Package created = packageService.get(returned.getId());
        Assert.assertNotNull(created);
        Assert.assertEquals(created.getPrice(), LOWEST_PRICE);
        Assert.assertEquals(created.getActive(), true);
        Assert.assertEquals(created.getArrival(), EXPECTED_ARRIVAL);
        Assert.assertEquals(created.getDeparture(), EXPECTED_DEPARTURE);
        Assert.assertNotNull(created.getHotel());
        Assert.assertEquals(created.getHotel().getId(), hotel.getId());

        hotel = hotelService.get(hotel.getId());
        Assert.assertNotNull(hotel);
        Assert.assertNotNull(hotel.getId());
        Assert.assertNotNull(hotel.getLowestPrice());
        Assert.assertEquals(hotel.getLowestPrice().doubleValue(), LOWEST_PRICE);
    }

}
