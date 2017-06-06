package sk.elko.hpt.core.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * TODO destination improvement: add some unique attribute for identification (e.g. 'code')<br />
 */
@Entity
public class Package {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = false, updatable = false, nullable = false)
    // @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private LocalDateTime arrival;

    @Column(unique = false, updatable = false, nullable = false)
    // @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private LocalDateTime departure;

    @Column(unique = false, updatable = true, nullable = false)
    private double price;

    @Column(unique = false, updatable = true, nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotelId", nullable = false)
    private Hotel hotel;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Package [id=");
        builder.append(id);
        builder.append(", arrival=");
        builder.append(arrival);
        builder.append(", departure=");
        builder.append(departure);
        builder.append(", price=");
        builder.append(price);
        builder.append(", active=");
        builder.append(active);
        builder.append(", hotel=");
        builder.append(hotel);
        builder.append("]");
        return builder.toString();
    }

}
