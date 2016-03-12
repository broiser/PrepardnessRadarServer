package at.jku.cis.radar.modelv2;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
		@NamedQuery(name = GeometryEvolutionEntry.FIND_EVOLUTIONS_BY_FEATURE_GROUP, query = "SELECT ge FROM GeometryEvolutionEntry ge where ge.featureEntry.featureGroup = :featureGroup AND ge.date BETWEEN :fromDate AND :toDate ORDER By ge.date"),
		@NamedQuery(name = GeometryEvolutionEntry.FIND_EVOLUTIONS_BY_EVENT, query = "SELECT ge FROM GeometryEvolutionEntry ge WHERE ge.featureEntry.event.id = :eventId AND ge.date BETWEEN :fromDate AND :toDate order by ge.date") })

public class GeometryEvolutionEntry extends BaseEntity {
	public static final String FIND_EVOLUTIONS_BY_FEATURE_GROUP = "GeometryEvolutionEntry.findEvolutionsByFeatureGroup";
	public static final String FIND_EVOLUTIONS_BY_EVENT = "GeometryEvolutionEntry.findEvolutionsByEvent";

	@ManyToOne
	private FeatureEntry featureEntry;

	@OneToMany(mappedBy = "geometryEvolutionEntry")
	private List<GeometryEntry> geometries;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	public List<GeometryEntry> getGeometries() {
		return geometries;
	}

	public void setGeometries(List<GeometryEntry> geometries) {
		this.geometries = geometries;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public FeatureEntry getFeatureEntry() {
		return featureEntry;
	}

	public void setFeatureEntry(FeatureEntry featureEntry) {
		this.featureEntry = featureEntry;
	}

}
