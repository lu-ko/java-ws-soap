package sk.elko.hpt.core.common;

/**
 * One common business exception for handling all errors.<br />
 * TODO exception improvement: Should be divided into more business exceptions with specific meaning and purpose.
 */
public class HptException extends Exception {
    private static final long serialVersionUID = 8205965032215119304L;

    public HptException(String message) {
        super(message);
    }

    public HptException(Throwable error) {
        super(error.getMessage(), error);
    }

}
