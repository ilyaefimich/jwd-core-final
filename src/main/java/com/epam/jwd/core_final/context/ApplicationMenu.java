package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.FileParser;
import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.service.impl.CrewMemberServiceImpl;
import com.epam.jwd.core_final.service.impl.FlightMissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceShipServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

@FunctionalInterface
public interface ApplicationMenu {

    static final Logger logger = LoggerFactory.getLogger("JWDCourseLogger");

    ApplicationContext getApplicationContext();

    default Object printAvailableOptions() {
        System.out.println("\"------------------------------------------ Main Menu ----------------------------------------\n" +
                "           1) Manage Crew Members\n" +
                "           2) Manage Spaceships\n" +
                "           3) Manage Missions\n" +
                "           4) Exit from program");
        System.out.println("?");
        return null;
    }

    default Object handleUserInput(Object o) {
        Scanner sc = new Scanner(System.in);
        int choice;
        choice = sc.nextInt();
        NassaContext.refreshDataByTimeout();

        switch (choice) {
            case 1:
                while (true) {
                    printAvailableOptionsCrewMember();
                    if ((Integer) handleUserInputCrewMember(null) == 1) break;
                }
                break;
            case 2:
                while (true) {
                    printAvailableOptionsSpaceship();
                    if ((Integer) handleUserInputSpaceship(null) == 1) break;
                }
                break;
            case 3:
                while (true) {
                    printAvailableOptionsMissionMenu();
                    if ((Integer) handleUserInputMissionMenu(null) == 1) break;
                }
                break;
            case 4:
                System.out.println("Thank you for using our application! Goodbye!");
                return 1;
            default:
                System.out.println("Please select a correct menu option.\n");
                printAvailableOptions();
                break;
        }
        return 0;
    }

    private void printAvailableOptionsMissionMenu() {
        System.out.println("\"------------------------------------------ Mission Menu ----------------------------------------\n" +
                "           1) View All Missions\n" +
                "           2) Filter Missions By Status\n" +
                "           3) Create Mission\n" +
                "           4) Update Mission's name\n" +
                "           5) Update Mission status\n" +
                "           6) Save Missions to file\n" +
                "           7) Return to the main menu");
        System.out.println("?");
    }

    private void handleCreateMission() {
        String name;
        LocalDate startDate;
        LocalDate endDate;
        long distance;
        Scanner sc = new Scanner(System.in);

        System.out.print("Input name of the mission: \n");
        while (true) {
            name = sc.nextLine();
            if (name.equals("")) {
                System.out.println("The mission name cannot be empty. Please enter a correct mission name.\n");
                continue;
            } else {
                break;
            }
        }

        System.out.print("Input start date of the mission (e.g. 2020-11-23 11:23:00):\n");
        while (true) {
            String startDateStr = sc.nextLine();
            try {
                String formatPattern = ApplicationProperties.getDateTimeFormat();
                startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern(formatPattern));
            } catch (Exception ex) {
                startDate = null;
            }

            if (startDate == null) {
                System.out.println("Incorrect format of the date. Please enter date in the format [ +" + ApplicationProperties.getDateTimeFormat() + "]\n");
                continue;
            } else {
                break;
            }
        }

        System.out.print("Input end date of the mission (e.g. 2020-11-23 11:23:00):\n");
        while (true) {
            String endDateStr = sc.nextLine();
            try {
                String formatPattern = ApplicationProperties.getDateTimeFormat();
                endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern(formatPattern));
            } catch (Exception ex) {
                endDate = null;
            }

