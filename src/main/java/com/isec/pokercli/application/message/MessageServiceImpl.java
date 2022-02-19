package com.isec.pokercli.application.message;

import com.isec.pokercli.services.persistence.entity.message.Message;
import com.isec.pokercli.services.persistence.entity.user.User;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.util.Arrays;

public class MessageServiceImpl implements MessageService {

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
    }
}
