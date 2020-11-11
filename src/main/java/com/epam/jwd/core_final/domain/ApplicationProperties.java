package com.epam.jwd.core_final.domain;

import java.util.Properties;

/**
 * This class should be IMMUTABLE!
 * <p>
 * Expected fields:
 * <p>
 * inputRootDir {@link String} - base dir for all input files
 * outputRootDir {@link String} - base dir for all output files
 * crewFileName {@link String}
 * missionsFileName {@link String}
 * spaceshipsFileName {@link String}
 * <p>
 * fileRefreshRate {@link Integer}
 * dateTimeFormat {@link String} - date/time format for {@link java.time.format.DateTimeFormatter} pattern
 */
public class ApplicationProperties {
    private static String inputRootDir;
    private static String outputRootDir;
    private static String crewFileName;
    private static String missionsFileName;
    private static String spaceshipsFileName;
    private static int fileRefreshRate;
    private static String dateTimeFormat;

    public static void populate(Properties properties) {
        inputRootDir = properties.getProperty("inputRootDir");
        outputRootDir = properties.getProperty("outputRootDir");
        crewFileName = properties.getProperty("crewFileName");
        missionsFileName = properties.getProperty("missionsFileName");
        spaceshipsFileName = properties.getProperty("spaceshipsFileName");
        fileRefreshRate = Integer.valueOf(properties.getProperty("fileRefreshRate"));
        dateTimeFormat = properties.getProperty("dateTimeFormat");
    }

    public static String getInputRootDir() {
        return inputRootDir;
    }

    public static String getOutputRootDir() {
        return outputRootDir;
    }

    public static String getCrewFileName() {
        return crewFileName;
    }

    public static String getMissionsFileName() {
        return missionsFileName;
    }

    public static String getSpaceshipsFileName() {
        return spaceshipsFileName;
    }

    public static int getFileRefreshRate() {
        return fileRefreshRate;
    }

    public static String getDateTimeFormat() {
        return dateTimeFormat;
    }
}
