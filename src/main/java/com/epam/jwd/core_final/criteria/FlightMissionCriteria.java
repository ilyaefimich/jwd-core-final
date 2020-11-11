package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {
    private Long distance;
    private MissionResult missionStatus;

    public Long getDistance() {
        return distance;
    }

    public MissionResult getMissionStatus() {
        return missionStatus;
    }


    public FlightMissionCriteria(FlightMissionCriteria.Builder builder) {
        super(builder);
        this.missionStatus = builder.missionResult;
    }

    public static class Builder extends Criteria.Builder {
        private MissionResult missionResult = null;
        private Long distance;

        public Builder() {
            super();
        }

        public FlightMissionCriteria.Builder withMissionStatus(MissionResult missionStatus) {
            this.missionResult = missionStatus;
            return this;
        }

        public FlightMissionCriteria.Builder withDistance(Long distance) {
            this.distance = distance;
            return this;
        }

        public FlightMissionCriteria.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public FlightMissionCriteria.Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public FlightMissionCriteria build() {
            return new FlightMissionCriteria(this);
        }
    }


}
