package sk.elko.hpt.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sk.elko.hpt.core.bo.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    Destination findByName(String name);

    @Query("SELECT d FROM Destination d WHERE 0 = (SELECT count(h) FROM Hotel h WHERE h.destination.id = d.id AND h.active = true)")
    List<Destination> findAllWithNoActiveHotel();

}
