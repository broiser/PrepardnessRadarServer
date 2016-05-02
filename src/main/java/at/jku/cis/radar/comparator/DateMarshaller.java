package at.jku.cis.radar.comparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
public class DateMarshaller extends XmlAdapter<String, Date> {

    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @Override
    public String marshal(Date date) {
        return date == null ? StringUtils.EMPTY : dateFormat.format(date);
    }

    @Override
    public Date unmarshal(String v) {
        try {
            return StringUtils.isEmpty(v) ? null : dateFormat.parse(v);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}