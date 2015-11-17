package sk.elko.hpt.core.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import sk.elko.hpt.core.bo.Destination;
import sk.elko.hpt.core.bo.Hotel;
import sk.elko.hpt.core.bo.Package;
import sk.elko.hpt.core.common.HptException;
import sk.elko.hpt.core.repository.DestinationRepository;
import sk.elko.hpt.core.repository.HotelRepository;
import sk.elko.hpt.core.repository.PackageRepository;
import sk.elko.hpt.core.service.DbService;

public class DbServiceImpl implements DbService {
    private static final Log log = LogFactory.getLog(DbServiceImpl.class);

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private PackageRepository packageRepository;

    // XXX: Destination

    @Override
    public List<Destination> findAllDestinations() {
        return destinationRepository.findAll();
    }

    @Override
    public List<Destination> findAllDestinationsWithNoActiveHotel() {
        return destinationRepository.findAllWithNoActiveHotel();
    }

    @Override
    public Destination findDestinationByName(String destinationName) {
        return destinationRepository.findByName(destinationName);
    }

    @Override
    public Destination getDestination(Long destinationId) {
        return destinationRepository.findOne(destinationId);
    }

    @Override
    @Transactional
    public Destination createDestination(Destination destination) throws HptException {
        log.info("createDestination - destination: " + destination);

        if (destination == null) {
            throw new HptException("Illegal argument: Given destination is null!");
        }
        if (destination.getId() != null) {
            throw new HptException("Illegal argument: Given destination.id is NOT null!");
        }
        if (destination.getName() == null) {
            throw new HptException("Missing argument: Given destination.name is null!");
        }
        if (findDestinationByName(destination.getName()) != null) {
            throw new HptException("Illegal argument: Destination with given name already exists!");
        }

        try {
            return destinationRepository.save(destination);
        } catch (Exception e) {
            throw new HptException(e);
        }
    }

    @Override
    @Transactional
    public Destination updateDestination(Destination destination) throws HptException {
        log.info("updateDestination - destination: " + destination);

        if (destination == null) {
            throw new HptException("Illegal argument: Given destination is null!");
        }
        if (destination.getId() == null) {
            throw new HptException("Illegal argument: Given destination.id is null!");
        }

        if (!destinationRepository.exists(destination.getId())) {
            throw new HptException("Entity not found: Destination[id=" + destination.getId() + "]");
        }

        try {
            return destinationRepository.save(destination);
        } catch (Exception e) {
            throw new HptException(e);
        }
    }

    // XXX: Hotel

    @Override
    public List<Hotel> findAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public List<Hotel> findHotelsByDestination(Long destinationId) {
        return hotelRepository.findByDestinationId(destinationId);
    }

    @Override
    public Hotel findHotelByName(String hotelName) {
        return hotelRepository.findByName(hotelName);
    }

    @Override
    public Hotel getHotel(Long hotelId) {
        return hotelRepository.findOne(hotelId);
    }

    @Override
    public Hotel getHotelWithLowestPrice(Long destinationId) {
        List<Hotel> sorted = hotelRepository.getHotelWithLowestPrice(destinationId, new PageRequest(0, 1));
        if (sorted.isEmpty()) {
            log.info("getHotelWithLowestPrice - destinationId = " + destinationId + ", hotel = " + null);
            return null;
        } else {
            Hotel lowestPrice = sorted.get(0);
            log.info("getHotelWithLowestPrice - destinationId = " + destinationId + ", hotel = " + lowestPrice);
            return lowestPrice;
        }
    }

    @Override
    @Transactional
    public Hotel createHotel(Hotel hotel) throws HptException {
        log.info("createHotel - hotel: " + hotel);

        if (hotel == null) {
            throw new HptException("Illegal argument: Given hotel is null!");
        }
        if (hotel.getId() != null) {
            throw new HptException("Illegal argument: Given hotel.id is NOT null!");
        }
        if (hotel.getName() == null) {
            throw new HptException("Missing argument: Given hotel.name is null!");
        }
        if (findHotelByName(hotel.getName()) != null) {
            throw new HptException("Illegal argument: Hotel with given name already exists!");
        }
        if (hotel.getDestination() == null) {
            throw new HptException("Missing argument: Given hotel.destination is null!");
        }
        if (hotel.getDestination().getId() == null) {
            throw new HptException("Missing argument: Given hotel.destination.id is null!");
        }
        if (getDestination(hotel.getDestination().getId()) == null) {
            throw new HptException("Entity not found: Destination[id=" + hotel.getDestination().getId() + "]");
        }
        if (!hotel.getPackages().isEmpty()) {
            throw new HptException("Illegal argument: Given destination.packages is NOT empty!");
        }

        try {
            return hotelRepository.save(hotel);
        } catch (Exception e) {
            throw new HptException(e);
        }
    }

