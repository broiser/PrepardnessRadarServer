package at.jku.cis.radar.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.GeometryCollection;

import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.geojson.GeoJsonFeatureCollection;
import at.jku.cis.radar.geometry.GeometryUtils;
import at.jku.cis.radar.model.FeatureEvolution;
import at.jku.cis.radar.service.FeatureEvolutionService;
import at.jku.cis.radar.transformer.FeatureEvolution2GeoJsonFeatureTransformer;
import at.jku.cis.radar.transformer.FeatureEvolution2GroupTransformer;

@Path("features")
public class FeatureRestService extends RestService {

	@Inject
	private FeatureEvolutionService featureEvolutionService;
	@Inject
	private FeatureEvolution2GeoJsonFeatureTransformer featureEvolution2GeoJsonFeatureTransformer;
	@Inject
	private FeatureEvolution2GroupTransformer featureEvolution2GroupTransformer;

	@GET
	@Path("{eventId}")
	public Response getFeatures(@PathParam("eventId") long eventId) {
		long currentMillis = DateTime.now().getMillis();
		return getFeatures(eventId, currentMillis, currentMillis);
	}

	@GET
	@Path("{eventId}/{from}/{to}")
	public Response getFeatures(@PathParam("eventId") long eventId, @PathParam("from") long from,
			@PathParam("to") long to) {
		DateTime toDate = new DateTime(to);
		DateTime fromDate = new DateTime(from);
		List<FeatureEvolution> featureEvolutions = featureEvolutionService.findNewestByEvent(eventId, fromDate, toDate);

		Map<Long, FeatureEvolution> combinedFeatureEvolutions = partitionAndCombine(featureEvolutions);
		return Response.ok(buildGeoJsonFeatureCollection(combinedFeatureEvolutions.values())).build();
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

	private Map<Long, FeatureEvolution> partitionAndCombine(List<FeatureEvolution> featureEvolutions) {
		Map<Long, FeatureEvolution> partitionedFeatureEvolutions = new HashMap<>();
		for (FeatureEvolution featureEvolution : featureEvolutions) {
			if (!(partitionedFeatureEvolutions.containsKey(featureEvolution.getFeatureGroup()))) {
				partitionedFeatureEvolutions.put(featureEvolution.getFeatureGroup(), featureEvolution);
			} else {
				FeatureEvolution currentFeatureEvolution = partitionedFeatureEvolutions
						.get(featureEvolution.getFeatureGroup());
				GeometryCollection c = (GeometryCollection) currentFeatureEvolution.getGeometry();
				currentFeatureEvolution
						.setGeometry(GeometryUtils.union((GeometryCollection) currentFeatureEvolution.getGeometry(),
								(GeometryCollection) featureEvolution.getGeometry()));
			}
		}
		return partitionedFeatureEvolutions;
	}

	private GeoJsonFeatureCollection buildGeoJsonFeatureCollection(Collection<FeatureEvolution> featureEvolutions) {
		Collection<GeoJsonFeature> geoJsonFeatures = CollectionUtils.collect(featureEvolutions,
				featureEvolution2GeoJsonFeatureTransformer);
		return new GeoJsonFeatureCollection(new ArrayList<>(geoJsonFeatures));
	}
}