            if (endDate == null) {
                System.out.println("Incorrect format of the date. Please enter date in the format [ +" + ApplicationProperties.getDateTimeFormat() + "]\n");
                continue;
            } else {
                break;
            }
        }

        System.out.print("Input distance of the mission: \n");
        while (true) {
            try {
                distance = sc.nextInt();
            } catch (Exception ex) {
                distance = 0;
            }

            if (distance <= 0) {
                System.out.println("Incorrect number. Please enter the correct number\n");
                continue;
            } else {
                break;
            }
        }

        try {
            FlightMission flightMission = FlightMissionServiceImpl.getInstance().createMission(name, distance, startDate, endDate);
            flightMission.setMissionResult(MissionResult.PLANNED);
            NassaContext.getMissions().add(flightMission);

        } catch (Exception e) {
            logger.error(e.getMessage());
            System.out.println("An exception has been thrown when create a mission: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Object handleUserInputMissionMenu(Object o) {
        Scanner sc = new Scanner(System.in);
        NassaContext.refreshDataByTimeout();

        switch (sc.nextInt()) {
            case 1:
                List<FlightMission> flightMissions = FlightMissionServiceImpl.getInstance().findAllMissions();
                System.out.println("----------------------------------------------------------- All Missions ------------------------------------------------------------------------------------------");
                System.out.println("|          Id             |              Name              |     Distance      |           Start date         |            End Date          |        Status      |");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                for (FlightMission flightMission : flightMissions) {
                    System.out.println("|" + String.format("%-25s", flightMission.getId()) + "|" + String.format("%-32s", flightMission.getName()) + "|" + String.format("%-19s", flightMission.getDistance()) + "|" + String.format("%-30s", flightMission.getStartDate()) + "|" + String.format("%-30s", flightMission.getEndDate()) + "|" + String.format("%-20s", flightMission.getMissionResult()) + "|");
                }
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                break;
            case 2:
                long missionId = 0;
                MissionResult status = null;
                System.out.println("Input new mission status (1 - Cancelled, 2 - Failed, 3 - Planned, 4 - In progress, 5 - Completed): ");
                while (true) {
                    try {
                        missionId = sc.nextLong();
                        status = MissionResult.resolveById(missionId);
                    } catch (Exception ex) {
                        missionId = 0;
                    }

                    if (missionId <= 0) {
                        System.out.println("Incorrect number. Please enter the correct number\n");
                        continue;
                    } else {
                        break;
                    }
                }

                FlightMissionCriteria criteria = new FlightMissionCriteria
                        .Builder()
                        .withMissionStatus(status)
                        .build();
                flightMissions = FlightMissionServiceImpl.getInstance().findAllMissionsByCriteria(criteria);
                System.out.println("----------------------------------------------------------- All Missions ------------------------------------------------------------------------------------------");
                System.out.println("|          Id             |              Name              |     Distance      |           Start date         |            End Date          |        Status      |");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                for (FlightMission flightMission : flightMissions) {
                    System.out.println("|" + String.format("%-25s", flightMission.getId()) + "|" + String.format("%-32s", flightMission.getName()) + "|" + String.format("%-19s", flightMission.getDistance()) + "|" + String.format("%-30s", flightMission.getStartDate()) + "|" + String.format("%-30s", flightMission.getEndDate()) + "|" + String.format("%-20s", flightMission.getMissionResult()) + "|");
                }
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                break;
            case 3:
                handleCreateMission();
                break;
            case 4:
                FlightMission flightMission = null;
                while (true) {
                    System.out.println("Please input Mission's Id:");
                    try {
                        flightMission = FlightMissionServiceImpl.getInstance().getMissionById(sc.nextLong());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Mission's name has not been updated. An exception when updating the name.");
                        continue;
                    }
                    break;
                }

                while (true) {
                    System.out.println("Please input new Mission's name:");
                    String name = sc.next();

                    if (name == null && name.equals("")) {
                        System.out.println("Please input correct name.");
                        continue;
                    }
                    flightMission.setName(name);
                    System.out.println("Mission's name has been successfully updated");
                    break;
                }
                break;
            case 5:
                handleMissionStatusUpdate();
                break;
            case 6:
                try {
                    FileParser.saveMissionsToJSONFile(new ArrayList(NassaContext.getMissions()));
                    System.out.println("The JSON file with missions has been successfully created.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("The JSON file with missions has not been created. There is an exception: " + ex.getMessage());
                }
                break;
            case 7:
                return 1;
            default:
                System.out.println("Please select a correct menu option.\n");
                printAvailableOptions();
                break;
        }
        return 0;
    }

    private void handleMissionStatusUpdate() {
        long missionId;
        long missionStatusId = -1;

        Scanner sc = new Scanner(System.in);

        System.out.println("Input mission Id: ");
        while (true) {
            try {
                missionId = sc.nextLong();
            } catch (Exception ex) {
                missionId = 0;
            }

            if (missionId <= 0) {
                System.out.println("Incorrect number. Please enter the correct number\n");
                continue;
            } else {
                break;
            }
        }

        System.out.println("Input new mission status (1 - Cancelled, 2 - Failed, 3 - Planned, 4 - In progress, 5 - Completed): ");
        while (true) {
            try {
                missionStatusId = sc.nextLong();
            } catch (Exception ex) {
                missionId = 0;
            }

            if (missionId <= 0) {
                System.out.println("Incorrect number. Please enter the correct number\n");
                continue;
            } else {
                break;
            }
        }

        try {
            MissionResult status = MissionResult.resolveById(missionStatusId);
            FlightMissionServiceImpl.getInstance().updateMissionStatus(missionId, status);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Menu: Exception when updating mission.\n");
        }
    }

    private Object printAvailableOptionsCrewMember() {
        System.out.println("\"------------------------------------------ Crew Member Menu ----------------------------------------\n" +
                "           1) View All Crew Members\n" +
                "           2) Filter Crew Members By Name\n" +
                "           3) Filter Crew Members By Rank\n" +
                "           4) Filter Crew Members By Role\n" +
                "           5) Update Crew Member's name\n" +
                "           6) Return to the main menu");
        System.out.println("?");
        return 0;
    }

    private Object handleUserInputCrewMember(Object o) {
        Scanner sc = new Scanner(System.in);
        Collection<CrewMember> crewMembers;
        CrewMemberCriteria criteria;
        NassaContext.refreshDataByTimeout();

        switch (sc.nextInt()) {
            case 1:
                crewMembers = CrewMemberServiceImpl.getInstance().findAllCrewMembers();
                System.out.println("------------------------------- All Crew Members ---------------------------");
                System.out.println("|      id      |        Name         |        Role        |      Rank      |");
                System.out.println("----------------------------------------------------------------------------");
                for (CrewMember crewMember : crewMembers) {
                    System.out.println("|" + String.format("%-14s", crewMember.getId()) + "|" + String.format("%-21s", crewMember.getName()) + "|" + String.format("%-20s", crewMember.getRole()) + "|" + String.format("%-16s", crewMember.getRank()) + "|");
                }
                System.out.println("----------------------------------------------------------------------------");
                break;
            case 2:
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.println("Please input name of the member you are looking for: ");
                    String name = scanner.nextLine();
                    if (name == null) {
                        System.out.println("Please input correct name.");
                        continue;
                    }
                    try {
                        criteria = new CrewMemberCriteria.Builder()
                                .withName(name)
                                .build();
                        crewMembers = CrewMemberServiceImpl.getInstance().findAllCrewMembersByCriteria(criteria);
                    } catch (Exception e) {
                        continue;
                    }
                    break;

                }
                System.out.println("-------------------------- Filtered Crew Members ---------------------------");
                System.out.println("|      id      |        Name         |        Role        |      Rank      |");
                System.out.println("----------------------------------------------------------------------------");
                for (CrewMember crewMember : crewMembers) {
                    System.out.println("|" + String.format("%-14s", crewMember.getId()) + "|" + String.format("%-21s", crewMember.getName()) + "|" + String.format("%-20s", crewMember.getRole()) + "|" + String.format("%-16s", crewMember.getRank()) + "|");
                }
                System.out.println("----------------------------------------------------------------------------");
                break;
            case 3:
                while (true) {
                    System.out.println("Please input Rank Id (1 - Trainee, 2 - Second officer, 3 - First officer, 4 - Captain):");
                    int rankId = sc.nextInt();
                    try {
                        criteria = new CrewMemberCriteria.Builder()
                                .withRank(Rank.resolveRankById(rankId))
                                .build();

                        crewMembers = CrewMemberServiceImpl.getInstance().findAllCrewMembersByCriteria(criteria);

                    } catch (Exception e) {
                        System.out.println("Please input correct id.");
                        continue;
                    }
                    break;
                }
                System.out.println("-------------------------- Filtered Crew Members ---------------------------");
                System.out.println("|      id      |        Name         |        Role        |      Rank      |");
                System.out.println("----------------------------------------------------------------------------");
                for (CrewMember crewMember : crewMembers) {
                    System.out.println("|" + String.format("%-14s", crewMember.getId()) + "|" + String.format("%-21s", crewMember.getName()) + "|" + String.format("%-20s", crewMember.getRole()) + "|" + String.format("%-16s", crewMember.getRank()) + "|");
                }
                System.out.println("----------------------------------------------------------------------------");
                break;
            case 4:
                while (true) {
                    System.out.println("Please input Role Id (1 - Mission Specialist, 2 - Flight Engineer, 3 - Pilot, 4 - Commander):");
                    int roleId = sc.nextInt();
                    try {
                        criteria = new CrewMemberCriteria.Builder()
                                .withRole(Role.resolveRoleById(roleId))
                                .build();

                        crewMembers = CrewMemberServiceImpl.getInstance().findAllCrewMembersByCriteria(criteria);

                    } catch (Exception e) {
                        System.out.println("Please input correct id.");
                        continue;
                    }
                    break;
                }
                System.out.println("-------------------------- Filtered Crew Members ---------------------------");
                System.out.println("|      id      |        Name         |        Role        |      Rank      |");
                System.out.println("----------------------------------------------------------------------------");
                for (CrewMember crewMember : crewMembers) {
                    System.out.println("|" + String.format("%-14s", crewMember.getId()) + "|" + String.format("%-21s", crewMember.getName()) + "|" + String.format("%-20s", crewMember.getRole()) + "|" + String.format("%-16s", crewMember.getRank()) + "|");
                }
                System.out.println("----------------------------------------------------------------------------");
                break;
            case 5:
                CrewMember crewMember = null;
                while (true) {
                    System.out.println("Please input Member's Id:");
                    long memberId = sc.nextLong();
                    try {
                        crewMember = CrewMemberServiceImpl.getInstance().getCrewMemberById(memberId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Member's name has not been updated. An exception when updating member's name.");
                        continue;
                    }
                    break;
                }

                while (true) {
                    System.out.println("Please input new Member's name:");
                    String name = sc.next();

                    if (name == null && name.equals("")) {
                        System.out.println("Please input correct name.");
                        continue;
                    }
                    crewMember.setName(name);
                    System.out.println("Member's name has been successfully updated");
                    break;
                }
                break;
            case 6:
                return 1;
            default:
                System.out.println("Please select a correct menu option.\n");
                printAvailableOptions();
                break;
        }
        return 0;
    }

    private Object printAvailableOptionsSpaceship() {
        System.out.println("\"------------------------------------------ Crew Member Menu ----------------------------------------\n" +
                "           1) View All Spaceships\n" +
                "           2) Filter Spaceships By Name\n" +
                "           3) Update spaceship's name\n" +
                "           4) Return to the main menu");
        System.out.println("?");
        return null;
    }

    private Object handleUserInputSpaceship(Object object) {
        Scanner sc = new Scanner(System.in);
        NassaContext.refreshDataByTimeout();

        switch (sc.nextInt()) {
            case 1:
                List<Spaceship> spaceships = SpaceShipServiceImpl.getInstance().findAllSpaceships();
                System.out.println("------------------ All Spaceships ----------------");
                System.out.println("|      id      |        Name         |  Distance |");
                System.out.println("--------------------------------------------------");
                for (Spaceship spaceship : spaceships) {
                    System.out.println("|" + String.format("%-14s", spaceship.getId()) + "|" + String.format("%-21s", spaceship.getName()) + "|" + String.format("%-11s", spaceship.getFlightDistance()) + "|");
                }
                System.out.println("--------------------------------------------------");
                break;
            case 2:
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.println("Please input name of the member you are looking for: ");
                    String name = scanner.nextLine();
                    if (name == null) {
                        System.out.println("Please input correct name.");
                        continue;
                    }
                    try {
                        SpaceshipCriteria criteria = new SpaceshipCriteria.Builder()
                                .withName(name)
                                .build();
                        spaceships = SpaceShipServiceImpl.getInstance().findAllSpaceshipsByCriteria(criteria);
                    } catch (Exception e) {
                        continue;
                    }
                    break;
                }
                System.out.println("---------------------- All Spaceships ------------");
                System.out.println("|      id      |        Name         |  Distance |");
                System.out.println("--------------------------------------------------");
                for (Spaceship spaceship : spaceships) {
                    System.out.println("|" + String.format("%-14s", spaceship.getId()) + "|" + String.format("%-21s", spaceship.getName()) + "|" + String.format("%-11s", spaceship.getFlightDistance()) + "|");
                }
                System.out.println("--------------------------------------------------");
                break;
            case 3:
                Spaceship spaceship = null;
                while (true) {
                    System.out.println("Please input Spaceship's Id:");
                    try {
                        spaceship = SpaceShipServiceImpl.getInstance().getSpaceshipById(sc.nextLong());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Spaceship's name has not been updated. An exception when updating name.");
                        continue;
                    }
                    break;
                }

                while (true) {
                    System.out.println("Please input new Spaceship's name:");
                    String name = sc.next();

                    if (name == null && name.equals("")) {
                        System.out.println("Please input correct name.");
                        continue;
                    }
                    spaceship.setName(name);
                    System.out.println("Spaceship's name has been successfully updated");
                    break;
                }
                break;
            case 4:
                return 1;
            default:
                System.out.println("Please select a correct menu option.\n");
                printAvailableOptions();
                break;
        }
        return 0;
    }

}
