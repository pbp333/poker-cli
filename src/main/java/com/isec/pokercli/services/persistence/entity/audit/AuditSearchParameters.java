package com.isec.pokercli.services.persistence.entity.audit;

public class AuditSearchParameters {

    private final Integer numberOfMessages;
    private final String user;
    private final AuditType type;

    public AuditSearchParameters(Builder builder) {
        this.numberOfMessages = builder.numberOfMessages;
        this.user = builder.user;
        this.type = builder.type;
    }

    public Integer getNumberOfMessages() {
        return numberOfMessages;
    }

    public String getUser() {
        return user;
    }

    public AuditType getType() {
        return type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer numberOfMessages;
        private String user;
        private AuditType type;

        private Builder() {

        }

        public Builder numberOfMessages(Integer numberOfMessages) {
            this.numberOfMessages = numberOfMessages;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder type(AuditType type) {
            this.type = type;
            return this;
        }

        public AuditSearchParameters build() {
            return new AuditSearchParameters(this);
        }
    }
}
