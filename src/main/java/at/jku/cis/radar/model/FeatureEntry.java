package at.jku.cis.radar.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@NamedQueries({
        @NamedQuery(name = FeatureEntry.FIND_FEATURE_BY_FEATURE_GROUP, query = "SELECT fe FROM FeatureEntry fe where fe.featureGroup = :featureGroup"),
        @NamedQuery(name = FeatureEntry.FIND_ALL_BY_EVENT, query = "SELECT DISTINCT ge.featureEntry FROM GeometryEvolutionEntry ge WHERE ge.featureEntry.event.id = :eventId AND ge.date BETWEEN :fromDate AND :toDate") })
@NamedNativeQuery(name = FeatureEntry.FIND_FEATURE_BY_EVENT, query = "SELECT fe.id, fe.featuregroup, fe.event_id  FROM FeatureEntry fe WHERE fe.event_id = :eventId ", resultClass = FeatureEntry.class)
public class FeatureEntry extends BaseEntity {
    public static final String FIND_FEATURE_BY_FEATURE_GROUP = "FeatureEntry.findFeatureByFeatureGroup";
    public static final String FIND_FEATURE_BY_EVENT = "FeatureEntry.findFeatureByEvent";
    public static final String FIND_ALL_BY_EVENT = "FeatureEntry.findAllByEvent";

    @OneToOne
    private Event event;
    private long featureGroup;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "featureEntry", cascade = CascadeType.ALL)
    private List<GeometryEvolutionEntry> geometryEvolutionEntries;

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

    public List<GeometryEvolutionEntry> getGeometryEvolutionEntries() {
        return geometryEvolutionEntries;
    }

    public void setGeometryEvolutionEntries(List<GeometryEvolutionEntry> geometryEvolutionEntries) {
        this.geometryEvolutionEntries = geometryEvolutionEntries;
    }

}
