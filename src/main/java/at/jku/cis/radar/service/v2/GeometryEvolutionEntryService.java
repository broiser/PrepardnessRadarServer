package at.jku.cis.radar.service.v2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.dao.GeometryEvolutionEntryDao;
import at.jku.cis.radar.model.v2.GeometryEntry;
import at.jku.cis.radar.model.v2.GeometryEvolutionEntry;

@ApplicationScoped
public class GeometryEvolutionEntryService implements Serializable {

    @Inject
    private GeometryEntryService geometryEntryService;
    @Inject
    private GeometryEvolutionEntryDao geometryEvolutionEntryDao;

    @Transactional
    public GeometryEvolutionEntry create(Geometry geometry, Map<String, Object> properties) {
        GeometryEvolutionEntry geometryEvolutionEntry = new GeometryEvolutionEntry();
        geometryEvolutionEntry.setDate(DateTime.now().toDate());
        geometryEvolutionEntry.setProperties(properties);
        geometryEvolutionEntry.setGeometryEntries(Arrays.asList(createGeometryEntry(geometry)));
        return geometryEvolutionEntryDao.create(geometryEvolutionEntry);
    }

    private GeometryEntry createGeometryEntry(Geometry geometry) {
        return geometryEntryService.create(geometry);
    }
}
