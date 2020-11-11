package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.exception.ServiceException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.service.CrewService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CrewMemberServiceImpl implements CrewService {

    private static volatile CrewMemberServiceImpl instance;

    private CrewMemberServiceImpl() {
    }

    /**
     * lazy singleton implementation
     * @return instance of CrewMemberServiceImpl
     */
    public static CrewMemberServiceImpl getInstance() {
        if (instance == null) {
            synchronized (CrewMemberServiceImpl.class) {
                if (instance == null) {
                    instance = new CrewMemberServiceImpl();
                }
            }
        }
        return instance;
    }

    /**
     * Return list of all existing members
     * @return - returns list of unmodifiable List
     */
    @Override
    public List<CrewMember> findAllCrewMembers() {
        return new ArrayList(NassaContext.getCrewMembers());
    }

    public int countFreeCrewMembersByRole(Role role) {
        CrewMemberCriteria criteria = new CrewMemberCriteria.Builder()
                .withRole(role)
                .withFlagReadyForNextMissions(true).build();
        return findAllCrewMembersByCriteria(criteria).size();
    }

    @Override
    public List<CrewMember> findAllCrewMembersByCriteria(Criteria<? extends CrewMember> criteria) {
        CrewMemberCriteria crewMemberCriteria = (CrewMemberCriteria) criteria;

        List<CrewMember> result = (List<CrewMember>) NassaContext.getCrewMembers();
        result = result
                .stream()
                .filter(CrewMember -> crewMemberCriteria.getName() == null || (CrewMember.getName().toLowerCase().indexOf(crewMemberCriteria.getName().toLowerCase()) != -1 ))
                .filter(CrewMember -> crewMemberCriteria.getRole() == null || (crewMemberCriteria.getRole() != null && CrewMember.getRole().equals(crewMemberCriteria.getRole())))
                .filter(CrewMember -> crewMemberCriteria.getRank() == null || (crewMemberCriteria.getRank() != null && CrewMember.getRank().getId() == (crewMemberCriteria.getRank().getId())))
                .filter(CrewMember -> crewMemberCriteria.getReadyForNextMissions() == null || (crewMemberCriteria.getReadyForNextMissions() != null && CrewMember.isReadyForNextMissions() == crewMemberCriteria.getReadyForNextMissions()))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(Criteria<? extends CrewMember> criteria) {
        List<CrewMember> result = findAllCrewMembersByCriteria(criteria);
        return result.size() > 0 ? Optional.of(result.get(0)) : Optional.empty();
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember pcrewMember) throws ServiceException {
        try {
            CrewMember crewMember = getCrewMemberById(pcrewMember.getId());
            Optional<CrewMember> optional = Optional.ofNullable(crewMember);
            if (!optional.isEmpty()) {
                crewMember.setName(pcrewMember.getName());
                crewMember.setRole(crewMember.getRole());
                crewMember.setRank(crewMember.getRank());
                crewMember.setReadyForNextMissions(crewMember.isReadyForNextMissions());
            } else {
                System.out.println("A Member with Id = " + pcrewMember.getId() + " was not found. The Member has not been updated!");
            }
            return crewMember;
        } catch (Exception e) {
            throw new ServiceException("CrewMemberServiceImpl: An exception when updating the member with id = " + pcrewMember.getId());
        }
    }

    @Override
    public void assignCrewMemberOnMission(FlightMission flightMission, CrewMember crewMember) throws ServiceException, RuntimeException {
        short numberOfMembersRequired = flightMission.getAssignedSpaceship().getNumberOfRequiredMembersByRole(crewMember.getRole());
        int numberOfMembersAlreadyAssigned = flightMission.getAssignedCrewMembers().size();
        if (numberOfMembersAlreadyAssigned < numberOfMembersRequired) {
            flightMission.getAssignedCrewMembers().add(crewMember);
        } else {
            throw new ServiceException("CrewMemberServiceImpl: Unable to assign Member to the Mission (id = " + flightMission.getId() + "). The specified mission has already been completed.");
        }
    }

    @Override
    public CrewMember createCrewMember(CrewMember crewMember) throws RuntimeException, ServiceException {
        return createCrewMember(crewMember.getName(), crewMember.getRank().getId(), crewMember.getRole().getId(), crewMember.isReadyForNextMissions());
    }

    public CrewMember createCrewMember(String name, long rankId, long roleId, boolean isReadyForNextMissions) throws ServiceException {
        if (memberWithNameAlreadyExists(name)) {
            throw new ServiceException("CrewMemberServiceImpl: Cannot create CrewMember. A member with such a name already exists.");
        }
        return (new CrewMemberFactory()).create(name, rankId, roleId, isReadyForNextMissions);
    }

    private boolean memberWithNameAlreadyExists(String name) {
        CrewMemberCriteria criteria = new CrewMemberCriteria.Builder()
                .withName(name)
                .build();
        Optional<CrewMember> optional = findCrewMemberByCriteria(criteria);
        return optional.isEmpty() ? false : true;
    }

    public CrewMember getCrewMemberById(long memberId) throws ServiceException {
        Collection<CrewMember> crewMembers = NassaContext.getCrewMembers();
        for(CrewMember crewMember : crewMembers) {
            if (crewMember.getId() == memberId) {
                return crewMember;
            }
        }
        return null;
    }

}
