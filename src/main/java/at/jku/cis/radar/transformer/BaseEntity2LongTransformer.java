package at.jku.cis.radar.transformer;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.collections4.Transformer;

import at.jku.cis.radar.model.BaseEntity;

@ApplicationScoped
public class BaseEntity2LongTransformer implements Transformer<BaseEntity, Long> {

    @Override
    public Long transform(BaseEntity baseEntity) {
        return baseEntity.getId();
    }
}
