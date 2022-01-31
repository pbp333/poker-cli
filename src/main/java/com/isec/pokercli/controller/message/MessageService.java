package com.isec.pokercli.controller.message;

public interface MessageService {

    void deliverMessage(String origin, String destination, String message);

    void deleteMessage(String origin, String destination, String message);

}
