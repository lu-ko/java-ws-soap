package sk.elko.hpt.core.controller.form;

import org.joda.time.DateTime;

public class DbStateForm {

    private int destinations;
    private int hotels;
    private int packages;
    private DateTime datetime;

    public int getDestinations() {
        return destinations;
    }

    public void setDestinations(int destinations) {
        this.destinations = destinations;
    }

    public int getHotels() {
        return hotels;
    }

    public void setHotels(int hotels) {
        this.hotels = hotels;
    }

    public int getPackages() {
        return packages;
    }

    public void setPackages(int packages) {
        this.packages = packages;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(DateTime datetime) {
        this.datetime = datetime;
    }

}
