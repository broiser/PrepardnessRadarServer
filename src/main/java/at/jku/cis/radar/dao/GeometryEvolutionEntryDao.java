package at.jku.cis.radar.dao;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import at.jku.cis.radar.model.GeometryEvolutionEntry;

@ApplicationScoped
public class GeometryEvolutionEntryDao extends AbstractDao<GeometryEvolutionEntry> {

    public GeometryEvolutionEntryDao() {
        super(GeometryEvolutionEntry.class);
    }

    public List<GeometryEvolutionEntry> findEvolutionsByFeatureGroup(long featureGroup, Date fromDate, Date toDate) {
        return createNamedQuery(GeometryEvolutionEntry.FIND_EVOLUTIONS_BY_FEATURE_GROUP)
                .setParameter("featureGroup", featureGroup).setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate).getResultList();

    }

    public List<GeometryEvolutionEntry> findEvolutionsByEvent(long eventId, Date fromDate, Date toDate) {
        return createNamedQuery(GeometryEvolutionEntry.FIND_EVOLUTIONS_BY_EVENT).setParameter("eventId", eventId)
                .setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
    }

}
