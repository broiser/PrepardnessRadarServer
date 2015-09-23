package at.jku.cis.radar.converter;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.Period;

@Converter(autoApply = true)
public class PeriodHibernateConverter implements AttributeConverter<Period, String> {

    @Override
    public String convertToDatabaseColumn(Period entityValue) {
        return Objects.toString(entityValue, null);
    }

    @Override
    public Period convertToEntityAttribute(String databaseValue) {
        return databaseValue == null ? null : Period.parse(databaseValue);
    }
}