package at.jku.cis.radar.rest;

import static at.jku.cis.radar.geojson.GeoJsonObject.STATUS;

import java.util.ArrayList;
import java.util.Collection;
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

import at.jku.cis.radar.annotations.CurrentAccount;
import at.jku.cis.radar.annotations.Secured;
import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.geojson.GeoJsonFeatureCollection;
import at.jku.cis.radar.model.Account;
import at.jku.cis.radar.model.FeatureEntry;
import at.jku.cis.radar.model.GeometryStatus;
import at.jku.cis.radar.service.FeatureEntryService;
import at.jku.cis.radar.transformer.FeatureEntryGeoJsonFeatureTransformer;

@Secured
@Path("features")
public class FeaturesRestService extends RestService {

	@Inject
	@CurrentAccount
	private Account account;
	@Inject
	private FeatureEntryService featureEntryService;
	@Inject
	private FeatureEntryGeoJsonFeatureTransformer featureEntryGeoJsonFeatureTransformer;

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

		List<FeatureEntry> featureEntries = featureEntryService.findAllByEvent(eventId, fromDate, toDate);
		return Response.ok(buildGeoJsonFeatureCollection(featureEntries)).build();
	}

	@PUT
	@Path("{eventId}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response editFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
		FeatureEntry featureEntry = editFeatureEntry(geoJsonFeature);
		return Response.ok(featureEntry.getFeatureGroup()).build();
	}

	@POST
	@Path("{eventId}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response createFeature(@PathParam("eventId") long eventId, GeoJsonFeature geoJsonFeature) {
		FeatureEntry featureEntry;
		if (geoJsonFeature.getFeatureGroup() < 1) {
			featureEntry = createFeatureEntry(eventId,
					account.getUsername(), geoJsonFeature);
		} else {
			featureEntry = evolveFeatureEntry(account.getUsername(), geoJsonFeature);
		}
		return Response.ok(featureEntry.getFeatureGroup()).build();
	}

	private GeoJsonFeatureCollection buildGeoJsonFeatureCollection(List<FeatureEntry> featureEntries) {
		Collection<GeoJsonFeature> geoJsonFeatures = CollectionUtils.collect(featureEntries,
				featureEntryGeoJsonFeatureTransformer);
		return new GeoJsonFeatureCollection(new ArrayList<GeoJsonFeature>(geoJsonFeatures));
	}

	private FeatureEntry createFeatureEntry(long eventId, String username, GeoJsonFeature geoJsonFeature) {
		GeometryStatus geometryStatus = determineGeometryStatus(determineStatus(geoJsonFeature));
		return featureEntryService.create(eventId, username, geometryStatus, geoJsonFeature.getGeometry());
	}

	private FeatureEntry editFeatureEntry(GeoJsonFeature geoJsonFeature) {
		long featureGroup = geoJsonFeature.getFeatureGroup();
		GeometryStatus geometryStatus = determineGeometryStatus(determineStatus(geoJsonFeature));
		return featureEntryService.edit(featureGroup, geometryStatus, geoJsonFeature.getGeometry());
	}

	private FeatureEntry evolveFeatureEntry(String username, GeoJsonFeature geoJsonFeature) {
		long featureGroup = geoJsonFeature.getFeatureGroup();
		Map<String, Object> properties = geoJsonFeature.getProperties();
		GeometryStatus geometryStatus = determineGeometryStatus((String) properties.get(STATUS));
		return featureEntryService.evolve(featureGroup, username, geometryStatus, geoJsonFeature.getGeometry());
	}

	private GeometryStatus determineGeometryStatus(String value) {
		if (value.equals("erased")) {

			return GeometryStatus.ERASED;
		} else {
			return GeometryStatus.ERASED;
		}
		// return StringUtils.isEmpty(value) ? GeometryStatus.CREATED :
		// GeometryStatus.valueOf(value);
	}

	private String determineStatus(GeoJsonFeature geoJsonFeature) {
		return (String) geoJsonFeature.getProperties().get(STATUS);
	}
}