package com.isec.pokercli.controller.message;

import com.isec.pokercli.model.entity.message.Message;
import com.isec.pokercli.model.entity.user.User;
import com.isec.pokercli.model.session.DbSessionManager;

public class MessageServiceImpl implements MessageService {

    @Override
    public void deliverMessage(String origin, String destination, String message) {

        var originUser = User.getByUsername(origin);
        if (originUser == null) {
            throw new IllegalArgumentException("User does not exist - " + origin);
        }

        var destinationUser = User.getByUsername(destination);
        if (destinationUser == null) {
            throw new IllegalArgumentException("User does not exist - " + destination);
        }
        Message.from(originUser.getId(), destinationUser.getId(), message);

        DbSessionManager.getUnitOfWork().commit();
    }

    @Override
    public void deleteMessage(String origin, String destination, String message) {

    }
}
