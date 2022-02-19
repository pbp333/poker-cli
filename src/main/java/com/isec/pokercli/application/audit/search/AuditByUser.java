package com.isec.pokercli.application.audit.search;

import com.isec.pokercli.services.persistence.entity.audit.AuditSearchParameters;

public class AuditByUser extends AuditDecorator {

    private final String user;

    public AuditByUser(AuditSearch search, String user) {
        super(search);
        this.user = user;
    }

    @Override
    public AuditSearchParameters.Builder getSearchParameters() {
        return search.getSearchParameters().user(this.user);
    }
}
