package at.jku.cis.radar.service.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.dao.GeometryEvolutionEntryDao;
import at.jku.cis.radar.model.v2.FeatureEntry;
import at.jku.cis.radar.model.v2.GeometryEntry;
import at.jku.cis.radar.model.v2.GeometryEvolutionEntry;
import at.jku.cis.radar.model.v2.GeometryStatus;

@ApplicationScoped
public class GeometryEvolutionEntryService implements Serializable {

    @Inject
    private GeometryEntryService geometryEntryService;
    @Inject
    private GeometryEvolutionEntryDao geometryEvolutionEntryDao;

    @Transactional
    public GeometryEvolutionEntry create(FeatureEntry featureEntry, Geometry geometry, Map<String, Object> properties) {
        GeometryStatus geometryStatus = GeometryStatus.valueOf((String) properties.remove("STATUS"));
        GeometryEvolutionEntry geometryEvolutionEntry = new GeometryEvolutionEntry();
        geometryEvolutionEntry.setDate(DateTime.now().toDate());
        geometryEvolutionEntry.setProperties(properties);
        geometryEvolutionEntry.setFeatureEntry(featureEntry);
        geometryEvolutionEntry.setGeometryEntries(new ArrayList<GeometryEntry>());
        geometryEvolutionEntry = geometryEvolutionEntryDao.create(geometryEvolutionEntry);
        geometryEvolutionEntry.getGeometryEntries()
                .add(createGeometryEntry(geometryEvolutionEntry, geometry, geometryStatus));
        return geometryEvolutionEntryDao.save(geometryEvolutionEntry);
    }

    @Transactional
    public GeometryEvolutionEntry edit(long id, Geometry geometry, Map<String, Object> properties) {
        GeometryStatus geometryStatus = GeometryStatus.valueOf((String) properties.remove("STATUS"));
        GeometryEvolutionEntry geometryEvolutionEntry = geometryEvolutionEntryDao.findById(id);
        geometryEvolutionEntry.getGeometryEntries()
                .add(createGeometryEntry(geometryEvolutionEntry, geometry, geometryStatus));
        geometryEvolutionEntry.getProperties().putAll(properties);
        return geometryEvolutionEntryDao.save(geometryEvolutionEntry);
    }

    private GeometryEntry createGeometryEntry(GeometryEvolutionEntry geometryEvolutionEntry, Geometry geometry,
            GeometryStatus geometryStatus) {
        return geometryEntryService.create(geometryEvolutionEntry, geometry, geometryStatus);
    }
}
