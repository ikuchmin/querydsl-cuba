package ru.udya.querydsl.cuba;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.testsupport.TestContainer;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class QuerydslcubaTestContainer extends TestContainer {

    public QuerydslcubaTestContainer() {
        super();
        appComponents = new ArrayList<>(Arrays.asList(
                "com.haulmont.cuba"
                // add CUBA premium add-ons here
                // "com.haulmont.bpm",
                // "com.haulmont.charts",
                // "com.haulmont.fts",
                // "com.haulmont.reports",
                // and custom app components if any
        ));
        appPropertiesFiles = Arrays.asList(
                // List the files defined in your web.xml
                // in appPropertiesConfig context parameter of the core module
                "ru/udya/querydsl/cuba/app.properties",
                // Add this file which is located in CUBA and defines some properties
                // specifically for test environment. You can replace it with your own
                // or add another one in the end.
                "com/haulmont/cuba/testsupport/test-app.properties",
                "ru/udya/querydsl/cuba/test-app.properties");
        initDbProperties();
    }

    private void initDbProperties() {
      File contextXmlFile = new File("modules/core/web/META-INF/context.xml");
        if (!contextXmlFile.exists()) {
            contextXmlFile = new File("web/META-INF/context.xml");
        }

        if (!contextXmlFile.exists()) {
            throw new RuntimeException("Cannot find 'context.xml' file to read database connection properties. " +
                    "You can set them explicitly in this method.");
        }
        Document contextXmlDoc = Dom4j.readDocument(contextXmlFile);
        Element resourceElem = contextXmlDoc.getRootElement().element("Resource");

        Optional.ofNullable(resourceElem.attributeValue("driverClassName")).ifPresent(e -> dbDriver = e);
        Optional.ofNullable(resourceElem.attributeValue("url")).ifPresent(e -> dbUrl = e);
        Optional.ofNullable(resourceElem.attributeValue("username")).ifPresent(e -> dbUser = e);
        Optional.ofNullable(resourceElem.attributeValue("password")).ifPresent(e -> dbPassword = e);
    }

    
    public static class Common extends QuerydslcubaTestContainer {

        public static final QuerydslcubaTestContainer.Common INSTANCE = new QuerydslcubaTestContainer.Common();

        private static volatile boolean initialized;

        private Common() {
        }

        @Override
        public void before() throws Throwable {
            if (!initialized) {
                super.before();
                initialized = true;
            }
            setupContext();
        }

        @Override
        public void after() {
            cleanupContext();
            // never stops - do not call super
        }
    }
}