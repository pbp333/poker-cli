package com.isec.pokercli.application.logging;

import com.isec.pokercli.services.persistence.entity.audit.AuditSearchParameters;

public class LogLast implements LoggerSearch {

    private final int numberOfMessages;

    public LogLast(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }


    @Override
    public AuditSearchParameters.Builder getSearchParameters() {
        return AuditSearchParameters.builder().numberOfMessages(numberOfMessages);
    }
}
