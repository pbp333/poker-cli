package com.isec.pokercli.services.persistence.entity.audit;

import com.isec.pokercli.services.persistence.entity.user.User;

import java.time.LocalDateTime;

public class Audit {

    private final Long id;
    private final User owner;
    private final LogType type;
    private final String log;
    private final LocalDateTime createdAt;

    private Audit(Builder builder) {
        this.id = builder.id;
        this.owner = builder.owner;
        this.type = builder.type;
        this.log = builder.log;
        this.createdAt = builder.createdAt;
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public LogType getType() {
        return type;
    }

    public String getLog() {
        return log;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User owner;
        private LogType type;
        private String log;
        private LocalDateTime createdAt;

        private Builder() {

        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public Builder type(LogType type) {
            this.type = type;
            return this;
        }

        public Builder log(String log) {
            this.log = log;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Audit build() {
            return new Audit(this);
        }
    }
}
