package at.jku.cis.radar.transformer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.collections4.Transformer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import at.jku.cis.radar.builder.GeoJsonFeatureEvolutionBuilder;
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

    @Override
    public List<GeoJsonFeatureEvolution> transform(FeatureEntry featureEntry) {
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = new ArrayList<>();

        for (GeometryEvolutionEntry geometryEvolutionEntry : featureEntry.getGeometryEvolutionEntries()) {
            geoJsonFeatureEvolutions.addAll(transform(featureEntry.getFeatureGroup(), geometryEvolutionEntry));
        }
        return geoJsonFeatureEvolutions;
    }

    private List<GeoJsonFeatureEvolution> transform(long featureGroup, GeometryEvolutionEntry geometryEvolutionEntry) {
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

        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = new ArrayList<>();
        if (!isGeometryEmpty(createdGeometryCollection)) {
            GeoJsonFeatureEvolution geoJsonFeatureEvolution = buildCreatedGeoJsonGeometry(featureGroup,
                    geometryEvolutionEntry.getDate(), createdGeometryCollection);
            geoJsonFeatureEvolutions.add(geoJsonFeatureEvolution);
        }
        if (!isGeometryEmpty(erasedGeometryCollection)) {
            GeoJsonFeatureEvolution geoJsonFeatureEvolution = buildErasedGeoJsonGeometry(featureGroup,
                    geometryEvolutionEntry.getDate(), erasedGeometryCollection);
            geoJsonFeatureEvolutions.add(geoJsonFeatureEvolution);
        }
        return geoJsonFeatureEvolutions;
    }

    private GeoJsonFeatureEvolution buildErasedGeoJsonGeometry(long featureGroup, Date date,
            GeometryCollection geometryCollection) {
        return buildGeoJsonGeometry(featureGroup, date, geometryCollection, GeoJsonStatus.ERASED);
    }

    private GeoJsonFeatureEvolution buildCreatedGeoJsonGeometry(long featureGroup, Date date,
            GeometryCollection geometryCollection) {
        return buildGeoJsonGeometry(featureGroup, date, geometryCollection, GeoJsonStatus.CREATED);
    }

    private GeoJsonFeatureEvolution buildGeoJsonGeometry(long featureGroup, Date date,
            GeometryCollection geometryCollection, GeoJsonStatus status) {
        GeoJsonFeatureEvolutionBuilder geoJsonFeatureEvolutionBuilder = new GeoJsonFeatureEvolutionBuilder();
        geoJsonFeatureEvolutionBuilder.withFeatureGroup(featureGroup);
        geoJsonFeatureEvolutionBuilder.withCreationDate(date);
        geoJsonFeatureEvolutionBuilder.withGeometry(geometryCollection);
        geoJsonFeatureEvolutionBuilder.withStatus(status);
        return geoJsonFeatureEvolutionBuilder.build();
    }

    private boolean isGeometryEmpty(GeometryCollection createdGeometryCollection) {
        return createdGeometryCollection.isEmpty();
    }
}
