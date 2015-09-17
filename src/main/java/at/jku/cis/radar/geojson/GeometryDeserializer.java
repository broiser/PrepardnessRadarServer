package at.jku.cis.radar.geojson;

import static at.jku.cis.radar.geojson.GeoJsonObject.COORDINATES;
import static at.jku.cis.radar.geojson.GeoJsonObject.GEOMETRIES;
import static at.jku.cis.radar.geojson.GeoJsonObject.TYPE;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeometryDeserializer extends JsonDeserializer<Geometry> {

    private GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public Geometry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        return parseGeometry(jsonParser.getCodec().readTree(jsonParser));
    }

    private Geometry parseGeometry(JsonNode root) {
        String typeName = root.get(TYPE).asText();
        if (typeName.equals(Point.class.getSimpleName())) {
            return geometryFactory.createPoint(parseCoordinate(root.get(COORDINATES)));
        } else if (typeName.equals(MultiPoint.class.getSimpleName())) {
            return geometryFactory.createMultiPoint(parseLineString(root.get(COORDINATES)));
        } else if (typeName.equals(LineString.class.getSimpleName())) {
            return geometryFactory.createLineString(parseLineString(root.get(COORDINATES)));
        } else if (typeName.equals(MultiLineString.class.getSimpleName())) {
            return geometryFactory.createMultiLineString(parseLineStrings(root.get(COORDINATES)));
        } else if (typeName.equals(Polygon.class.getSimpleName())) {
            return parsePolygonCoordinates(root.get(COORDINATES));
        } else if (typeName.equals(MultiPolygon.class.getSimpleName())) {
            return geometryFactory.createMultiPolygon(parsePolygons(root.get(COORDINATES)));
        } else if (typeName.equals(GeometryCollection.class.getSimpleName())) {
            return geometryFactory.createGeometryCollection(parseGeometries(root.get(GEOMETRIES)));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private Geometry[] parseGeometries(JsonNode arrayOfGeoms) {
        Geometry[] items = new Geometry[arrayOfGeoms.size()];
        for (int i = 0; i != arrayOfGeoms.size(); ++i) {
            items[i] = parseGeometry(arrayOfGeoms.get(i));
        }
        return items;
    }

    private Polygon parsePolygonCoordinates(JsonNode arrayOfRings) {
        return geometryFactory.createPolygon(parseExteriorRing(arrayOfRings), parseInteriorRings(arrayOfRings));
    }

    private Polygon[] parsePolygons(JsonNode arrayOfPolygons) {
        Polygon[] polygons = new Polygon[arrayOfPolygons.size()];
        for (int i = 0; i != arrayOfPolygons.size(); ++i) {
            polygons[i] = parsePolygonCoordinates(arrayOfPolygons.get(i));
        }
        return polygons;
    }

    private LinearRing parseExteriorRing(JsonNode arrayOfRings) {
        return geometryFactory.createLinearRing(parseLineString(arrayOfRings.get(0)));
    }

    private LinearRing[] parseInteriorRings(JsonNode arrayOfRings) {
        LinearRing rings[] = new LinearRing[arrayOfRings.size() - 1];
        for (int i = 1; i < arrayOfRings.size(); ++i) {
            rings[i - 1] = geometryFactory.createLinearRing(parseLineString(arrayOfRings.get(i)));
        }
        return rings;
    }

    private Coordinate parseCoordinate(JsonNode array) {
        return new Coordinate(array.get(0).asDouble(), array.get(1).asDouble());
    }

    private Coordinate[] parseLineString(JsonNode array) {
        Coordinate[] points = new Coordinate[array.size()];
        for (int i = 0; i != array.size(); ++i) {
            points[i] = parseCoordinate(array.get(i));
        }
        return points;
    }

    private LineString[] parseLineStrings(JsonNode array) {
        LineString[] strings = new LineString[array.size()];
        for (int i = 0; i != array.size(); ++i) {
            strings[i] = geometryFactory.createLineString(parseLineString(array.get(i)));
        }
        return strings;
    }
}