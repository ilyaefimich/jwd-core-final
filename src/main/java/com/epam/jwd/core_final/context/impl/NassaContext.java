package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.util.PropertyReaderUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class NassaContext implements ApplicationContext {

    private static Collection<CrewMember> crewMembers;
    private static Collection<Spaceship> spaceships;
    private static Collection<FlightMission> missions;
    private static LocalDateTime lastUpdate = LocalDateTime.now();
    private static int refreshRate = 0;
    private static long lastModifiedDateCrewsFile = 0;
    private static long lastModifiedDateSpaceshipsFile = 0;
    private static long lastModifiedLengthCrewsFile = 0;
    private static long lastModifiedLengthSpaceshipsFile = 0;

    public static Collection<FlightMission> getMissions() {
        return missions;
    }

    public static Collection<CrewMember> getCrewMembers() {
        return crewMembers;
    }

    public static Collection<Spaceship> getSpaceships() {
        return spaceships;
    }

    public NassaContext() {
    }

    private static void initializeData() {
        crewMembers = new ArrayList<CrewMember>();
        spaceships = new ArrayList<Spaceship>();
        missions = new ArrayList<FlightMission>();
        lastUpdate = LocalDateTime.now();

        lastModifiedDateCrewsFile = FileParser.getUpdateDateOfCrewMemberFile();
        lastModifiedDateSpaceshipsFile = FileParser.getUpdateDateOfSpaceshipFile();
        lastModifiedLengthCrewsFile = FileParser.getLengthOfCrewMemberFile();
        lastModifiedLengthSpaceshipsFile = FileParser.getLengthOfSpaceshipFile();
    }

    @Override
    public <T extends BaseEntity> Collection<T> retrieveBaseEntityList(Class<T> tClass) {
        return null;
    } //TODO

    /**
     * @throws InvalidStateException
     */
    public static void init() throws InvalidStateException {
        try {
            PropertyReaderUtil.loadProperties();
            refreshRate = ApplicationProperties.getFileRefreshRate();
            readDataFromFiles();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidStateException();
        }
    }

    private static void readDataFromFiles() {
        initializeData();
        crewMembers = FileParser.loadCrewMembersFromFile();
        spaceships = FileParser.loadSpaceshipsFromFile();
    }

    public static void refreshDataByTimeout() {
        if (LocalDateTime.now().isAfter(lastUpdate.plusMinutes(refreshRate))) {
            if (lastModifiedDateCrewsFile != FileParser.getUpdateDateOfCrewMemberFile()
            || lastModifiedDateSpaceshipsFile != FileParser.getUpdateDateOfSpaceshipFile()
            || lastModifiedLengthCrewsFile != FileParser.getLengthOfCrewMemberFile()
            || lastModifiedLengthSpaceshipsFile != FileParser.getLengthOfSpaceshipFile()){
                readDataFromFiles();
            }
        }
    }
}