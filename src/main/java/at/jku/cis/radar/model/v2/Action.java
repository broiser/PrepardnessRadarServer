package at.jku.cis.radar.model.v2;

import javax.persistence.Entity;

@Entity
public class Action extends BaseEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
