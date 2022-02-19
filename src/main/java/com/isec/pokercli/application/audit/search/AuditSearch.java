package com.isec.pokercli.application.audit.search;

import com.isec.pokercli.services.persistence.entity.audit.AuditSearchParameters;

public interface AuditSearch {

    AuditSearchParameters.Builder getSearchParameters();
}
