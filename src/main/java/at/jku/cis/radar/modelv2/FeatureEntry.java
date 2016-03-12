package at.jku.cis.radar.modelv2;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@NamedQuery(name = FeatureEntry.FIND_FEATURE_BY_FEATURE_GROUP, query = "SELECT fe FROM FeatureEntry fe where fe.featureGroup = :featureGroup")
@NamedNativeQuery(name = FeatureEntry.FIND_FEATURE_BY_EVENT, query = "SELECT fe.id, fe.featuregroup, fe.event_id  FROM FeatureEntry fe WHERE fe.event_id = :eventId ", resultClass = FeatureEntry.class)

public class FeatureEntry extends BaseEntity {

	public static final String FIND_FEATURE_BY_FEATURE_GROUP = "FeatureEntry.findFeatureByFeatureGroup";
	public static final String FIND_FEATURE_BY_EVENT = "FeatureEntry.findFeatureByEvent";

	@OneToOne
	private Event event;
	private long featureGroup;

	@OneToMany(mappedBy = "featureEntry")
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
