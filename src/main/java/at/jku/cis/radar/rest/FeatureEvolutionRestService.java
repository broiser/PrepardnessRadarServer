package at.jku.cis.radar.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import at.jku.cis.radar.model.FeatureEvolution;
import at.jku.cis.radar.service.FeatureEvolutionPreparer;
import at.jku.cis.radar.service.FeatureEvolutionService;

@Path("featureEvolution")
public class FeatureEvolutionRestService extends RestService {

    @Inject
    private FeatureEvolutionService featureEvolutionService;
    @Inject
    private FeatureEvolutionPreparer featureEvolutionPreparer;

    @GET
    @Path("{eventId}/{featureGroup}")
    public Response getFeatureEvolution(@PathParam("eventId") long eventId,
            @PathParam("featureGroup") long featureGroup) {
        DateTime dateTime = DateTime.now().withTimeAtStartOfDay();
        long from = dateTime.getMillis();
        long to = dateTime.plusDays(1).getMillis();
        return getFeatureEvolution(eventId, featureGroup, from, to);
    }

    @GET
    @Path("{eventId}/{featureGroup}/{from}/{to}")
    public Response getFeatureEvolution(@PathParam("eventId") long eventId,
            @PathParam("featureGroup") long featureGroup, @PathParam("from") long from, @PathParam("to") long to) {
        List<FeatureEvolution> featureEvolutions = findBetween(eventId, featureGroup, from, to);
        return Response.ok(featureEvolutionPreparer.prepareEvolution(featureEvolutions)).build();
    }

    private List<FeatureEvolution> findBetween(long eventId, long featureGroup, long from, long to) {
        DateTime toDate = new DateTime(to);
        DateTime fromDate = new DateTime(from);
        return featureEvolutionService.findBetween(eventId, featureGroup, fromDate, toDate);
    }
}
