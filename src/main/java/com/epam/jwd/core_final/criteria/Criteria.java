package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;

/**
 * Should be a builder for {@link BaseEntity} fields
 */
public abstract class Criteria<T extends BaseEntity> {
    protected String name = null;
    protected Long id = null;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Criteria(Criteria.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public static class Builder {
        protected String name;
        protected Long id;

        public Builder() {
        }

        public Criteria.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Criteria.Builder withId(Long id) {
            this.id = id;
            return this;
        }
    }
}
