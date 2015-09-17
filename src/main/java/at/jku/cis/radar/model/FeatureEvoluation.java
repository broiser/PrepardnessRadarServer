package at.jku.cis.radar.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQuery(name = FeatureEvoluation.FIND_LATEST_BY_EVENT, query = "SELECT fe FROM FeatureEvoluation fe WHERE fe.event.id = :eventId ORDER BY fe.date DESC")
public class FeatureEvoluation extends BaseEntity {
    public static final String FIND_LATEST_BY_EVENT = "FeatureEvoluation.findLatestByEvent";

    @OneToOne
    private Event event;
    @OneToOne
    private Feature feature;
    private Date date;

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
