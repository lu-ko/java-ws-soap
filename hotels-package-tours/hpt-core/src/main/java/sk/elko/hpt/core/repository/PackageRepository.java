package sk.elko.hpt.core.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sk.elko.hpt.core.bo.Package;

public interface PackageRepository extends JpaRepository<Package, Long> {

    @Query("SELECT p FROM Package p WHERE p.hotel.id = (:hotelId) ORDER BY p.price ASC")
    List<Package> getPackageWithLowestPrice(@Param("hotelId") Long hotelId, Pageable pageable);

}
