package at.jku.cis.radar.rest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.joda.time.Period;

import at.jku.cis.radar.dao.EventDao;
import at.jku.cis.radar.modelv2.Event;
import at.jku.cis.radar.modelv2.FeatureEntry;
import at.jku.cis.radar.modelv2.GeometryEvolutionEntry;
import at.jku.cis.radar.service.EventService;
import at.jku.cis.radar.service.FeatureEntryService;
import at.jku.cis.radar.service.FeatureEvolutionService;
import at.jku.cis.radar.service.GeometryEntryService;
import at.jku.cis.radar.service.GeometryEvolutionEntryService;
import at.jku.cis.radar.transformer.FeatureEvolution2GroupTransformer;

@Path("geometryEvolution")
public class GeometryEvolutionEntryRestService extends RestService {

	@Inject
	private GeometryEvolutionEntryService geometryEvolutionEntryService;

	@Inject
	private FeatureEntryService featureEntryService;

	@Inject
	private GeometryEntryService geometryEntryService;

	@Inject
	private EventService eventService;

	@Inject
	private EventDao eventDao;

	@Inject
	private FeatureEvolutionService featureEvolutionService;
	@Inject
	private FeatureEvolution2GroupTransformer featureEvolution2GroupTransformer;

	@GET
	@Path("byEvent/{eventId}")
	public Response getGeometryEvolution(@PathParam("eventId") long eventId) {
		long toDate = DateTime.now().getMillis();
		long fromDate = DateTime.now().minusDays(1).getMillis();
		return getGeometryEvolution(eventId, fromDate, toDate);
	}

	@GET
	@Path("byEvent/{eventId}/{from}/{to}")
	public Response getGeometryEvolution(@PathParam("eventId") long eventId, @PathParam("from") long from,
			@PathParam("to") long to) {
		DateTime toDate = new DateTime(to);
		DateTime fromDate = new DateTime(from);

		//List<GeometryEvolutionEntry> geometryEvolutionEntries = geometryEvolutionEntryService
		//		.getGeometryEvolution(eventId, fromDate, toDate);

		/*
		 * GeometryEntry g = new GeometryEntry(); g.setDate(new Date());
		 * g.setGeometry(new GeometryFactory().createPoint(new
		 * Coordinate(1,1)));
		 * g.setGeometryEvolutionEntry(geometryEvolutionEntries.get(0));
		 * g.setStatus(GeometryStatus.CREATED); geometryEntryService.create(g);
		 * 
		 * GeometryEvolutionEntry ge = new GeometryEvolutionEntry();
		 * ge.setDate(new Date());
		 * ge.setFeatureEntry(geometryEvolutionEntries.get(0).getFeatureEntry())
		 * ;
		 * 
		 * geometryEvolutionEntryService.save(ge);
		 */
		/*
		FeatureEntry fe = new FeatureEntry();
		fe.setEvent(eventService.findById(eventId));
		fe.setFeatureGroup(13);
		fe.setGeometryEvolutionEntries(new ArrayList<GeometryEvolutionEntry>());

		featureEntryService.save(fe);
*/
		Event e = new Event();
		e.setColor(Color.BLACK.getRGB());
		e.setName("TestEvent__asdf___");
		e.setValidationPeriod(new Period().plusDays(4));
		e.setVisible(true);

		eventService.save(e);
		/*
		 * for (GeometryEvolutionEntry entry : geometryEvolutionEntries) {
		 * List<GeometryEntry> list =
		 * geometryEntryService.getGeometryEntry(entry.getId());
		 * entry.setGeometries(list); }
		 */
		// List<FeatureEntry> featureEntries =
		// featureEntryService.getFeatureEntry(eventId);
		// TODO
		// TODO
		return null; // Response.ok(buildGeoJsonFeatureCollection(geometryEvolutionEntries,
						// featureEntries)).build();
	}

}
