package com.isec.pokercli.controller.message;

import com.isec.pokercli.model.entity.message.Message;
import com.isec.pokercli.model.entity.user.User;

public class MessageServiceImpl implements MessageService {

    @Override
    public void deliverMessage(String origin, String destination, String message) {
        // Get origin user ID
        User originUser = User.getByName(origin);
        Long originUserId = originUser.getId();

        // Get destination user ID
        User destinationUser = User.getByName(destination);
        Long destinationUserId = destinationUser.getId();

        Message m = new Message();
        m.setFromUserId(originUserId);
        m.setToUserId(destinationUserId);
        m.setContent(message);
        m.create();
    }

    @Override
    public void deleteMessage(String origin, String destination, String message) {
        // Get origin user ID
        User originUser = User.getByName(origin);
        Long originUserId = originUser.getId();

        // Get destination user ID
        User destinationUser = User.getByName(destination);
        Long destinationUserId = destinationUser.getId();

        Message m = Message.getBySourceDestinationAndContent(originUserId, destinationUserId, message);
        if (m!=null) {
            m.delete();
        } else {
            System.out.println("Couldn't find the message");
        }
    }
}
