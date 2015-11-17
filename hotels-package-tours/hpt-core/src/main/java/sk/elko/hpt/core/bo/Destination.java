package sk.elko.hpt.core.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * TODO destination improvement: do not use 'name' as unique and not updatable identification<br />
 * TODO destination improvement: add some new unique attribute for identification (e.g. 'code')<br />
 */
@Entity
public class Destination {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, updatable = false, nullable = false)
    private String name;

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

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Destination [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", lowestPrice=");
        builder.append(lowestPrice);
        builder.append("]");
        return builder.toString();
    }

}
