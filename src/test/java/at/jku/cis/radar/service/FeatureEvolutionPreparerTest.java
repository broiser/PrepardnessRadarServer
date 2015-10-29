package at.jku.cis.radar.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import at.jku.cis.radar.geojson.Feature;
import at.jku.cis.radar.model.FeatureEvolution;

@RunWith(MockitoJUnitRunner.class)
public class FeatureEvolutionPreparerTest {

    private static final int EVENT_ID = 15;
    private static final int FEATURE_GROUP = 10;

    @Mock
    private FeatureEvolutionService featureEvolutionService;

    @InjectMocks
    private FeatureEvolutionPreparer featureEvolutionPreparer;

    @Test
    public void singleFeatureEvolutionIsPreparedAsMap() {
        Date date = toDate(12, 00);
        List<FeatureEvolution> featureEvolutions = mockFeatureEvolutionService(
                createFeatureEvolution(date, createPolygon()));
        Map<Date, List<Feature>> result = featureEvolutionPreparer.prepareEvolution(featureEvolutions);
        assertEquals(featureEvolutions.get(0).getGeometry(), result.get(date).get(0).getGeometry());
    }

    @Test
    public void twoFeatureEvolutionWithIntersectionArePreparedAsMap() {
        Date date = toDate(13, 00);
        List<FeatureEvolution> featureEvolutions = mockFeatureEvolutionService(
                createFeatureEvolution(toDate(12, 00), toCoordinate(10, 10), toCoordinate(10, 15), toCoordinate(15, 15),
                        toCoordinate(15, 10)),
                createFeatureEvolution(date, toCoordinate(10, 10), toCoordinate(10, 12), toCoordinate(12, 12),
                        toCoordinate(12, 10)));
        Map<Date, List<Feature>> result = featureEvolutionPreparer.prepareEvolution(featureEvolutions);
        assertEquals("", result.get(date).get(0).getGeometry());
    }

    private List<FeatureEvolution> mockFeatureEvolutionService(FeatureEvolution... featureEvolutionArray) {
        List<FeatureEvolution> featureEvolutions = Arrays.asList(featureEvolutionArray);
        when(featureEvolutionService.findBetween(eq(EVENT_ID), eq(FEATURE_GROUP), any(DateTime.class),
                any(DateTime.class))).thenReturn(featureEvolutions);
        return featureEvolutions;
    }

    private FeatureEvolution createFeatureEvolution(Date date, Coordinate... coordinates) {
        return createFeatureEvolution(date, createPolygon(coordinates));
    }

    private FeatureEvolution createFeatureEvolution(Date date, Geometry geometry) {
        FeatureEvolution featureEvolution = new FeatureEvolution();
        featureEvolution.setDate(date);
        featureEvolution.setFeatureGroup(FEATURE_GROUP);
        featureEvolution.setGeometry(createGeometryCollection(geometry));
        return featureEvolution;
    }

    private GeometryCollection createGeometryCollection(Geometry geometry) {
        return new GeometryCollection(new Geometry[] { geometry }, geometry.getFactory());
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
