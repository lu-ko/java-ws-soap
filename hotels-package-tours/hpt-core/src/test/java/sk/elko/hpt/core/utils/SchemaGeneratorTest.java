package sk.elko.hpt.core.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

import sk.elko.hpt.core.config.AbstractTestContext;
import sk.elko.hpt.core.config.DbConfig;

/**
 * Test to generate DDL scripts.
 */
public class SchemaGeneratorTest extends AbstractTestContext {

    @Test
    public void generate_create_and_drop_script() throws Exception {
        SchemaGenerator gen = new SchemaGenerator(DbConfig.DB_BO_PACKAGE, null);

        // generate DROP script
        gen.generate(Dialect.HSQL, true);

        // generate CREATE script
        gen.generate(Dialect.HSQL, false);
    }

    private class SchemaGenerator {

        private Configuration cfg;

        public SchemaGenerator(String packageName, String defaultSchema) throws Exception {
            if (!StringUtils.hasText(packageName)) {
                throw new IllegalArgumentException("Given package name is empty!");
            }

            cfg = new Configuration();
            cfg.setProperty("hibernate.hbm2ddl.auto", "create");

            if (StringUtils.hasText(defaultSchema)) {
                cfg.setProperty("hibernate.default_schema", defaultSchema);
            }

            for (Class<?> clazz : getClasses(packageName)) {
                cfg.addAnnotatedClass(clazz);
            }
        }

        /**
         * Method that actually creates the DDL file.
         * 
         * @param dbDialect
         *            {@link Dialect} to use
         * @param dropOrCreate
         *            If {@code true}, only DROP script will be produced. Otherwise only CREATE script will be produced.
         */
        public void generate(Dialect dbDialect, boolean dropOrCreate) {
            cfg.setProperty("hibernate.dialect", dbDialect.getDialectClass());

            SchemaExport export = new SchemaExport(cfg);
            export.setDelimiter(";");
            export.setFormat(true);
            export.setHaltOnError(true);
            export.setOutputFile(getFilePath(dbDialect.name(), dropOrCreate));
            export.execute(true, false, dropOrCreate, !dropOrCreate);
        }

        private String getFilePath(String dialectName, boolean dropOrCreate) {
            return "build/" + (dropOrCreate ? "drop" : "create") + "_schema_" + dialectName.toLowerCase() + ".sql";

        }

        /**
         * Utility method used to fetch Class list based on a package name.
         * 
         * @param packageName
         *            (should be the package containing your annotated beans.
         */
        private List<Class<?>> getClasses(String packageName) throws Exception {
            List<Class<?>> classes = new ArrayList<Class<?>>();
            File directory = null;

            try {
                ClassLoader cld = Thread.currentThread().getContextClassLoader();
                if (cld == null) {
                    throw new ClassNotFoundException("Can't get class loader.");
                }
                String path = packageName.replace('.', '/');
                URL resource = cld.getResource(path);
                if (resource == null) {
                    throw new ClassNotFoundException("No resource for " + path);
                }
                directory = new File(resource.getFile());
            } catch (NullPointerException x) {
                throw new ClassNotFoundException(packageName + " (" + directory
                        + ") does not appear to be a valid package");
            }

            if (directory.exists()) {
                String[] files = directory.list();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].endsWith(".class")) {
                        // removes the .class extension
                        classes.add(Class.forName(packageName + '.' + files[i].substring(0, files[i].length() - 6)));
                    }
                }
            } else {
                throw new ClassNotFoundException(packageName + " is not a valid package");
            }

            return classes;
        }
    }

    /**
     * Holds the classnames of hibernate dialects for easy reference.
     */
    private static enum Dialect {

        HSQL("org.hibernate.dialect.HSQLDialect"),

        DB2("org.hibernate.dialect.DB2Dialect"),

        POSTGRE("org.hibernate.dialect.PostgreSQLDialect"),

        ORACLE("org.hibernate.dialect.Oracle10gDialect"),

        MYSQL("org.hibernate.dialect.MySQLDialect");

        private String dialectClass;

        private Dialect(String dialectClass) {
            this.dialectClass = dialectClass;
        }

        public String getDialectClass() {
            return dialectClass;
        }
    }

}