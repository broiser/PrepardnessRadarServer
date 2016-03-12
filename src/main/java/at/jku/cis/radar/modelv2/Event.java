package at.jku.cis.radar.modelv2;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.Period;

@Entity
@NamedQuery(name = Event.FIND_BY_NAME, query = "SELECT e FROM Event e WHERE e.name = :" + Event.NAME)
@NamedNativeQuery(name = Event.FIND_ALL, query = "SELECT e.* FROM Event e WHERE e.event_id is null", resultClass = Event.class)
public class Event extends BaseEntity {
    public static final String FIND_BY_NAME = "Event.findByName";
    public static final String FIND_ALL = "Event.findAll";

    public static final String NAME = "name";

    @Column(unique = true)
    private String name;
    private int color;
    private boolean visible;
    @JsonIgnore
    private Period validationPeriod;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private List<Event> events;

    public Event() {
    }

    public Event(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Period getValidationPeriod() {
        return validationPeriod;
    }

    public void setValidationPeriod(Period validationPeriod) {
        this.validationPeriod = validationPeriod;
    }
}
