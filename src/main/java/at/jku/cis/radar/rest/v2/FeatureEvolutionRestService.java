package at.jku.cis.radar.rest.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import at.jku.cis.radar.builder.GeoJsonFeatureBuilder;
import at.jku.cis.radar.geojson.GeoJsonFeature;
import at.jku.cis.radar.geojson.GeoJsonFeatureCollection;
import at.jku.cis.radar.geometry.GeometryService;
import at.jku.cis.radar.model.v2.FeatureEntry;
import at.jku.cis.radar.model.v2.GeometryEntry;
import at.jku.cis.radar.model.v2.GeometryEvolutionEntry;
import at.jku.cis.radar.service.v2.FeatureEntryService;
import at.jku.cis.radar.service.v2.GeometryEvolutionEntryService;

@Path("featureEvolution")
public class FeatureEvolutionRestService extends RestService {

	@Inject
	private FeatureEntryService featureEntryService;
	@Inject
	private GeometryEvolutionEntryService geometryEvolutionEntryService;
	@Inject
	private GeometryService geometryService;

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
		FeatureEntry featureEntry = featureEntryService.findByFeatureGroup(featureGroup);
		return Response.ok(createGeoJsonFeatureCollection(featureEntry, from, to)).build();
	}

	private GeoJsonFeatureCollection createGeoJsonFeatureCollection(FeatureEntry featureEntry, long from, long to) {
		SortedMap<Date, GeoJsonFeature> sortedMap= combineEditGeometries(featureEntry);
		GeoJsonFeatureCollection geoJsonFeatureCollection =  new GeoJsonFeatureCollection();
		geoJsonFeatureCollection.setFeatures(new ArrayList<GeoJsonFeature>(sortedMap.subMap(new Date(from), new Date(to)).values()));
		return geoJsonFeatureCollection;
	}

	private SortedMap<Date, GeoJsonFeature> combineEditGeometries(FeatureEntry featureEntry) {
		TreeMap<Date, GeoJsonFeature> geoJsonFeatureMap = new TreeMap<>();
		for (GeometryEvolutionEntry geometryEvolutionEntry : featureEntry.getGeometryEvolutionEntries()) {
			GeometryCollection geometryCollection = new GeometryFactory().createGeometryCollection(new Geometry[0]);
			for (GeometryEntry geometryEntry : geometryEvolutionEntry.getGeometryEntries()) {
				switch (geometryEntry.getStatus()) {
				case CREATED:
					geometryCollection = new GeometryService().union(geometryCollection,
							(GeometryCollection) geometryEntry.getGeometry());
					break;
				case ERASED:
					geometryCollection = new GeometryService().difference(geometryCollection,
							geometryEntry.getGeometry());
					break;
				}
				GeoJsonFeature geoJsonFeature = new GeoJsonFeatureBuilder().withFeatureGroup(featureEntry.getFeatureGroup())
						.withGeometry(geometryCollection).withProperties(new HashMap<String, Object>()).build();
				geoJsonFeatureMap.put(geometryEvolutionEntry.getDate(), geoJsonFeature);
			}
			
		}
		return geoJsonFeatureMap;
	}

}
