package at.jku.cis.radar.service;

import static at.jku.cis.radar.geojson.GeoJsonStatus.CREATED;
import static at.jku.cis.radar.geojson.GeoJsonStatus.EARSED;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import at.jku.cis.radar.builder.GeoJsonFeatureEvolutionBuilder;
import at.jku.cis.radar.geojson.GeoJsonFeatureEvolution;
import at.jku.cis.radar.geojson.GeoJsonStatus;
import at.jku.cis.radar.model.FeatureEvolution;

@ApplicationScoped
public class FeatureEvolutionPreparer implements Serializable {

    public List<GeoJsonFeatureEvolution> prepareEvolution(List<FeatureEvolution> featureEvolutions) {
        if (featureEvolutions.isEmpty()) {
            return Collections.emptyList();
        }
        return createFeatureEvolutions(featureEvolutions);
    }

    private List<GeoJsonFeatureEvolution> createFeatureEvolutions(List<FeatureEvolution> featureEvolutions) {
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = new ArrayList<>();

        for (int i = featureEvolutions.size() - 1; i >= 0; i--) {
            FeatureEvolution featureEvolution = featureEvolutions.get(i);
            geoJsonFeatureEvolutions = createFeatureEvolutions(geoJsonFeatureEvolutions, featureEvolution);
        }

        return geoJsonFeatureEvolutions;
    }

    private List<GeoJsonFeatureEvolution> createFeatureEvolutions(
            List<GeoJsonFeatureEvolution> currentGeoJsonFeatureEvolutions, FeatureEvolution featureEvolution) {
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = new ArrayList<>();

        Date date = featureEvolution.getDate();
        GeometryCollection geometryCollection = (GeometryCollection) featureEvolution.getGeometry();

        for (int n = 0; n < geometryCollection.getNumGeometries(); n++) {
            Geometry geometry = geometryCollection.getGeometryN(n);

            if (currentGeoJsonFeatureEvolutions.isEmpty()) {
                Map<String, Object> properties = featureEvolution.getProperties();
                GeoJsonFeatureEvolution geoJsonFeatureEvolution = buildGeoJsonFeatureEvolution(date,
                        featureEvolution.getFeatureGroup(), geometry, properties, CREATED);
                geoJsonFeatureEvolutions.add(geoJsonFeatureEvolution);
            } else {
                for (GeoJsonFeatureEvolution geoJsonFeatureEvolution : currentGeoJsonFeatureEvolutions) {
                    geoJsonFeatureEvolutions.addAll(intersectFeatureEvolution(geoJsonFeatureEvolution, geometry, date));
                }
            }
        }
        return geoJsonFeatureEvolutions;
    }

    private List<GeoJsonFeatureEvolution> intersectFeatureEvolution(GeoJsonFeatureEvolution geoJsonFeatureEvolution,
            Geometry geometry, Date date) {
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = new ArrayList<>();

        Map<String, Object> properties = geoJsonFeatureEvolution.getProperties();
        long featureGroup = geoJsonFeatureEvolution.getFeatureGroup();

        if (geoJsonFeatureEvolution.getGeometry().disjoint(geometry)) {
            geoJsonFeatureEvolutions.add(geoJsonFeatureEvolution);
        } else {
            Geometry createdGeomtry = geoJsonFeatureEvolution.getGeometry().difference(geometry);
            Geometry earsedGeometry = geometry.difference(geoJsonFeatureEvolution.getGeometry());
            if (createdGeomtry.isEmpty() && earsedGeometry.isEmpty()) {
                geoJsonFeatureEvolution.setDate(date);
                geoJsonFeatureEvolutions.add(geoJsonFeatureEvolution);
            } else if (createdGeomtry.isEmpty()) {
                geoJsonFeatureEvolutions
                        .add(buildGeoJsonFeatureEvolution(date, featureGroup, earsedGeometry, properties, EARSED));
                geoJsonFeatureEvolutions.add(buildGeoJsonFeatureEvolution(geoJsonFeatureEvolution.getDate(),
                        featureGroup, geometry, properties, CREATED));
            } else if (earsedGeometry.isEmpty()) {
                geoJsonFeatureEvolutions
                        .add(buildGeoJsonFeatureEvolution(date, featureGroup, geometry, properties, CREATED));
                geoJsonFeatureEvolutions.add(buildGeoJsonFeatureEvolution(geoJsonFeatureEvolution.getDate(),
                        featureGroup, createdGeomtry, properties, CREATED));
            }
        }

        return geoJsonFeatureEvolutions;
    }

    private GeoJsonFeatureEvolution buildGeoJsonFeatureEvolution(Date date, long featureGroup, Geometry geometry,
            Map<String, Object> properties, GeoJsonStatus geoJsonStatus) {
        GeoJsonFeatureEvolutionBuilder geoJsonFeatureEvolutionBuilder = new GeoJsonFeatureEvolutionBuilder();
        geoJsonFeatureEvolutionBuilder.withDate(date).withGeometry(geometry);
        geoJsonFeatureEvolutionBuilder.withFeatureGroup(featureGroup);
        geoJsonFeatureEvolutionBuilder.withStatus(geoJsonStatus);
        return geoJsonFeatureEvolutionBuilder.withProperties(properties).build();
    }
}
