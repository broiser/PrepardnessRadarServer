package at.jku.cis.radar.model.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Crs implements Serializable {

    private CrsType type = CrsType.name;
    private Map<String, Object> properties = new HashMap<String, Object>();

    public CrsType getType() {
        return type;
    }

    public void setType(CrsType type) {
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Crs{" + "type='" + type + '\'' + ", properties=" + properties + '}';
    }
}
