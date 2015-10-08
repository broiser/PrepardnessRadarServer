package at.jku.cis.radar.service;

import java.io.Serializable;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FeatureReferenceService implements Serializable {

    public String generateNextReference() {
        return UUID.randomUUID().toString();
    }
}
