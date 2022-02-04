package com.isec.pokercli.model.entity.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserUnitOfWork {

    private final List<User> created = new ArrayList<>();
    private final List<User> updated = new ArrayList<>();
    private final List<User> deleted = new ArrayList<>();

    private static final UserUnitOfWork instance = new UserUnitOfWork();

    private final Map<Long, User> idMap = new HashMap<>();

    private UserUnitOfWork() {

    }

    public static UserUnitOfWork getInstance() {
        return instance;
    }

    public void addCreated(User entity) {
        created.add(entity);
    }

    public void addUpdated(User entity) {
        deleted.remove(entity);
        updated.add(entity);
    }

    public void addDeleted(User entity) {
        created.remove(entity);
        updated.remove(entity);
        deleted.add(entity);
    }

    public void commit() {
        created.stream().forEach(User::create);
        updated.stream().forEach(User::update);
        deleted.stream().forEach(User::delete);

        created.clear();
        updated.clear();
        deleted.clear();
    }
}
