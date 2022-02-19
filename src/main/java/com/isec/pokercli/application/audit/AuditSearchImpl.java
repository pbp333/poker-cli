package com.isec.pokercli.application.audit;

import com.isec.pokercli.services.persistence.entity.audit.Audit;
import com.isec.pokercli.services.persistence.entity.audit.AuditMapper;

public class AuditSearchImpl implements AuditService {

    @Override
    public void entry(Audit audit) {
        AuditMapper.insert(audit);
    }
}
