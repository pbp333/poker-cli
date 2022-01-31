package com.isec.pokercli.model.entity.user;

import java.util.ArrayList;
import java.util.List;

public class UserUnitOfWork {

    private final List<User> created = new ArrayList<>();
    private final List<User> updated = new ArrayList<>();
    private final List<User> deleted = new ArrayList<>();

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
}
