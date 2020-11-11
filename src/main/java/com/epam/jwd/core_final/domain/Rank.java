package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.exception.UnknownEntityException;

public enum Rank implements BaseEntity {
    TRAINEE(1L),
    SECOND_OFFICER(2L),
    FIRST_OFFICER(3L),
    CAPTAIN(4L);

    private final Long id;

    Rank(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return resolveRankById(id).getName();
    }

    /**
     * @throws UnknownEntityException if such id does not exist
     */
    public static Rank resolveRankById(long id) {
        for (Rank rank : Rank.values()) {
            if (rank.id.equals(id)) {
                return rank;
            }
        }
        throw new UnknownEntityException("Such rank Id does not exist: " + id);
    }
}
