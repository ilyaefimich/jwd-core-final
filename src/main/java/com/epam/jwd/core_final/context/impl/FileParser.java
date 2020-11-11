package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.domain.ApplicationProperties;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.FileParseException;
import com.epam.jwd.core_final.service.impl.CrewMemberServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class FileParser {
    static public Collection<CrewMember> loadCrewMembersFromFile() {
        String fileName = ApplicationProperties.getInputRootDir() + "/" + ApplicationProperties.getCrewFileName();
        Scanner sc = new Scanner(System.in);
        BufferedReader br = null;
        try {
            File file = new File(fileName);
            Scanner scanner = null;
            scanner = new Scanner(file);

            Collection<CrewMember> crewMembers = new ArrayList<>();
            int index = 0;
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] members = scanner.nextLine().split(";");

                for (String member : members) {
                    String[] memberFields = member.split(",");
                    long role = Long.parseLong(memberFields[0]);
                    String name = memberFields[1];
                    int rank = Integer.parseInt(memberFields[2]);

                    CrewMember crewMember = CrewMemberServiceImpl.getInstance().createCrewMember(name, rank, role, true);
                    crewMembers.add(crewMember);
                }
            }
            scanner.close();
            return crewMembers;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileParseException(fileName);
        }
    }

    static public Collection<Spaceship> loadSpaceshipsFromFile() {
        String fileName = ApplicationProperties.getInputRootDir() + "/" + ApplicationProperties.getSpaceshipsFileName();
        Scanner sc = new Scanner(System.in);
        BufferedReader br = null;
        try {
            File file = new File(fileName);
            Scanner scanner = null;
            scanner = new Scanner(file);

            Collection<Spaceship> spaceShips = new ArrayList<>();
            scanner.nextLine();
            scanner.nextLine();
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] shipFields = scanner.nextLine().split(";");
                String name = shipFields[0];
                long distance = Long.parseLong(shipFields[1]);
                String crewRole = shipFields[2].substring(1, shipFields[2].length() - 1);
                String[] crewRoles = crewRole.split(",");
                Spaceship spaceship = new Spaceship(name);
                spaceship.setFlightDistance(distance);
                spaceship.setReadyForNextMissions(true);

                for (String crew : crewRoles) {
                    String[] roleDescription = crew.split(":");
                    spaceship.addCrewRole(Long.parseLong(roleDescription[0]), Short.parseShort(roleDescription[1]));
                }
                spaceShips.add(spaceship);
            }
            scanner.close();
            return spaceShips;

        } catch (Exception e) {
            throw new FileParseException(fileName);
        }
    }

    static public void saveMissionsToJSONFile(List<FlightMission> missions) {
        String fileName = ApplicationProperties.getOutputRootDir() + "/" + ApplicationProperties.getMissionsFileName();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(fileName), missions);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileParseException(fileName);
        }
    }

    static public long getLengthOfSpaceshipFile() {
        String fileName = ApplicationProperties.getInputRootDir() + "/" + ApplicationProperties.getSpaceshipsFileName();
        try {
            File file = new File(fileName);
            return file.length();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileParseException(fileName);
        }
    }

    static public long getLengthOfCrewMemberFile() {
        String fileName = ApplicationProperties.getInputRootDir() + "/" + ApplicationProperties.getCrewFileName();
        try {
            File file = new File(fileName);
            return file.length();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileParseException(fileName);
        }
    }

    static public long getUpdateDateOfSpaceshipFile() {
        String fileName = ApplicationProperties.getInputRootDir() + "/" + ApplicationProperties.getSpaceshipsFileName();
        try {
            File file = new File(fileName);
            return file.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileParseException(fileName);
        }
    }

    static public long getUpdateDateOfCrewMemberFile() {
        String fileName = ApplicationProperties.getInputRootDir() + "/" + ApplicationProperties.getCrewFileName();
        try {
            File file = new File(fileName);
            return file.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileParseException(fileName);
        }
    }
}
