package sk.elko.hpt.core.service;

import java.util.List;

import sk.elko.hpt.core.bo.Destination;
import sk.elko.hpt.core.common.HptException;

/**
 * Services to manipulate with {@link Destination}s. Can be injected with real implementation or stub/mock.
 */
public interface DestinationService {

    public List<Destination> findAll();

    public List<Destination> findAllWithNoActiveHotel();

    public Destination findByName(String destinationName);

    public Destination get(Long destinationId);

    public Destination create(Destination destination) throws HptException;

    public void refreshPrice(Long destinationId) throws HptException;

}
