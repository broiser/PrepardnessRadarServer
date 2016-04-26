package at.jku.cis.radar.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class GeometryEvolutionContent extends BaseEntity {

    private String title;
    
    @Column(length = 1024)
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
