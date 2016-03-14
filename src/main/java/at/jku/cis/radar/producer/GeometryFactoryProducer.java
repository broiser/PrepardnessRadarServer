package at.jku.cis.radar.producer;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import com.vividsolutions.jts.geom.GeometryFactory;

@ApplicationScoped
public class GeometryFactoryProducer implements Serializable {

    @Produces
    @Dependent
    public GeometryFactory produceGeometryFactory() {
        return new GeometryFactory();
    }
}
