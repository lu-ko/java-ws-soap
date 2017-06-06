package sk.elko.hpt.core.common;

import sk.elko.hpt.core.bo.Destination;
import sk.elko.hpt.core.bo.Hotel;
import sk.elko.hpt.core.bo.Package;
import sk.elko.hpt.schema.ObjectFactory;
import sk.elko.hpt.schema.WsDestination;
import sk.elko.hpt.schema.WsHotel;
import sk.elko.hpt.schema.WsPackage;

public class SchemaMapper {

    private final ObjectFactory objFactory = new ObjectFactory();

    public WsDestination toWs(Destination bo) {
        if (bo == null) {
            return null;
        }

        WsDestination ws = objFactory.createWsDestination();
        ws.setId(bo.getId());
        ws.setName(bo.getName());
        ws.setLowestPrice(bo.getLowestPrice());

        return ws;
    }

    public Destination toBo(WsDestination ws) {
        if (ws == null) {
            return null;
        }

        Destination bo = new Destination();
        if (ws.getId() > 0) {
            bo.setId(ws.getId());
        }
        bo.setName(ws.getName());

        return bo;
    }

    public WsHotel toWs(Hotel bo) {
        if (bo == null) {
            return null;
        }

        WsHotel ws = objFactory.createWsHotel();
        ws.setId(bo.getId());
        ws.setName(bo.getName());
        ws.setActive(bo.getActive());
        ws.setLowestPrice(bo.getLowestPrice());
        ws.setDestination(toWs(bo.getDestination()));
        for (Package boPckg : bo.getPackages()) {
            ws.getPackages().add(toWs(boPckg));
        }

        return ws;
    }

    public Hotel toBo(WsHotel ws) {
        if (ws == null) {
            return null;
        }

        Hotel bo = new Hotel();
        if (ws.getId() > 0) {
            bo.setId(ws.getId());
        }
        bo.setName(ws.getName());
        bo.setActive(ws.isActive());
        bo.setDestination(toBo(ws.getDestination()));

        return bo;
    }

    public WsPackage toWs(Package bo) {
        if (bo == null) {
            return null;
        }

        WsPackage ws = objFactory.createWsPackage();
        ws.setId(bo.getId());
        ws.setPrice(bo.getPrice());
        ws.setActive(bo.getActive());
        ws.setArrival(DateConverter.getXMLGregorianCalendar(bo.getArrival()));
        ws.setDeparture(DateConverter.getXMLGregorianCalendar(bo.getDeparture()));
        // ws.setHotel(toWs(bo.getHotel())); // no cycling!

        return ws;
    }

    public Package toBo(WsPackage ws) {
        if (ws == null) {
            return null;
        }

        Package bo = new Package();
        if (ws.getId() > 0) {
            bo.setId(ws.getId());
        }
        bo.setPrice(ws.getPrice());
        bo.setActive(ws.isActive());
        bo.setArrival(DateConverter.getLocalDate(ws.getArrival()));
        bo.setDeparture(DateConverter.getLocalDate(ws.getDeparture()));
        bo.setHotel(toBo(ws.getHotel()));

        return bo;
    }

}
