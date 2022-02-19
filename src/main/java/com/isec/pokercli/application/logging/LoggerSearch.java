package com.isec.pokercli.application.logging;

import com.isec.pokercli.services.persistence.entity.audit.AuditSearchParameters;

public interface LoggerSearch {

    AuditSearchParameters.Builder getSearchParameters();
}
