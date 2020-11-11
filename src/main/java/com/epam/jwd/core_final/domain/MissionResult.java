package com.epam.jwd.core_final.domain;

public enum MissionResult {
    CANCELLED(1L),
    FAILED(2L),
    PLANNED(3L),
    IN_PROGRESS(4L),
    COMPLETED(5L);

    private final Long id;

    MissionResult(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return resolveById(id).getName();
    }

    public static MissionResult resolveById(Long id) throws RuntimeException {
        for (MissionResult status : MissionResult.values()) {
            if (status.id.equals(id)) {
                return status;
            }
        }
        throw new RuntimeException();
    }

    }
