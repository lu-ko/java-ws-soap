package sk.elko.hpt.core.controller.form;

public class AboutForm {

    private String buildTime;
    private String hptCoreVersion;
    private String hptSchemaVersion;

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getHptCoreVersion() {
        return hptCoreVersion;
    }

    public void setHptCoreVersion(String hptCoreVersion) {
        this.hptCoreVersion = hptCoreVersion;
    }

    public String getHptSchemaVersion() {
        return hptSchemaVersion;
    }

    public void setHptSchemaVersion(String hptSchemaVersion) {
        this.hptSchemaVersion = hptSchemaVersion;
    }

}
