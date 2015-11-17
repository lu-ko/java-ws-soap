package sk.elko.hpt.core.service;

import java.util.List;

import sk.elko.hpt.core.bo.Destination;
import sk.elko.hpt.core.bo.Hotel;
import sk.elko.hpt.core.bo.Package;
import sk.elko.hpt.core.common.HptException;

/**
 * TODO: database improvements: create indexes>br />
 */
public interface DbService {

    // XXX: Destination

    public List<Destination> findAllDestinations();

    public List<Destination> findAllDestinationsWithNoActiveHotel();

    public Destination findDestinationByName(String destinationName);

    public Destination getDestination(Long destinationId);

    public Destination createDestination(Destination destination) throws HptException;

    public Destination updateDestination(Destination hotel) throws HptException;

    // XXX: Hotel

    public List<Hotel> findAllHotels();

    public List<Hotel> findHotelsByDestination(Long destinationId);

    public Hotel findHotelByName(String hotelName);

    public Hotel getHotel(Long hotelId);

    /**
     * Returns {@link Hotel} which has the {@link Package} with lowest price for all (active+inactive) packages assigned
     * to the {@link Destination} with given id, or returns {@code null} if destination has only hotels with no
     * packages.
     */
    public Hotel getHotelWithLowestPrice(Long destinationId);

    public Hotel createHotel(Hotel hotel) throws HptException;

    public Hotel updateHotel(Hotel hotel) throws HptException;

    // XXX: Package

    public List<Package> findAllPackages();

    public Package getPackage(Long pckgId);

    /**
     * Returns {@link Package} which has the lowest price for all (active+inactive) packages assigned to the
     * {@link Hotel} with given id, or returns {@code null} if hotel has no packages.
     */
    public Package getPackageWithLowestPrice(Long hotelId);

    public Package createPackage(Package pckg) throws HptException;

    public Package updatePackage(Package pckg) throws HptException;

}
