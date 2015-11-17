package sk.elko.hpt.core.service;

import java.util.List;

import sk.elko.hpt.core.bo.Hotel;
import sk.elko.hpt.core.common.HptException;

/**
 * Services to manipulate with {@link Hotel}s. Can be injected with real implementation or stub/mock.
 */
public interface HotelService {

    public List<Hotel> findAll();

    public List<Hotel> findByDestination(Long destinationId);

    public Hotel findByName(String hotelName);

    public Hotel get(Long hotelId);

    public Hotel create(Hotel hotel) throws HptException;

    public void setActive(Long hotelId, boolean active) throws HptException;

    public void refreshPrice(Long hotelId) throws HptException;

}
