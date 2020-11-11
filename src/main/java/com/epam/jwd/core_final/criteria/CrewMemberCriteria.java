package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.CrewMember} fields
 */
public class CrewMemberCriteria extends Criteria<CrewMember> {
    private Role role = null;
    private Rank rank = null;
    private Boolean isReadyForNextMissions = null;

    public Role getRole() {
        return role;
    }

    public Rank getRank() {
        return rank;
    }

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public CrewMemberCriteria(Builder builder) {
        super(builder);
        this.role = builder.role;
        this.rank = builder.rank;
        this.isReadyForNextMissions = builder.isReadyForNextMissions;
    }

    public static class Builder extends Criteria.Builder {
        private Role role;
        private Rank rank;
        private Boolean isReadyForNextMissions;

        public Builder() {
            super();
        }

        public Builder withRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder withRank(Rank rank) {
            this.rank = rank;
            return this;
        }

        public Builder withFlagReadyForNextMissions(Boolean isReadyForNextMissions) {
            this.isReadyForNextMissions = isReadyForNextMissions;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }


        public CrewMemberCriteria build() {
            return new CrewMemberCriteria(this);
        }
    }

}
