package at.jku.cis.radar.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.GeometryCollection;

import at.jku.cis.radar.geojson.GeoJsonFeatureCollection;
import at.jku.cis.radar.geojson.GeoJsonFeatureEvolution;
import at.jku.cis.radar.geometry.GeometryUtils;
import at.jku.cis.radar.model.FeatureEvolution;
import at.jku.cis.radar.service.FeatureEvolutionPreparer;
import at.jku.cis.radar.service.FeatureEvolutionService;

@Path("featuresEvolution")
public class FeatureEvolutionRestService extends RestService {

	@Inject
	private FeatureEvolutionService featureEvolutionService;
	@Inject
	private FeatureEvolutionPreparer featureEvolutionPreparer;

	@GET
	@Path("{eventId}/{featureGroup}")
	public Response getFeatureEvolution(@PathParam("eventId") long eventId,
			@PathParam("featureGroup") long featureGroup) {
		DateTime dateTime = DateTime.now();
		long to = dateTime.getMillis();
		long from = dateTime.minusDays(1).getMillis();
		return getFeatureEvolution(eventId, featureGroup, from, to);
	}

	@GET
	@Path("{eventId}/{featureGroup}/{from}/{to}")
	public Response getFeatureEvolution(@PathParam("eventId") long eventId,
			@PathParam("featureGroup") long featureGroup, @PathParam("from") long from, @PathParam("to") long to) {
		List<FeatureEvolution> featureEvolutions = findBetween(eventId, featureGroup, from, to);
		List<FeatureEvolution> partitionedEvolutions = partitionAndCombine(featureEvolutions);
 		List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = featureEvolutionPreparer
				.prepareEvolution(partitionedEvolutions);

		return Response.ok(new GeoJsonFeatureCollection(geoJsonFeatureEvolutions)).build();
	}

	private List<FeatureEvolution> findBetween(long eventId, long featureGroup, long from, long to) {
		DateTime toDate = new DateTime(to);
		DateTime fromDate = new DateTime(from);
		return featureEvolutionService.findBetween(eventId, featureGroup, fromDate, toDate);
	}

	private List<FeatureEvolution> partitionAndCombine(List<FeatureEvolution> featureEvolutions) {
		Map<Long, FeatureEvolution> partitionedFeatureEvolutions = new HashMap<>();

		for (FeatureEvolution featureEvolution : featureEvolutions) {
			if (!(partitionedFeatureEvolutions.containsKey(featureEvolution.getFeatureGroup()))) {
				partitionedFeatureEvolutions.put(featureEvolution.getFeatureGroup(), featureEvolution);
			} else {
				FeatureEvolution currentFeatureEvolution = partitionedFeatureEvolutions
						.get(featureEvolution.getFeatureGroup());
				GeometryCollection c = (GeometryCollection) currentFeatureEvolution.getGeometry();
				if (featureEvolution.getStatus() == 'c') {
					currentFeatureEvolution
							.setGeometry(GeometryUtils.union((GeometryCollection) currentFeatureEvolution.getGeometry(),
									(GeometryCollection) featureEvolution.getGeometry()));
				} else {
					currentFeatureEvolution.setGeometry(
							GeometryUtils.difference((GeometryCollection) currentFeatureEvolution.getGeometry(),
									featureEvolution.getGeometry().getGeometryN(0)));
				}
			}
		}
		return new ArrayList<FeatureEvolution>(partitionedFeatureEvolutions.values());
	}
}
