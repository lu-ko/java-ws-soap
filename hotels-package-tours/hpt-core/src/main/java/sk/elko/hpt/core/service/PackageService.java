package sk.elko.hpt.core.service;

import java.util.List;

import sk.elko.hpt.core.bo.Package;
import sk.elko.hpt.core.common.HptException;

/**
 * Services to manipulate with {@link Package}s. Can be injected with real implementation or stub/mock.
 */
public interface PackageService {

    public List<Package> findAll();

    public Package get(Long pckgId);

    public Package create(Package pckg) throws HptException;

    public void setPrice(Long pckgId, double price) throws HptException;

    public void setActive(Long pckgId, boolean active) throws HptException;

    // TODO 10. Get all active pairs of location/destination and it’s cheapest package (returns list of
    // location/destination, name of hotel, departure, arrival, price and duration of package)

}
