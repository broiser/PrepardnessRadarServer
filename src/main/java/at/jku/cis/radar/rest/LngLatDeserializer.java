package at.jku.cis.radar.rest;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;

import at.jku.cis.radar.model.rest.LngLat;

public class LngLatDeserializer extends JsonDeserializer<LngLat> {

    @Override
    public LngLat deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.isExpectedStartArrayToken()) {
            return deserializeArray(jp, ctxt);
        }
        throw ctxt.mappingException(LngLat.class);
    }

    protected LngLat deserializeArray(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        LngLat node = new LngLat();
        node.setLongitude(extractDouble(jp, ctxt, false));
        node.setLatitude(extractDouble(jp, ctxt, false));
        if (jp.hasCurrentToken() && jp.getCurrentToken() != JsonToken.END_ARRAY) {
            jp.nextToken();
        }
        return node;
    }

    private double extractDouble(JsonParser jp, DeserializationContext ctxt, boolean optional)
            throws JsonParseException, IOException {
        JsonToken token = jp.nextToken();
        if (token == null) {
            return extractDouble(ctxt, optional);
        } else {
            return extractDouble(jp, ctxt, optional, token);
        }
    }

    private double extractDouble(DeserializationContext ctxt, boolean optional) throws JsonMappingException {
        if (optional)
            return Double.NaN;
        else
            throw ctxt.mappingException("Unexpected end-of-input when binding data into LngLat");
    }

    private double extractDouble(JsonParser jp, DeserializationContext ctxt, boolean optional, JsonToken token)
            throws JsonMappingException, IOException, JsonParseException {
        switch (token) {
        case END_ARRAY:
            return extractDouble(ctxt, optional);
        case VALUE_NUMBER_FLOAT:
            return jp.getDoubleValue();
        case VALUE_NUMBER_INT:
            return jp.getLongValue();
        default:
            throw ctxt.mappingException("Unexpected token (" + token.name() + ") when binding data into LngLat ");
        }
    }
}
