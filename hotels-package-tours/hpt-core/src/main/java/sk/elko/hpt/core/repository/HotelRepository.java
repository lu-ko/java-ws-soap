package sk.elko.hpt.core.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sk.elko.hpt.core.bo.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Hotel findByName(String name);

    List<Hotel> findByDestinationId(Long destinationId);

    @Query("SELECT h FROM Hotel h WHERE h.destination.id = (:destinationId) ORDER BY h.lowestPrice ASC")
    List<Hotel> getHotelWithLowestPrice(@Param("destinationId") Long destinationId, Pageable pageable);

}
