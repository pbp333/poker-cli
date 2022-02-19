package com.isec.pokercli.application.audit;

import com.isec.pokercli.services.persistence.entity.audit.Audit;

public interface AuditService {

    void entry(Audit audit);
}
