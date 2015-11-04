package at.jku.cis.radar.service;

import static at.jku.cis.radar.geojson.GeoJsonStatus.CREATED;
import static at.jku.cis.radar.geojson.GeoJsonStatus.EARSED;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import at.jku.cis.radar.geojson.GeoJsonFeatureEvolution;
import at.jku.cis.radar.geojson.GeoJsonStatus;
import at.jku.cis.radar.model.FeatureEvolution;

@RunWith(MockitoJUnitRunner.class)
public class FeatureEvolutionPreparerTest {

    private static final int FEATURE_GROUP = 10;

    @InjectMocks
    private FeatureEvolutionPreparer featureEvolutionPreparer;

    @Test
    public void singleFeatureIsCreatedFromGeometryCollectionWithOnePolygon() {
        Date date = toDate(12, 00);
        Polygon polygon = createPolygon();
        List<FeatureEvolution> featureEvolutions = Arrays.asList(createFeatureEvolution(date, polygon));
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = featureEvolutionPreparer
                .prepareEvolution(featureEvolutions);
        assertEvolutionEquals(date, CREATED, polygon, geoJsonFeatureEvolutions.get(0));
    }

    @Test
    public void twoFeatureAreCreatedFromGeometryCollectionWithTwoPolygons() {
        Date date = toDate(12, 00);
        Polygon polygon = createPolygon();
        List<FeatureEvolution> featureEvolutions = Arrays.asList(createFeatureEvolution(date, polygon, polygon));
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = featureEvolutionPreparer
                .prepareEvolution(featureEvolutions);
        assertEvolutionEquals(date, CREATED, polygon, geoJsonFeatureEvolutions.get(0));
        assertEvolutionEquals(date, CREATED, polygon, geoJsonFeatureEvolutions.get(1));
    }

    @Test
    public void twoFeatureAreCreatedFromTwoGeometryCollectionWithOnePolygon() {
        Date firstDate = toDate(11, 00);
        Date secondDate = toDate(12, 00);
        List<FeatureEvolution> featureEvolutions = Arrays.asList(
                createFeatureEvolution(firstDate, toCoordinate(0, 0), toCoordinate(10, 0), toCoordinate(10, 10)),
                createFeatureEvolution(secondDate, toCoordinate(0, 0), toCoordinate(10, 0), toCoordinate(10, 10),
                        toCoordinate(0, 10)));
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = featureEvolutionPreparer
                .prepareEvolution(featureEvolutions);
        assertEquals(2, geoJsonFeatureEvolutions.size());
        assertEvolutionEquals(firstDate, CREATED, "POLYGON ((0 0, 10 0, 10 10, 0 0))", geoJsonFeatureEvolutions.get(0));
        assertEvolutionEquals(secondDate, CREATED, "POLYGON ((0 0, 0 10, 10 10, 0 0))",
                geoJsonFeatureEvolutions.get(1));
    }

    @Test
    public void oneFeatureIsCreatedAndOneFeatureIsEarsedFromTwoGeometryCollectionWithOnePolygon() {
        Date firstDate = toDate(11, 00);
        Date secondDate = toDate(12, 00);
        List<FeatureEvolution> featureEvolutions = Arrays.asList(
                createFeatureEvolution(firstDate, toCoordinate(0, 0), toCoordinate(10, 0), toCoordinate(10, 10),
                        toCoordinate(0, 10)),
                createFeatureEvolution(secondDate, toCoordinate(0, 0), toCoordinate(10, 0), toCoordinate(10, 10)));
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = featureEvolutionPreparer
                .prepareEvolution(featureEvolutions);
        assertEquals(2, geoJsonFeatureEvolutions.size());
        assertEvolutionEquals(firstDate, EARSED, "POLYGON ((0 0, 0 10, 10 10, 0 0))", geoJsonFeatureEvolutions.get(0));
        assertEvolutionEquals(secondDate, CREATED, "POLYGON ((0 0, 10 0, 10 10, 0 10, 0 0))",
                geoJsonFeatureEvolutions.get(1));
    }

    @Test
    public void oneFeatureIsCreatedFromTwoGeometryCollectionWithOnePolygon() {
        Date date = toDate(11, 00);
        Polygon polygon = createPolygon(toCoordinate(0, 0), toCoordinate(10, 0), toCoordinate(10, 10));
        List<FeatureEvolution> featureEvolutions = Arrays.asList(createFeatureEvolution(date, polygon),
                createFeatureEvolution(toDate(12, 00), polygon));
        List<GeoJsonFeatureEvolution> geoJsonFeatureEvolutions = featureEvolutionPreparer
                .prepareEvolution(featureEvolutions);
        assertEquals(1, geoJsonFeatureEvolutions.size());
        assertEvolutionEquals(date, CREATED, "POLYGON ((0 0, 10 0, 10 10, 0 0))", geoJsonFeatureEvolutions.get(0));
    }

    private void assertEvolutionEquals(Date date, GeoJsonStatus geoJsonStatus, Geometry geometry,
            GeoJsonFeatureEvolution geoJsonFeatureEvolution) {
        assertEvolutionEquals(date, geoJsonStatus, geometry.toString(), geoJsonFeatureEvolution);
    }

    private void assertEvolutionEquals(Date date, GeoJsonStatus geoJsonStatus, String geometry,
            GeoJsonFeatureEvolution geoJsonFeatureEvolution) {
        assertEquals(date, geoJsonFeatureEvolution.getDate());
        assertEquals(geoJsonStatus, geoJsonFeatureEvolution.getStatus());
        assertEquals(FEATURE_GROUP, geoJsonFeatureEvolution.getFeatureGroup());
        assertEquals(geometry, geoJsonFeatureEvolution.getGeometry().toString());
    }

    private FeatureEvolution createFeatureEvolution(Date date, Coordinate... coordinates) {
        return createFeatureEvolution(date, createPolygon(coordinates));
    }

    private FeatureEvolution createFeatureEvolution(Date date, Geometry... geometries) {
        FeatureEvolution featureEvolution = new FeatureEvolution();
        featureEvolution.setDate(date);
        featureEvolution.setFeatureGroup(FEATURE_GROUP);
        featureEvolution.setGeometry(createGeometryCollection(geometries));
        return featureEvolution;
    }

    private GeometryCollection createGeometryCollection(Geometry[] geometries) {
        return new GeometryCollection(geometries, geometries[0].getFactory());
    }

    private Polygon createPolygon(Coordinate... coordinateArray) {
        if (coordinateArray.length == 0) {
            return new GeometryFactory().createPolygon(coordinateArray);
        }
        Coordinate[] coordinates = Arrays.copyOf(coordinateArray, coordinateArray.length + 1);
        coordinates[coordinateArray.length] = coordinateArray[0];
        return new GeometryFactory().createPolygon(coordinates);
    }

    private Coordinate toCoordinate(double x, double y) {
        return new Coordinate(x, y);
    }

    private Date toDate(int hour, int minute) {
        return new DateTime(2015, 1, 1, hour, minute).toDate();
    }
}
