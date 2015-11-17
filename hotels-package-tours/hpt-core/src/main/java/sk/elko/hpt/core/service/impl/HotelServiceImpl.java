package sk.elko.hpt.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import sk.elko.hpt.core.bo.Hotel;
import sk.elko.hpt.core.bo.Package;
import sk.elko.hpt.core.common.HptException;
import sk.elko.hpt.core.service.DbService;
import sk.elko.hpt.core.service.DestinationService;
import sk.elko.hpt.core.service.HotelService;

public class HotelServiceImpl implements HotelService {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private DbService dbService;

    @Override
    public List<Hotel> findAll() {
        return dbService.findAllHotels();
    }

    @Override
    public List<Hotel> findByDestination(Long destinationId) {
        return dbService.findHotelsByDestination(destinationId);
    }

    @Override
    public Hotel findByName(String hotelName) {
        return dbService.findHotelByName(hotelName);
    }

    @Override
    public Hotel get(Long hotelId) {
        return dbService.getHotel(hotelId);
    }

    @Override
    public Hotel create(Hotel hotel) throws HptException {
        return dbService.createHotel(hotel);
    }

    @Override
    public void setActive(Long hotelId, boolean active) throws HptException {
        Hotel hotel = get(hotelId);
        if (hotel == null) {
            throw new HptException("Entity not found: Hotel[id=" + hotelId + "]");
        }

        hotel.setActive(active);
        dbService.updateHotel(hotel);
    }

    @Override
    public void refreshPrice(Long hotelId) throws HptException {
        if (hotelId == null) {
            throw new HptException("Illegal argument: Given hotel.id is null!");
        }
        Hotel hotel = get(hotelId);
        if (hotel == null) {
            throw new HptException("Entity not found: Hotel[id=" + hotelId + "]");
        }

        Package pckg = dbService.getPackageWithLowestPrice(hotelId);
        if (pckg != null) {
            hotel.setLowestPrice(pckg.getPrice());
        } else {
            hotel.setLowestPrice(null);
        }

        // TODO performance improvement: invoke update only if lowest price was changed
        dbService.updateHotel(hotel);
        destinationService.refreshPrice(hotel.getDestination().getId());
    }

}
