package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.dao.GeometryEvolutionEntryDao;
import at.jku.cis.radar.model.GeometryEntry;
import at.jku.cis.radar.model.GeometryEvolutionEntry;
import at.jku.cis.radar.model.GeometryStatus;

@ApplicationScoped
public class GeometryEvolutionEntryService implements Serializable {

    @Inject
    private AccountService accountService;
    @Inject
    private GeometryEntryService geometryEntryService;
    @Inject
    private GeometryEvolutionEntryDao geometryEvolutionEntryDao;

    @Transactional
    public GeometryEvolutionEntry create(String username, GeometryStatus geometryStatus, Geometry geometry) {
        GeometryEvolutionEntry geometryEvolutionEntry = new GeometryEvolutionEntry();
        geometryEvolutionEntry.setDate(DateTime.now().toDate());
        geometryEvolutionEntry.setAccount(accountService.findByUsername(username));
        geometryEvolutionEntry.setGeometryEntries(new ArrayList<GeometryEntry>());
        geometryEvolutionEntry = geometryEvolutionEntryDao.create(geometryEvolutionEntry);
        geometryEvolutionEntry.getGeometryEntries()
                .add(createGeometryEntry(geometryEvolutionEntry, geometry, geometryStatus));
        return geometryEvolutionEntryDao.save(geometryEvolutionEntry);
    }

    @Transactional
    public GeometryEvolutionEntry edit(long id, GeometryStatus geometryStatus, Geometry geometry) {
        GeometryEvolutionEntry geometryEvolutionEntry = geometryEvolutionEntryDao.findById(id);
        geometryEvolutionEntry.getGeometryEntries()
                .add(createGeometryEntry(geometryEvolutionEntry, geometry, geometryStatus));
        return geometryEvolutionEntryDao.save(geometryEvolutionEntry);
    }

    private GeometryEntry createGeometryEntry(GeometryEvolutionEntry geometryEvolutionEntry, Geometry geometry,
            GeometryStatus geometryStatus) {
        GeometryEntry geometryEntry = geometryEntryService.create(geometryStatus, geometry);
        geometryEntry.setGeometryEvolutionEntry(geometryEvolutionEntry);
        return geometryEntry;
    }
}
