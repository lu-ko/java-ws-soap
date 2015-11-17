package sk.elko.hpt.core.bo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * TODO hotel improvement: do not use 'name' as unique and not updatable identification<br />
 * TODO hotel improvement: add some new unique attribute for identification (e.g. 'code')<br />
 */
@Entity
public class Hotel {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String name;

    @Column(unique = false, updatable = true, nullable = false)
    private boolean active;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hotel")
    private Collection<Package> packages = new ArrayList<Package>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destinationId", nullable = false)
    private Destination destination;

    @Column(unique = false, insertable = false, updatable = true, nullable = true)
    private Double lowestPrice;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Collection<Package> getPackages() {
        if (packages == null) {
            packages = new ArrayList<Package>();
        }
        return packages;
    }

    public void setPackages(Collection<Package> packages) {
        this.packages = packages;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Hotel [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", active=");
        builder.append(active);
        builder.append(", destination=");
        builder.append(destination);
        builder.append(", #packages=");
        builder.append(packages == null ? null : packages.size());
        builder.append(", lowestPrice=");
        builder.append(lowestPrice);
        builder.append("]");
        return builder.toString();
    }

}
