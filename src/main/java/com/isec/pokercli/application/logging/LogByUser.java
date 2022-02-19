package com.isec.pokercli.application.logging;

import com.isec.pokercli.services.persistence.entity.audit.AuditSearchParameters;

public class LogByUser extends LogDecorator {

    private final String user;

    public LogByUser(LoggerSearch search, String user) {
        super(search);
        this.user = user;
    }

    @Override
    public AuditSearchParameters.Builder getSearchParameters() {
        return search.getSearchParameters().user(this.user);
    }
}
