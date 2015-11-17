package sk.elko.hpt.core.common;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Helper class used for simple date and time transformation between {@link XMLGregorianCalendar} and Joda's
 * {@link DateTime}.
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
     * Transforms {@link XMLGregorianCalendar} to Joda {@link DateTime}.
     * 
     * @param xmlGregorianCalendar
     *            {@link XMLGregorianCalendar}
     * @return {@link DateTime}
     */
    public static DateTime getJodaDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        } else {
            return new DateTime(xmlGregorianCalendar.toGregorianCalendar().getTime());
        }
    }

    /**
     * Transforms {@link DateTime} to {@link javax.xml.datatype.XMLGregorianCalendar}.
     * 
     * @param jodaDateTime
     *            {@link DateTime}
     * @return {@link javax.xml.datatype.XMLGregorianCalendar}
     */
    public static XMLGregorianCalendar getXMLGregorianCalendar(DateTime jodaDateTime) {
        if (jodaDateTime == null) {
            return null;
        }
        GregorianCalendar calendar = gregorianCalendar.get();
        calendar.setTime(jodaDateTime.toDate());

        return dateTypeFactory.get().newXMLGregorianCalendar(calendar);

    }

    public static String formatDateTime(final DateTime dateTime) {
        String dateTimeString = "-";
        if (dateTime != null) {
            dateTimeString = DateTimeFormat.forPattern(DEFAULT_PATTERN).print(dateTime);
        }
        return dateTimeString;
    }
}
