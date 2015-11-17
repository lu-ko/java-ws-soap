package sk.elko.hpt.core.service;

public interface AppVersionService {

    /** Returns date and time of build. */
    public String getBuildTime();

    /** Returns version of project {@code hpt-core}. */
    public String getHptCoreVersion();

    /** Returns version of project {@code hpt-schema}. */
    public String getHptSchemaVersion();

}
