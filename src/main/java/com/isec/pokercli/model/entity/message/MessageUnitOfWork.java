package com.isec.pokercli.model.entity.message;

import java.util.ArrayList;
import java.util.List;

public class MessageUnitOfWork {

    private final List<Message> created = new ArrayList<>();
    private final List<Message> updated = new ArrayList<>();
    private final List<Message> deleted = new ArrayList<>();

    private static final MessageUnitOfWork instance = new MessageUnitOfWork();

    private MessageUnitOfWork() {

    }

    public static MessageUnitOfWork getInstance() {
        return instance;
    }

    public void addCreated(Message entity) {
        created.add(entity);
    }

    public void addUpdated(Message entity) {
        deleted.remove(entity);
        updated.add(entity);
    }

    public void addDeleted(Message entity) {
        created.remove(entity);
        updated.remove(entity);
        deleted.add(entity);
    }

    public void commit() {
        created.stream().forEach(Message::create);
        updated.stream().forEach(Message::update);
        deleted.stream().forEach(Message::delete);

        created.clear();
        updated.clear();
        deleted.clear();
    }
}
