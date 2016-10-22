package at.jku.cis.radar.transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.Transformer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import at.jku.cis.radar.builder.GeoJsonFeatureEvolutionBuilder;
import at.jku.cis.radar.comparator.GeoJsonFeatureEvolutionComparator;
import at.jku.cis.radar.geojson.GeoJsonFeatureEvolution;
import at.jku.cis.radar.geojson.GeoJsonStatus;
import at.jku.cis.radar.model.FeatureEntry;
import at.jku.cis.radar.model.GeometryEntry;
import at.jku.cis.radar.model.GeometryEvolutionEntry;
import at.jku.cis.radar.service.GeometryService;

@ApplicationScoped
public class FeatureEntryGeoJsonFeatureEvolutionTransformer
        implements Transformer<FeatureEntry, List<GeoJsonFeatureEvolution>> {

    @Inject
    private GeometryService geometryService;
    @Inject
    private GeometryFactory geometryFactory;
    @Inject
    private GeoJsonFeatureEvolutionComparator geoJsonFeatureEvolutionComparator;

    @Override
    public List<GeoJsonFeatureEvolution> transform(FeatureEntry featureEntry) {
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = new ArrayList<>();

        for (GeometryEvolutionEntry geometryEvolutionEntry : featureEntry.getGeometryEvolutionEntries()) {
            geoJsonFeatureEvolutions.addAll(transform(geometryEvolutionEntry));
        }

        Collections.sort(geoJsonFeatureEvolutions, geoJsonFeatureEvolutionComparator);
        return geoJsonFeatureEvolutions;
    }

    private List<GeoJsonFeatureEvolution> transform(GeometryEvolutionEntry geometryEvolutionEntry) {
        GeometryCollection erasedGeometryCollection = geometryFactory.createGeometryCollection(new Geometry[0]);
        GeometryCollection createdGeometryCollection = geometryFactory.createGeometryCollection(new Geometry[0]);

        for (GeometryEntry geometryEntry : geometryEvolutionEntry.getGeometryEntries()) {
            GeometryCollection geometryCollection = (GeometryCollection) geometryEntry.getGeometry();
            switch (geometryEntry.getStatus()) {
            case CREATED:
                createdGeometryCollection = geometryService.union(createdGeometryCollection, geometryCollection);
                break;
            case ERASED:
                if (isGeometryEmpty(createdGeometryCollection)) {
                    erasedGeometryCollection = geometryCollection;
                } else {
                    createdGeometryCollection = geometryService.difference(createdGeometryCollection,
                            geometryEntry.getGeometry());
                    if (!isGeometryEmpty(erasedGeometryCollection)) {
                        erasedGeometryCollection = geometryService.union(erasedGeometryCollection, geometryCollection);
                    }
                }
                break;
            }
        }

        return buildGeoJsonFeatureEvolutions(geometryEvolutionEntry.getFeatureEntry().getFeatureGroup(),
                geometryEvolutionEntry.getDate(), createdGeometryCollection, erasedGeometryCollection);
    }

    private List<GeoJsonFeatureEvolution> buildGeoJsonFeatureEvolutions(long featureGroup, Date date,
            GeometryCollection createdGeometryCollection, GeometryCollection erasedGeometryCollection) {
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = new ArrayList<>();

        if (!isGeometryEmpty(createdGeometryCollection)) {
            geoJsonFeatureEvolutions.add(
                    buildGeoJsonFeatureEvolution(featureGroup, date, createdGeometryCollection, GeoJsonStatus.CREATED));
        }
        if (!isGeometryEmpty(erasedGeometryCollection)) {
            geoJsonFeatureEvolutions.add(
                    buildGeoJsonFeatureEvolution(featureGroup, date, erasedGeometryCollection, GeoJsonStatus.ERASED));
        }
        return geoJsonFeatureEvolutions;
    }

    private GeoJsonFeatureEvolution buildGeoJsonFeatureEvolution(long featureGroup, Date date, Geometry geometry,
            GeoJsonStatus status) {
        GeoJsonFeatureEvolutionBuilder geoJsonFeatureEvolutionBuilder = new GeoJsonFeatureEvolutionBuilder();
        geoJsonFeatureEvolutionBuilder.withFeatureGroup(featureGroup);
        geoJsonFeatureEvolutionBuilder.withCreationDate(date);
        geoJsonFeatureEvolutionBuilder.withGeometry(geometry);
        geoJsonFeatureEvolutionBuilder.withStatus(status);
        return geoJsonFeatureEvolutionBuilder.build();
    }

    private boolean isGeometryEmpty(GeometryCollection createdGeometryCollection) {
        return createdGeometryCollection.isEmpty();
    }
}
