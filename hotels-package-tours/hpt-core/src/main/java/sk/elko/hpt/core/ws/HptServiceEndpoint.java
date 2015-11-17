package sk.elko.hpt.core.ws;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import sk.elko.hpt.core.bo.Destination;
import sk.elko.hpt.core.bo.Hotel;
import sk.elko.hpt.core.bo.Package;
import sk.elko.hpt.core.common.HptException;
import sk.elko.hpt.core.common.SchemaMapper;
import sk.elko.hpt.core.service.DestinationService;
import sk.elko.hpt.core.service.HotelService;
import sk.elko.hpt.core.service.PackageService;
import sk.elko.hpt.schema.*;

/**
 * TODO endpoint improvement: do some validation before invoking business service<br />
 */
@Endpoint(value = SOAPBinding.SOAP12HTTP_BINDING)
public class HptServiceEndpoint {
    private static final Log log = LogFactory.getLog(HptServiceEndpoint.class);

    private static final String TARGET_NAMESPACE = "http://hpt.elko.sk/hpt/1.0/service";
    private final ObjectFactory objFactory = new ObjectFactory();

    public HptServiceEndpoint() {
        log.info("HptServiceEndpoint - initializing...");
    }

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private PackageService packageService;

    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = "findDestinationRequest")
    @ResponsePayload
    public JAXBElement<FindDestinationResponse> findDestination(
            @RequestPayload JAXBElement<FindDestinationRequest> request) {
        String destinationName = request.getValue().getDestinationName();
        log.info("findDestination - WS destinationName = " + destinationName);

        FindDestinationResponse response = objFactory.createFindDestinationResponse();

        if (StringUtils.hasLength(destinationName)) {
            // find by name
            Destination bo = destinationService.findByName(destinationName);
            if (bo != null) {
                response.getResultList().add(new SchemaMapper().toWs(bo));
            }
        } else {
            // find all
            List<Destination> boList = destinationService.findAll();
            for (Destination bo : boList) {
                response.getResultList().add(new SchemaMapper().toWs(bo));
            }
        }

        return objFactory.createFindDestinationResponse(response);
    }

    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = "createDestinationRequest")
    @ResponsePayload
    public JAXBElement<CreateDestinationResponse> createDestination(
            @RequestPayload JAXBElement<CreateDestinationRequest> request) throws HptException {
        WsDestination wsDest = request.getValue().getDestination();
        log.info("createDestination - WS destination = " + wsDest);

        Destination destination = destinationService.create(new SchemaMapper().toBo(wsDest));

        CreateDestinationResponse response = objFactory.createCreateDestinationResponse();
        response.setDestinationId(destination.getId());

        return objFactory.createCreateDestinationResponse(response);
    }

    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = "findHotelRequest")
    @ResponsePayload
    public JAXBElement<FindHotelResponse> findHotel(@RequestPayload JAXBElement<FindHotelRequest> request) {
        Long destinationId = request.getValue().getDestinationId();
        log.info("findHotel - WS destinationId = " + destinationId);

        FindHotelResponse response = objFactory.createFindHotelResponse();

        if (destinationId != null && destinationId > 0) {
            // find by destinationId
            List<Hotel> boList = hotelService.findByDestination(destinationId);
            for (Hotel bo : boList) {
                response.getResultList().add(new SchemaMapper().toWs(bo));
            }
        } else {
            // find all
            List<Hotel> boList = hotelService.findAll();
            for (Hotel bo : boList) {
                response.getResultList().add(new SchemaMapper().toWs(bo));
            }
        }

        return objFactory.createFindHotelResponse(response);
    }

    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = "createHotelRequest")
    @ResponsePayload
    public JAXBElement<CreateHotelResponse> createHotel(@RequestPayload JAXBElement<CreateHotelRequest> request)
            throws HptException {
        WsHotel wsHotel = request.getValue().getHotel();
        log.info("createHotel - WS hotel = " + wsHotel);

        Hotel hotel = hotelService.create(new SchemaMapper().toBo(wsHotel));

        CreateHotelResponse response = objFactory.createCreateHotelResponse();
        response.setHotelId(hotel.getId());

        return objFactory.createCreateHotelResponse(response);
    }

    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = "setHotelActiveRequest")
    @ResponsePayload
    public JAXBElement<SetHotelActiveResponse> setHotelActive(@RequestPayload JAXBElement<SetHotelActiveRequest> request)
            throws HptException {
        Long hotelId = request.getValue().getHotelId();
        boolean active = request.getValue().isActive();
        log.info("setHotelActive - WS hotelId = " + hotelId + ", active = " + active);

        hotelService.setActive(hotelId, active);
        return objFactory.createSetHotelActiveResponse(objFactory.createSetHotelActiveResponse());
    }

    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = "createPackageRequest")
    @ResponsePayload
    public JAXBElement<CreatePackageResponse> createPackage(@RequestPayload JAXBElement<CreatePackageRequest> request)
            throws HptException {
        WsPackage wsPackage = request.getValue().getPckg();
        log.info("createPackage - WS package = " + wsPackage);

        Package pckg = packageService.create(new SchemaMapper().toBo(wsPackage));

        CreatePackageResponse response = objFactory.createCreatePackageResponse();
        response.setPckgId(pckg.getId());

        return objFactory.createCreatePackageResponse(response);
    }

    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = "setPackagePriceRequest")
    @ResponsePayload
    public JAXBElement<SetPackagePriceResponse> setPackagePrice(
            @RequestPayload JAXBElement<SetPackagePriceRequest> request) throws HptException {
        Long packageId = request.getValue().getPackageId();
        double price = request.getValue().getPrice();
        log.info("setPackagePrice - WS packageId = " + packageId + ", price = " + price);

        packageService.setPrice(packageId, price);
        return objFactory.createSetPackagePriceResponse(objFactory.createSetPackagePriceResponse());
    }

    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = "setPackageActiveRequest")
    @ResponsePayload
    public JAXBElement<SetPackageActiveResponse> setPackageActive(
            @RequestPayload JAXBElement<SetPackageActiveRequest> request) throws HptException {
        Long packageId = request.getValue().getPackageId();
        boolean active = request.getValue().isActive();
        log.info("setPackageActive - WS packageId = " + packageId + ", active = " + active);

        packageService.setActive(packageId, active);
        return objFactory.createSetPackageActiveResponse(objFactory.createSetPackageActiveResponse());
    }

    @PayloadRoot(namespace = TARGET_NAMESPACE, localPart = "destinationsNoActiveHotelsRequest")
    @ResponsePayload
    public JAXBElement<DestinationsNoActiveHotelsResponse> destinationsNoActiveHotels(
            @RequestPayload JAXBElement<DestinationsNoActiveHotelsRequest> request) {
        log.info("destinationsNoActiveHotels - WS");

        DestinationsNoActiveHotelsResponse response = objFactory.createDestinationsNoActiveHotelsResponse();

        List<Destination> boList = destinationService.findAllWithNoActiveHotel();
        for (Destination bo : boList) {
            response.getResultList().add(new SchemaMapper().toWs(bo));
        }

        return objFactory.createDestinationsNoActiveHotelsResponse(response);
    }
}
