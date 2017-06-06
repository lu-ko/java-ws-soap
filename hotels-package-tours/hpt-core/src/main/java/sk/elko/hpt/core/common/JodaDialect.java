package sk.elko.hpt.core.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;

/**
 * A Thymeleaf dialect that provide {@link Joda} expression object. This object can be used to format Java's
 * {@link LocalDateTime} objects in your Thymeleaf pages.
 *
 * It's called Joda due to historical reasons (usage of Joda time in Java 7).
 */
public class JodaDialect extends AbstractDialect implements IExpressionEnhancingDialect {
    private static final Log log = LogFactory.getLog(JodaDialect.class);

    public static final String DEFAULT_PREFIX = "joda";
    public static final String JODA_EXPRESSION_OBJECT_NAME = "joda";

    @Autowired
    private HttpServletRequest request;

    private final Joda joda;

    public JodaDialect() {
        this.joda = new Joda();
        log.info("JodaDialect - initialized");
    }

    @Override
    public String getPrefix() {
        return DEFAULT_PREFIX;
    }

    @Override
    public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
        if (joda == null) {
            throw new IllegalStateException("Joda is null!");
        }

        HashMap<String, Object> expressionobjects = new HashMap<String, Object>();
        expressionobjects.put(JODA_EXPRESSION_OBJECT_NAME, joda);
        return expressionobjects;
    }

    public Locale getLocale() {
        if (request == null) {
            throw new IllegalStateException("HttpServletRequest is null!");
        }
        return RequestContextUtils.getLocale(request);
    }

    /**
     * Joda expression object used in Thymeleaf dialect {@link JodaDialect}.
     */
    public class Joda {
        private final static String DEFAULT_PATTERN = "dd.MM.yyyy HH:mm:ss";

        /**
         * Prints out a formatted Joda's {@link LocalDateTime} instance given as parameter {@code dateTime} with the default
         * pattern {@link Joda#DEFAULT_PATTERN}.
         * 
         * @param dateTime
         *            Joda's datetime
         * @return Formatted string
         */
        public String format(final LocalDateTime dateTime) {
            return format(dateTime, DEFAULT_PATTERN);
        }

        /**
         * Prints out a formatted {@link LocalDateTime} instance given as parameter {@code dateTime} with pattern
         * given as second parameter {@code pattern}.
         * 
         * @param dateTime
         *            Java's datetime
         * @param pattern
         *            Pattern used by the {@link DateTimeFormatter#ofPattern(String)} method
         * @return Formatted string
         */
        public String format(final LocalDateTime dateTime, String pattern) {
            String dateTimeString = "-";
            if (dateTime != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                dateTimeString = dateTime.format(formatter);
            }
            return dateTimeString;
        }
    }

}
