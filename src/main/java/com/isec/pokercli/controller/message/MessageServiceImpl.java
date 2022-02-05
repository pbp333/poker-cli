package com.isec.pokercli.controller.message;

import com.isec.pokercli.model.entity.message.Message;
import com.isec.pokercli.model.entity.user.User;
import com.isec.pokercli.model.session.DbSessionManager;

import java.util.Arrays;

public class MessageServiceImpl implements MessageService {

    @Override
    public void deliverMessage(String origin, String destination, String content) {

        var originUser = User.getByUsername(origin);
        if (originUser == null) {
            throw new IllegalArgumentException("User does not exist - " + origin);
        }

        var destinationUser = User.getByUsername(destination);
        if (destinationUser == null) {
            throw new IllegalArgumentException("User does not exist - " + destination);
        }
        var message = Message.from(originUser.getId(), destinationUser.getId(), content);

        if (destinationUser.isOnline()) {
            destinationUser.read(Arrays.asList(message));
        }

        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void deleteMessage(String origin, String destination, String content) {

        var originUser = User.getByUsername(origin);
        if (originUser == null) {
            throw new IllegalArgumentException("User does not exist - " + origin);
        }

        var destinationUser = User.getByUsername(destination);
        if (destinationUser == null) {
            throw new IllegalArgumentException("User does not exist - " + destination);
        }

        var message = Message.getByOriginAndDestinationAndMessage(originUser.getId(), destinationUser.getId(), content);

        if (message == null) {
            throw new IllegalArgumentException("Message does not exist");
        }

        message.remove();

        DbSessionManager.getUnitOfWork().commit();
    }
}
