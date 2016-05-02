package at.jku.cis.radar.dao;

import javax.enterprise.context.ApplicationScoped;

import at.jku.cis.radar.model.GeometryEntry;

@ApplicationScoped
public class GeometryEntryDao extends AbstractDao<GeometryEntry> {

    public GeometryEntryDao() {
        super(GeometryEntry.class);
    }

}
