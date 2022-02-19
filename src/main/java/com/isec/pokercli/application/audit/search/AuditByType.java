package com.isec.pokercli.application.audit.search;

import com.isec.pokercli.services.persistence.entity.audit.AuditSearchParameters;
import com.isec.pokercli.services.persistence.entity.audit.AuditType;

public class AuditByType extends AuditDecorator {

    private final AuditType type;

    public AuditByType(AuditSearch search, AuditType type) {
        super(search);
        this.type = type;
    }


    @Override
    public AuditSearchParameters.Builder getSearchParameters() {
        return search.getSearchParameters().type(this.type);
    }
}
