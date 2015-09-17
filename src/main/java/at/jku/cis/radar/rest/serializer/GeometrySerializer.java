package at.jku.cis.radar.rest.serializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeometrySerializer extends JsonSerializer<Geometry> {

    @Override
    public void serialize(Geometry geometry, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        writeGeometry(jsonGenerator, geometry);
    }

    private void writeGeometry(JsonGenerator jsonGenerator, Geometry geometry)
            throws JsonGenerationException, IOException {
        if (geometry instanceof Polygon) {
            writePolygon(jsonGenerator, (Polygon) geometry);

        } else if (geometry instanceof Point) {
            writePoint(jsonGenerator, (Point) geometry);

        } else if (geometry instanceof MultiPoint) {
            writeMultiPoint(jsonGenerator, (MultiPoint) geometry);

        } else if (geometry instanceof MultiPolygon) {
            writeMultiPolygon(jsonGenerator, (MultiPolygon) geometry);

        } else if (geometry instanceof LineString) {
            writeLineString(jsonGenerator, (LineString) geometry);

        } else if (geometry instanceof MultiLineString) {
            writeMultiLineString(jsonGenerator, (MultiLineString) geometry);
        } else if (geometry instanceof GeometryCollection) {
            writeGeometryCollection(jsonGenerator, (GeometryCollection) geometry);
        } else {
            throw new UnsupportedOperationException("not implemented: " + geometry.getClass().getName());
        }
    }

    private void writeGeometryCollection(JsonGenerator jgen, GeometryCollection value)
            throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "GeometryCollection");
        jgen.writeArrayFieldStart("geometries");

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeGeometry(jgen, value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writeMultiPoint(JsonGenerator jgen, MultiPoint value) throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "MultiPoint");
        jgen.writeArrayFieldStart("coordinates");

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePointCoords(jgen, (Point) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writeMultiLineString(JsonGenerator jgen, MultiLineString value)
            throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "MultiLineString");
        jgen.writeArrayFieldStart("coordinates");

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writeLineStringCoords(jgen, (LineString) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    @Override
    public Class<Geometry> handledType() {
        return Geometry.class;
    }

    private void writeMultiPolygon(JsonGenerator jgen, MultiPolygon value) throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "MultiPolygon");
        jgen.writeArrayFieldStart("coordinates");

        for (int i = 0; i != value.getNumGeometries(); ++i) {
            writePolygonCoordinates(jgen, (Polygon) value.getGeometryN(i));
        }

        jgen.writeEndArray();
        jgen.writeEndObject();
    }

    private void writePolygon(JsonGenerator jgen, Polygon value) throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "Polygon");
        jgen.writeFieldName("coordinates");
        writePolygonCoordinates(jgen, value);

        jgen.writeEndObject();
    }

    private void writePolygonCoordinates(JsonGenerator jgen, Polygon value)
            throws IOException, JsonGenerationException {
        jgen.writeStartArray();
        writeLineStringCoords(jgen, value.getExteriorRing());

        for (int i = 0; i != value.getNumInteriorRing(); ++i) {
            writeLineStringCoords(jgen, value.getInteriorRingN(i));
        }
        jgen.writeEndArray();
    }

    private void writeLineStringCoords(JsonGenerator jgen, LineString ring)
            throws JsonGenerationException, IOException {
        jgen.writeStartArray();
        for (int i = 0; i != ring.getNumPoints(); ++i) {
            Point p = ring.getPointN(i);
            writePointCoords(jgen, p);
        }
        jgen.writeEndArray();
    }

    private void writeLineString(JsonGenerator jgen, LineString lineString)
            throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "LineString");
        jgen.writeFieldName("coordinates");
        writeLineStringCoords(jgen, lineString);
        jgen.writeEndObject();
    }

    private void writePoint(JsonGenerator jgen, Point p) throws JsonGenerationException, IOException {
        jgen.writeStartObject();
        jgen.writeStringField("type", "Point");
        jgen.writeFieldName("coordinates");
        writePointCoords(jgen, p);
        jgen.writeEndObject();
    }

    private void writePointCoords(JsonGenerator jgen, Point p) throws IOException, JsonGenerationException {
        jgen.writeStartArray();
        jgen.writeNumber(p.getX());
        jgen.writeNumber(p.getY());
        jgen.writeEndArray();
    }

}