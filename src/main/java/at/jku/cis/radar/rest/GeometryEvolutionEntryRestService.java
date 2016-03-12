package at.jku.cis.radar.rest;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.postgis.Polygon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import at.jku.cis.radar.builder.GeoJsonFeatureBuilder;
import at.jku.cis.radar.dao.EventDao;
import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.geojson.GeoJsonFeatureCollection;
import at.jku.cis.radar.model.FeatureEvolution;
import at.jku.cis.radar.modelv2.FeatureEntry;
import at.jku.cis.radar.modelv2.GeometryEntry;
import at.jku.cis.radar.modelv2.GeometryEvolutionEntry;
import at.jku.cis.radar.modelv2.GeometryStatus;
import at.jku.cis.radar.service.EventService;
import at.jku.cis.radar.service.FeatureEntryService;
import at.jku.cis.radar.service.FeatureEvolutionService;
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
	@Transactional
	public Response getGeometryEvolution(@PathParam("eventId") long eventId, @PathParam("from") long from,
			@PathParam("to") long to) {
		DateTime toDate = new DateTime(to);
		DateTime fromDate = new DateTime(from);

		List<GeometryEvolutionEntry> geometryEvolutionEntries = geometryEvolutionEntryService
				.getGeometryEvolution(eventId, fromDate, toDate);

		GeometryEntry g = new GeometryEntry();
		g.setDate(new Date());
		g.setGeometry(new GeometryFactory().createPoint(new Coordinate(1,1)));
		g.setGeometryEvolutionEntry(geometryEvolutionEntries.get(0));
		g.setStatus(GeometryStatus.CREATED);
		geometryEntryService.create(g);
		
		
	
		
		/*
		for (GeometryEvolutionEntry entry : geometryEvolutionEntries) {
			List<GeometryEntry> list = geometryEntryService.getGeometryEntry(entry.getId());
			entry.setGeometries(list);
		}
		*/
		// List<FeatureEntry> featureEntries =
		// featureEntryService.getFeatureEntry(eventId);
		// TODO
		// TODO
		return null; // Response.ok(buildGeoJsonFeatureCollection(geometryEvolutionEntries,
						// featureEntries)).build();
	}

	@PUT
	@Path("{eventId}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response saveFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
		FeatureEvolution featureEvolution = featureEvolutionService.save(eventId, geoJsonFeature);
		return Response.ok(featureEvolution2GroupTransformer.transform(featureEvolution)).build();
	}

	@POST
	@Path("{eventId}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response createFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
		FeatureEvolution featureEvolution = featureEvolutionService.create(eventId, geoJsonFeature);
		return Response.ok(featureEvolution2GroupTransformer.transform(featureEvolution)).build();
	}

	private GeoJsonFeatureCollection buildGeoJsonFeatureCollection(
			List<GeometryEvolutionEntry> geometryEvolutionEntries, List<FeatureEntry> featureEntries) {
		GeoJsonFeatureBuilder geoJsonFeatureBuilder = null;

		for (FeatureEntry featureEntry : featureEntries) {
			geoJsonFeatureBuilder = new GeoJsonFeatureBuilder();

			// geoJsonFeatureBuilder.
		}

		return null;

	}

}
