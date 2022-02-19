package com.isec.pokercli.application.audit.search;

import com.isec.pokercli.services.persistence.entity.audit.AuditSearchParameters;

public class AuditLast extends AuditDecorator {

    private final int numberOfMessages;

    public AuditLast(AuditSearch search, int numberOfMessages) {
        super(search);
        this.numberOfMessages = numberOfMessages;
    }


    @Override
    public AuditSearchParameters.Builder getSearchParameters() {
        return AuditSearchParameters.builder().numberOfMessages(numberOfMessages);
    }
}
