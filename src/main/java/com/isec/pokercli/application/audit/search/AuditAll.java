package com.isec.pokercli.application.audit.search;

import com.isec.pokercli.services.persistence.entity.audit.AuditSearchParameters;

public class AuditAll implements AuditSearch {
    @Override
    public AuditSearchParameters.Builder getSearchParameters() {
        return AuditSearchParameters.builder();
    }
}