    @Override
    @Transactional
    public Hotel updateHotel(Hotel hotel) throws HptException {
        log.info("updateHotel - hotel: " + hotel);

        if (hotel == null) {
            throw new HptException("Illegal argument: Given hotel is null!");
        }
        if (hotel.getId() == null) {
            throw new HptException("Illegal argument: Given hotel.id is null!");
        }

        if (!hotelRepository.exists(hotel.getId())) {
            throw new HptException("Entity not found: Hotel[id=" + hotel.getId() + "]");
        }

        try {
            return hotelRepository.save(hotel);
        } catch (Exception e) {
            throw new HptException(e);
        }
    }

    // XXX: Package

    @Override
    public List<Package> findAllPackages() {
        return packageRepository.findAll();
    }

    @Override
    public Package getPackage(Long pckgId) {
        return packageRepository.findOne(pckgId);
    }

    @Override
    public Package getPackageWithLowestPrice(Long hotelId) {
        List<Package> sorted = packageRepository.getPackageWithLowestPrice(hotelId, new PageRequest(0, 1));
        if (sorted.isEmpty()) {
            log.info("getPackageWithLowestPrice - hotelId = " + hotelId + ", package = " + null);
            return null;
        } else {
            Package lowestPrice = sorted.get(0);
            log.info("getPackageWithLowestPrice - hotelId = " + hotelId + ", package = " + lowestPrice);
            return lowestPrice;
        }
    }

    @Override
    @Transactional
    public Package createPackage(Package pckg) throws HptException {
        log.info("createPackage - package: " + pckg);

        if (pckg == null) {
            throw new HptException("Illegal argument: Given package is null!");
        }
        if (pckg.getId() != null) {
            throw new HptException("Illegal argument: Given package.id is NOT null!");
        }
        if (pckg.getPrice() < 0) {
            throw new HptException("Illegal argument: Given package.price is negative!");
        }
        if (pckg.getArrival() == null) {
            throw new HptException("Missing argument: Given package.arrival is null!");
        }
        if (pckg.getDeparture() == null) {
            throw new HptException("Missing argument: Given package.departure is null!");
        }
        if (pckg.getHotel() == null) {
            throw new HptException("Missing argument: Given package.hotel is null!");
        }
        if (pckg.getHotel().getId() == null) {
            throw new HptException("Missing argument: Given package.hotel.id is null!");
        }
        if (getHotel(pckg.getHotel().getId()) == null) {
            throw new HptException("Entity not found: Hotel[id=" + pckg.getHotel().getId() + "]");
        }

        try {
            return packageRepository.save(pckg);
        } catch (Exception e) {
            throw new HptException(e);
        }
    }

    @Override
    @Transactional
    public Package updatePackage(Package pckg) throws HptException {
        log.info("updatePackage - hotel: " + pckg);

        if (pckg == null) {
            throw new HptException("Illegal argument: Given hotel is null!");
        }
        if (pckg.getId() == null) {
            throw new HptException("Illegal argument: Given hotel.id is null!");
        }
        if (pckg.getHotel() == null) {
            throw new HptException("Missing argument: Given package.hotel is null!");
        }
        if (pckg.getHotel().getId() == null) {
            throw new HptException("Missing argument: Given package.hotel.id is null!");
        }

        if (!packageRepository.exists(pckg.getId())) {
            throw new HptException("Entity not found: Package[id=" + pckg.getId() + "]");
        }

        try {
            return packageRepository.save(pckg);
        } catch (Exception e) {
            throw new HptException(e);
        }
    }

}
