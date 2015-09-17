package at.jku.cis.radar.rest.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

public class GeometryDeserializer extends JsonDeserializer<Geometry> {

    private GeometryFactory gemetryFactory = new GeometryFactory();

    @Override
    public Geometry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ObjectCodec objectCodec = jsonParser.getCodec();
        return parseGeometry(objectCodec.readTree(jsonParser));
    }

    private Geometry parseGeometry(JsonNode root) {
        String typeName = root.get("type").asText();
        if (typeName.equals("Point")) {
            return gemetryFactory.createPoint(parseCoordinate(root.get("coordinates")));

        } else if (typeName.equals("MultiPoint")) {
            return gemetryFactory.createMultiPoint(parseLineString(root.get("coordinates")));

        } else if (typeName.equals("LineString")) {
            return gemetryFactory.createLineString(parseLineString(root.get("coordinates")));

        } else if (typeName.equals("MultiLineString")) {
            return gemetryFactory.createMultiLineString(parseLineStrings(root.get("coordinates")));

        } else if (typeName.equals("Polygon")) {
            JsonNode arrayOfRings = root.get("coordinates");
            return parsePolygonCoordinates(arrayOfRings);

        } else if (typeName.equals("MultiPolygon")) {
            JsonNode arrayOfPolygons = root.get("coordinates");
            return gemetryFactory.createMultiPolygon(parsePolygons(arrayOfPolygons));

        } else if (typeName.equals("GeometryCollection")) {
            return gemetryFactory.createGeometryCollection(parseGeometries(root.get("geometries")));
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
        return gemetryFactory.createPolygon(parseExteriorRing(arrayOfRings), parseInteriorRings(arrayOfRings));
    }

    private Polygon[] parsePolygons(JsonNode arrayOfPolygons) {
        Polygon[] polygons = new Polygon[arrayOfPolygons.size()];
        for (int i = 0; i != arrayOfPolygons.size(); ++i) {
            polygons[i] = parsePolygonCoordinates(arrayOfPolygons.get(i));
        }
        return polygons;
    }

    private LinearRing parseExteriorRing(JsonNode arrayOfRings) {
        return gemetryFactory.createLinearRing(parseLineString(arrayOfRings.get(0)));
    }

    private LinearRing[] parseInteriorRings(JsonNode arrayOfRings) {
        LinearRing rings[] = new LinearRing[arrayOfRings.size() - 1];
        for (int i = 1; i < arrayOfRings.size(); ++i) {
            rings[i - 1] = gemetryFactory.createLinearRing(parseLineString(arrayOfRings.get(i)));
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
            strings[i] = gemetryFactory.createLineString(parseLineString(array.get(i)));
        }
        return strings;
    }
}