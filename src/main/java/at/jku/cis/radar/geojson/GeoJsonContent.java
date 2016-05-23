package at.jku.cis.radar.geojson;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import at.jku.cis.radar.comparator.DateMarshaller;
import at.jku.cis.radar.model.Event;

public class GeoJsonContent implements Serializable {

    private String title;
    private long featureGroup;
    private Event event;
    private String description;
    private String creator;
    private String modifier;
    private Date creationDate;
    private Date modifiedDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getFeatureGroup() {
        return featureGroup;
    }

    public void setFeatureGroup(long featureGroup) {
        this.featureGroup = featureGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @XmlJavaTypeAdapter(value = DateMarshaller.class)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @XmlJavaTypeAdapter(value = DateMarshaller.class)
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
