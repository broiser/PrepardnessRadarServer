package at.jku.cis.radar.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQuery(name = FeatureEvoluation.FIND_BETWEEN_BY_EVENT, query = "SELECT fe FROM FeatureEvoluation fe WHERE fe.event.id = :eventId AND fe.date BETWEEN :fromDate AND :toDate ORDER BY fe.date DESC")
public class FeatureEvoluation extends BaseEntity {
    public static final String FIND_BETWEEN_BY_EVENT = "FeatueEvoluation.findBetweenByEvent";

    @OneToOne
    private Event event;
    @OneToOne
    private Feature feature;
    @Temporal(TemporalType.TIMESTAMP)
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
