package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.Spaceship;

/**
 * Should be a builder for {@link Spaceship} fields
 */
public class SpaceshipCriteria extends Criteria<Spaceship> {
    private String name;
    private Long flightDistance;
    private Boolean isReadyForNextMissions;

    public String getName() {
        return name;
    }


    public Long getDistance() {
        return flightDistance;
    }

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;

    }

    public SpaceshipCriteria(Builder builder) {
        super(builder);
        this.flightDistance = builder.flightDistance;
        this.isReadyForNextMissions = builder.isReadyForNextMissions;
        this.name = builder.name;
    }

    public static class Builder extends Criteria.Builder {
        private String name;
        private Long flightDistance;
        private Boolean isReadyForNextMissions;

        public Builder() {
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withFlightDistance(Long flightDistance) {
            this.flightDistance = flightDistance;
            return this;
        }


        public Builder withFlagReadyForNextMissions(Boolean isReadyForNextMissions) {
            this.isReadyForNextMissions = isReadyForNextMissions;
            return this;
        }

        public SpaceshipCriteria build() {
            return new SpaceshipCriteria(this);
        }
    }

}
