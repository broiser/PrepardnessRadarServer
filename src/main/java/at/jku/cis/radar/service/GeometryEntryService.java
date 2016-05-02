package at.jku.cis.radar.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;

import at.jku.cis.radar.dao.GeometryEntryDao;
import at.jku.cis.radar.model.GeometryEntry;
import at.jku.cis.radar.model.GeometryStatus;

@ApplicationScoped
public class GeometryEntryService implements Serializable {

    @Inject
    private GeometryEntryDao geometryEntryDao;

    @Transactional
    public GeometryEntry create(GeometryStatus geometryStatus, Geometry geometry) {
        GeometryEntry geometryEntry = new GeometryEntry();
        geometryEntry.setStatus(geometryStatus);
        geometryEntry.setGeometry(geometry);
        geometryEntry.setDate(DateTime.now().toDate());
        return geometryEntryDao.create(geometryEntry);
    }
}
