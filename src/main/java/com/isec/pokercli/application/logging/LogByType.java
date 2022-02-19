package com.isec.pokercli.application.logging;

import com.isec.pokercli.services.persistence.entity.audit.AuditSearchParameters;
import com.isec.pokercli.services.persistence.entity.audit.LogType;

public class LogByType extends LogDecorator {

    private final LogType type;

    public LogByType(LoggerSearch search, LogType type) {
        super(search);
        this.type = type;
    }


    @Override
    public AuditSearchParameters.Builder getSearchParameters() {
        return search.getSearchParameters().type(this.type);
    }
}
