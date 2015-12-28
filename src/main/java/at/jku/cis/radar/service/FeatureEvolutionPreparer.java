package at.jku.cis.radar.service;

import static at.jku.cis.radar.geojson.GeoJsonStatus.CREATED;
import static at.jku.cis.radar.geojson.GeoJsonStatus.ERASED;

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
                long featureGroup = featureEvolution.getFeatureGroup();
                Map<String, Object> properties = featureEvolution.getProperties();
                geoJsonFeatureEvolutions
                        .add(buildGeoJsonFeatureEvolution(date, featureGroup, geometry, properties, CREATED));
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
        GeoJsonStatus status = geoJsonFeatureEvolution.getStatus();
        long featureGroup = geoJsonFeatureEvolution.getFeatureGroup();

        if (geoJsonFeatureEvolution.getGeometry().disjoint(geometry)) {
            geoJsonFeatureEvolutions.add(geoJsonFeatureEvolution);
        } else {
            Geometry createdGeometry = geoJsonFeatureEvolution.getGeometry().difference(geometry);
            Geometry erasedGeometry = geometry.difference(geoJsonFeatureEvolution.getGeometry());

            if (createdGeometry.isEmpty() && erasedGeometry.isEmpty()) {
                geoJsonFeatureEvolutions
                        .add(buildGeoJsonFeatureEvolution(date, featureGroup, geometry, properties, status));
            } else if (createdGeometry.isEmpty()) {
                geoJsonFeatureEvolutions.add(buildGeoJsonFeatureEvolution(geoJsonFeatureEvolution.getDate(),
                        featureGroup, erasedGeometry, properties, ERASED));
                geoJsonFeatureEvolutions.add(buildGeoJsonFeatureEvolution(date, featureGroup,
                        geometry.difference(erasedGeometry), properties, CREATED));
                // create
            } else if (erasedGeometry.isEmpty()) {
                geoJsonFeatureEvolutions
                        .add(buildGeoJsonFeatureEvolution(date, featureGroup, geometry, properties, CREATED));
                geoJsonFeatureEvolutions.add(buildGeoJsonFeatureEvolution(geoJsonFeatureEvolution.getDate(),
                        featureGroup, createdGeometry, properties, CREATED));
            }
        }
        return geoJsonFeatureEvolutions;
    }

    private GeoJsonFeatureEvolution buildGeoJsonFeatureEvolution(Date date, long featureGroup, Geometry geometry,
            Map<String, Object> properties, GeoJsonStatus geoJsonStatus) {
        GeoJsonFeatureEvolutionBuilder geoJsonFeatureEvolutionBuilder = new GeoJsonFeatureEvolutionBuilder();
        geoJsonFeatureEvolutionBuilder.withProperties(properties);
        geoJsonFeatureEvolutionBuilder.withDate(date).withGeometry(geometry);
        geoJsonFeatureEvolutionBuilder.withFeatureGroup(featureGroup);
        geoJsonFeatureEvolutionBuilder.withStatus(geoJsonStatus);
        return geoJsonFeatureEvolutionBuilder.build();
    }
}
