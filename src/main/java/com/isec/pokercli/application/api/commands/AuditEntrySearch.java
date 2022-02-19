package com.isec.pokercli.application.api.commands;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.audit.search.*;
import com.isec.pokercli.services.persistence.entity.audit.AuditMapper;
import com.isec.pokercli.services.persistence.entity.audit.AuditType;

public class AuditEntrySearch implements Command {

    private final Integer numberOfMessages;
    private final String username;
    private final String type;

    public AuditEntrySearch(Integer numberOfMessages, String username, String type) {
        this.numberOfMessages = numberOfMessages;
        this.username = username;
        this.type = type;
    }


    @Override
    public void execute() {

        AuditSearch search = new AuditAll();

        if (this.numberOfMessages != null) {
            search = new AuditLast(search, numberOfMessages);
        }

        if (this.type != null && !this.type.isBlank()) {
            search = new AuditByType(search, AuditType.valueOf(type));
        }

        if (this.username != null && !this.username.isBlank()) {
            search = new AuditByUser(search, username);
        }

        AuditMapper.findBy(search.getSearchParameters().build()).forEach(System.out::println);

    }

    @Override
    public void undo() {
        throw new UnsupportedOperationException("Search cannot be undone");
    }
}
