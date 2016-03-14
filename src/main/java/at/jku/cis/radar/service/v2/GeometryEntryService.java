package at.jku.cis.radar.service.v2;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.dao.GeometryEntryDao;
import at.jku.cis.radar.model.v2.GeometryEntry;
import at.jku.cis.radar.model.v2.GeometryEvolutionEntry;
import at.jku.cis.radar.model.v2.GeometryStatus;

@ApplicationScoped
public class GeometryEntryService implements Serializable {

    @Inject
    private GeometryEntryDao geometryEntryDao;

    @Transactional
    public GeometryEntry create(GeometryEvolutionEntry geometryEvolutionEntry, Geometry geometry, GeometryStatus status) {
        GeometryEntry geometryEntry = new GeometryEntry();
        geometryEntry.setGeometry(geometry);
        geometryEntry.setStatus(status);
        geometryEntry.setGeometryEvolutionEntry(geometryEvolutionEntry);
        geometryEntry.setDate(DateTime.now().toDate());
        return geometryEntryDao.create(geometryEntry);
    }
}
