package at.jku.cis.radar.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import at.jku.cis.radar.model.Event;

@ApplicationScoped
public class EventDao extends AbstractDao<Event> {

    public EventDao() {
        super(Event.class);
    }

    @Override
    public List<Event> findAll() {
        return createNamedQuery(Event.FIND_ALL).getResultList();
    }

    public Event findByName(String name) {
        return getSingleResult(createNamedQuery(Event.FIND_BY_NAME).setParameter(Event.NAME, name));
    }
}
