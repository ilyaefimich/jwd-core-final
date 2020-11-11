package com.epam.jwd.core_final.service;

import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface FlightMissionService {

    List<FlightMission> findAllMissions();

    List<FlightMission> findAllMissionsByCriteria(Criteria<? extends FlightMission> criteria);

    Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria);

    FlightMission updateSpaceshipDetails(FlightMission flightMission) throws ServiceException;

    FlightMission createMission(FlightMission flightMission);
}
