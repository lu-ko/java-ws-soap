package sk.elko.hpt.core.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.util.StringUtils;
import org.testng.annotations.Test;

import sk.elko.hpt.core.config.AbstractTestContext;
import sk.elko.hpt.core.config.DatabaseConfig;

/**
 * Test to generate DDL scripts.
 */
public class SchemaGeneratorTest extends AbstractTestContext {

    @Test
    public void generate_create_and_drop_script() throws Exception {
        SchemaGenerator gen = new SchemaGenerator(DatabaseConfig.DB_BO_PACKAGE, null, Dialect.HSQL);
        gen.generate();
    }

    private class SchemaGenerator {

        private Dialect dialect;
        private MetadataSources metadata;

        public SchemaGenerator(String packageName, String defaultSchema, Dialect dbDialect) throws Exception {
            if (!StringUtils.hasText(packageName)) {
                throw new IllegalArgumentException("Given package name is empty!");
            }
            dialect = dbDialect;

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
            builder = builder.applySetting("hibernate.dialect", dialect.getDialectClass());
            builder = builder.applySetting("hibernate.hbm2ddl.auto", "create");

            if (StringUtils.hasText(defaultSchema)) {
                builder.applySetting("hibernate.default_schema", defaultSchema);
            }

            metadata = new MetadataSources(builder.build());

            for (Class<?> clazz : getClasses(packageName)) {
                metadata.addAnnotatedClass(clazz);
            }
        }

        /**
         * Method that actually creates the DDL script.
         */
        public void generate() {
            SchemaExport export = new SchemaExport();
            export.setDelimiter(";");
            export.setFormat(true);
            export.setHaltOnError(true);
            export.setOutputFile(getFilePath(dialect.name()));
            export.create(EnumSet.of(TargetType.STDOUT, TargetType.SCRIPT), metadata.buildMetadata());
        }

        private String getFilePath(String dialectName) {
            return "build/schema_" + dialectName.toLowerCase() + ".sql";

        }

        /**
         * Utility method used to fetch Class list based on a package name.
         *
         * @param packageName (should be the package containing your annotated beans.
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