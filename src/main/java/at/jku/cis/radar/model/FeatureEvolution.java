package at.jku.cis.radar.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
        @NamedQuery(name = FeatureEvolution.FIND_NEWEST_BY_FEATURE_GROUP, query = "SELECT fe FROM FeatureEvolution fe where fe.event.id = :eventId AND fe.feature.featureGroup = :featureGroup ORDER By fe.date DESC"),
        @NamedQuery(name = FeatureEvolution.FIND_BETWEEN_BY_EVENT, query = "SELECT fe FROM FeatureEvolution fe WHERE fe.event.id = :eventId AND fe.date BETWEEN :fromDate AND :toDate ORDER BY fe.date"),
        @NamedQuery(name = FeatureEvolution.FIND_BETWEEN_BY_EVENT_AND_FEATURE_GROUP, query = "SELECT fe FROM FeatureEvolution fe WHERE fe.event.id = :eventId AND fe.feature.featureGroup = :featureGroup AND fe.date BETWEEN :fromDate AND :toDate ORDER BY fe.date") })
public class FeatureEvolution extends BaseEntity {
    public static final String FIND_NEWEST_BY_FEATURE_GROUP = "FeatureEvolution.findNewestByFeatureGroup";
    public static final String FIND_BETWEEN_BY_EVENT = "FeatueEvolution.findBetweenByEvent";
    public static final String FIND_BETWEEN_BY_EVENT_AND_FEATURE_GROUP = "FeatureEvolution.findBetweenByEventAndFeatureGroup";

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
