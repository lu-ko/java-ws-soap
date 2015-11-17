package sk.elko.hpt.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import sk.elko.hpt.core.bo.Destination;
import sk.elko.hpt.core.bo.Hotel;
import sk.elko.hpt.core.common.HptException;
import sk.elko.hpt.core.service.DbService;
import sk.elko.hpt.core.service.DestinationService;

public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DbService dbService;

    @Override
    public List<Destination> findAll() {
        return dbService.findAllDestinations();
    }

    @Override
    public List<Destination> findAllWithNoActiveHotel() {
        return dbService.findAllDestinationsWithNoActiveHotel();
    }

    @Override
    public Destination findByName(String destinationName) {
        return dbService.findDestinationByName(destinationName);
    }

    @Override
    public Destination get(Long destinationId) {
        return dbService.getDestination(destinationId);
    }

    @Override
    public Destination create(Destination destination) throws HptException {
        return dbService.createDestination(destination);
    }

    @Override
    public void refreshPrice(Long destinationId) throws HptException {
        if (destinationId == null) {
            throw new HptException("Illegal argument: Given destination.id is null!");
        }
        Destination destination = get(destinationId);
        if (destination == null) {
            throw new HptException("Entity not found: Destination[id=" + destinationId + "]");
        }

        Hotel hotel = dbService.getHotelWithLowestPrice(destinationId);
        if (hotel != null) {
            destination.setLowestPrice(hotel.getLowestPrice());
        } else {
            destination.setLowestPrice(null);
        }

        // TODO performance improvement: invoke update only if lowest price was changed
        dbService.updateDestination(destination);
    }

}
