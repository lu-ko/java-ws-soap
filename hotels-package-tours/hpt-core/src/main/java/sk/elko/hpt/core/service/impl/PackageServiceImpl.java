package sk.elko.hpt.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import sk.elko.hpt.core.bo.Package;
import sk.elko.hpt.core.common.HptException;
import sk.elko.hpt.core.service.DbService;
import sk.elko.hpt.core.service.HotelService;
import sk.elko.hpt.core.service.PackageService;

public class PackageServiceImpl implements PackageService {

    @Autowired
    private DbService dbService;

    @Autowired
    private HotelService hotelService;

    @Override
    public List<Package> findAll() {
        return dbService.findAllPackages();
    }

    @Override
    public Package get(Long pckgId) {
        return dbService.getPackage(pckgId);
    }

    @Override
    public Package create(Package pckg) throws HptException {
        Package created = dbService.createPackage(pckg);
        hotelService.refreshPrice(pckg.getHotel().getId());
        return created;
    }

    @Override
    public void setPrice(Long pckgId, double price) throws HptException {
        Package pckg = get(pckgId);
        if (pckg == null) {
            throw new HptException("Entity not found: Package[id=" + pckgId + "]");
        }

        pckg.setPrice(price);
        dbService.updatePackage(pckg);
        hotelService.refreshPrice(pckg.getHotel().getId());
    }

    @Override
    public void setActive(Long pckgId, boolean active) throws HptException {
        Package pckg = get(pckgId);
        if (pckg == null) {
            throw new HptException("Entity not found: Package[id=" + pckgId + "]");
        }

        pckg.setActive(active);
        dbService.updatePackage(pckg);
    }

}
