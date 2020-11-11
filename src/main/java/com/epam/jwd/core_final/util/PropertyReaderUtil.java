package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.exception.FileParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyReaderUtil {

    private static final Properties properties = new Properties();

    private PropertyReaderUtil() {
    }

    /**
     * try-with-resource using FileInputStream
     *
     * <p>
     * as a result - you should populate {@link ApplicationProperties} with corresponding
     * values from property file
     */
    public static void loadProperties() {
        final String propertiesFileName = "src/main/resources/application.properties";
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(propertiesFileName);
            properties.load(iStream);
            ApplicationProperties.populate(properties);

        } catch (IOException e) {
            throw new FileParseException(propertiesFileName);
        } finally {
            try {
                if(iStream != null){
                    iStream.close();
                }
            } catch (IOException e) {
                throw new FileParseException(propertiesFileName);
            }
        }
    }
}
