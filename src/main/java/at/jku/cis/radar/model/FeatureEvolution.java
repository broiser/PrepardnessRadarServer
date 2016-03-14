package at.jku.cis.radar.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.model.v2.BaseEntity;
import at.jku.cis.radar.model.v2.Event;

//@Entity
@NamedQueries({
        @NamedQuery(name = FeatureEvolution.FIND_NEWEST_BY_FEATURE_GROUP, query = "SELECT fe FROM FeatureEvolution fe where fe.event.id = :eventId AND fe.featureGroup = :featureGroup ORDER By fe.date DESC"),
        @NamedQuery(name = FeatureEvolution.FIND_BETWEEN_BY_EVENT_AND_FEATURE_GROUP, query = "SELECT fe FROM FeatureEvolution fe WHERE fe.event.id = :eventId AND fe.featureGroup = :featureGroup AND fe.date BETWEEN :fromDate AND :toDate ORDER BY fe.date") })
@NamedNativeQuery(name = FeatureEvolution.FIND_NEWEST_BY_EVENT, query = "SELECT fe.id, fe.date, fe.featuregroup, fe.geometry, fe.event_id, fe.status FROM featureevolution fe WHERE fe.event_id = :eventId AND fe.date BETWEEN :fromDate AND :toDate order by fe.date", resultClass = FeatureEvolution.class)
public class FeatureEvolution extends BaseEntity {

    public static final String FIND_NEWEST_BY_FEATURE_GROUP = "FeatureEvolution.findNewestByFeatureGroup";
    public static final String FIND_BETWEEN_BY_EVENT_AND_FEATURE_GROUP = "FeatureEvolution.findBetweenByEventAndFeatureGroup";
    public static final String FIND_NEWEST_BY_EVENT = "FeatureEvolution.findNewestByEvent";

    @OneToOne
    private Event event;
    private long featureGroup;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Type(type = "org.hibernate.spatial.GeometryType")
    @Column(name = "geometry", columnDefinition = "Geometry")
    private Geometry geometry;
    private char status;
    @Transient
    private Map<String, Object> properties = new HashMap<String, Object>();

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getFeatureGroup() {
        return featureGroup;
    }

    public void setFeatureGroup(long featureGroup) {
        this.featureGroup = featureGroup;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
}
