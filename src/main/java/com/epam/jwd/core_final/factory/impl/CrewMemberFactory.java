package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.factory.EntityFactory;

public class CrewMemberFactory implements EntityFactory<CrewMember> {

    @Override
    public CrewMember create(Object... args) {
        CrewMember crewMember = new CrewMember((String)args[0]);
        crewMember.setRank(Rank.resolveRankById((Long)args[1]));
        crewMember.setRole(Role.resolveRoleById((Long)args[2]));
        crewMember.setReadyForNextMissions((Boolean)args[3]);
        return crewMember;

    }
}
