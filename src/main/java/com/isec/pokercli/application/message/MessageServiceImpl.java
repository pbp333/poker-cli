package com.isec.pokercli.application.message;

import com.isec.pokercli.application.audit.AuditSearchImpl;
import com.isec.pokercli.application.audit.AuditService;
import com.isec.pokercli.services.persistence.entity.audit.Audit;
import com.isec.pokercli.services.persistence.entity.audit.AuditType;
import com.isec.pokercli.services.persistence.entity.message.Message;
import com.isec.pokercli.services.persistence.entity.user.User;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.util.Arrays;

public class MessageServiceImpl implements MessageService {

    private final AuditService auditService;

    public MessageServiceImpl() {
        this.auditService = new AuditSearchImpl();
    }

    @Override
    public void deliverMessage(String origin, String destination, String content) {

        var originUser = User.getByUsername(origin)
                .orElseThrow(() -> new IllegalArgumentException("Origin User is not valid"));

        var destinationUser = User.getByUsername(destination)
                .orElseThrow(() -> new IllegalArgumentException("Destination User is not valid"));

        var message = Message.from(originUser.getId(), destinationUser.getId(), content);

        if (destinationUser.isOnline()) {
            destinationUser.read(Arrays.asList(message));
        }

        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.MESSAGE).owner(originUser).log("Message sent").build());
    }

    @Override
    public void deleteMessage(String origin, String destination, String content) {

        var originUser = User.getByUsername(origin)
                .orElseThrow(() -> new IllegalArgumentException("Origin User is not valid"));

        var destinationUser = User.getByUsername(destination)
                .orElseThrow(() -> new IllegalArgumentException("Destination User is not valid"));

        var message = Message.getByOriginAndDestinationAndMessage(originUser.getId(), destinationUser.getId(), content);

        if (message == null) {
            throw new IllegalArgumentException("Message does not exist");
        }

        message.remove();

        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.MESSAGE).owner(originUser).log("Message deleted").build());
    }
}
