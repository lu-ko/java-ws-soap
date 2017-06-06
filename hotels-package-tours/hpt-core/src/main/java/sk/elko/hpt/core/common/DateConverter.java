package sk.elko.hpt.core.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper class used for simple date and time transformation between {@link XMLGregorianCalendar} and {@link LocalDateTime}.
 */
public class DateConverter {
    private static final Log log = LogFactory.getLog(DateConverter.class);

    private final static String DEFAULT_PATTERN = "dd.MM.yyyy HH:mm:ss";

    public static ThreadLocal<GregorianCalendar> gregorianCalendar = new ThreadLocal<GregorianCalendar>() {
        @Override
        public GregorianCalendar initialValue() {
            return (GregorianCalendar) GregorianCalendar.getInstance();
        }
    };

    public static ThreadLocal<DatatypeFactory> dateTypeFactory = new ThreadLocal<DatatypeFactory>() {
        @Override
        public DatatypeFactory initialValue() {
            try {
                return DatatypeFactory.newInstance();
            } catch (Exception e) {
                log.error("initialValue - " + e.getMessage(), e);
                return null;
            }
        }
    };

    /**
     * Transforms {@link java.util.Date} to {@link javax.xml.datatype.XMLGregorianCalendar}.
     * 
     * @param javaUtilDate
     *            {@link java.util.Date}
     * @return {@link javax.xml.datatype.XMLGregorianCalendar}
     */
    public static XMLGregorianCalendar getXMLGregorianCalendar(Date javaUtilDate) {
        if (javaUtilDate == null) {
            return null;
        }
        GregorianCalendar calendar = gregorianCalendar.get();
        calendar.setTime(javaUtilDate);

        return dateTypeFactory.get().newXMLGregorianCalendar(calendar);

    }

    /**
     * Transforms {@link XMLGregorianCalendar} to {@link LocalDateTime}.
     * 
     * @param xmlGregorianCalendar
     *            {@link XMLGregorianCalendar}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime getLocalDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        } else {
            return LocalDateTime.from(Instant.ofEpochMilli(xmlGregorianCalendar.toGregorianCalendar().getTime().getTime()));
        }
    }

    /**
     * Transforms {@link LocalDateTime} to {@link javax.xml.datatype.XMLGregorianCalendar}.
     * 
     * @param javaDateTime
     *            {@link LocalDateTime}
     * @return {@link javax.xml.datatype.XMLGregorianCalendar}
     */
    public static XMLGregorianCalendar getXMLGregorianCalendar(LocalDateTime javaDateTime) {
        if (javaDateTime == null) {
            return null;
        }
        GregorianCalendar calendar = GregorianCalendar.from(javaDateTime.atZone(ZoneId.systemDefault()));

        return dateTypeFactory.get().newXMLGregorianCalendar(calendar);

    }

    public static String formatDateTime(final LocalDateTime dateTime) {
        String dateTimeString = "-";
        if (dateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
            dateTimeString = dateTime.format(formatter);
        }
        return dateTimeString;
    }
}
