package com.isec.pokercli.application.audit.search;

public abstract class AuditDecorator implements AuditSearch {

    protected final AuditSearch search;

    public AuditDecorator(AuditSearch search) {
        this.search = search;
    }

}
