package at.jku.cis.radar.model.v2;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

// @NamedQuery(name = GeometryEntry.FIND_GEOMETRIES_BY_EVOLUTION_ID, query =
// "select ge from GeometryEntry ge where ge.geometryEvolutionEntry.id =
// :evolutionId order by ge.date")
@Entity
public class GeometryEntry extends BaseEntity {

	public static final String FIND_GEOMETRIES_BY_EVOLUTION_ID = "GeometryEntry.findGeometriesByEvolutionId";

	@ManyToOne
	private GeometryEvolutionEntry geometryEvolutionEntry;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Type(type = "org.hibernate.spatial.GeometryType")
	@Column(name = "geometry", columnDefinition = "Geometry")
	private Geometry geometry;

	@Enumerated(EnumType.STRING)
	private GeometryStatus status;

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

	public GeometryStatus getStatus() {
		return status;
	}

	public void setStatus(GeometryStatus status) {
		this.status = status;
	}

	public GeometryEvolutionEntry getGeometryEvolutionEntry() {
		return geometryEvolutionEntry;
	}

	public void setGeometryEvolutionEntry(GeometryEvolutionEntry geometryEvolutionEntry) {
		this.geometryEvolutionEntry = geometryEvolutionEntry;
	}

}
