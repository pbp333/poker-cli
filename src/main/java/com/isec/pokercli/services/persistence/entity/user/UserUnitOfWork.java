package com.isec.pokercli.services.persistence.entity.user;

import java.util.*;

public class UserUnitOfWork {

    private final List<User> created = new ArrayList<>();
    private final List<User> updated = new ArrayList<>();
    private final List<User> deleted = new ArrayList<>();

    private static final UserUnitOfWork instance = new UserUnitOfWork();

    private final Map<Long, User> idMap = new HashMap<>();
    private final Map<String, User> usernameMap = new HashMap<>();

    private UserUnitOfWork() {

    }

    public static UserUnitOfWork getInstance() {
        return instance;
    }

    public void addCreated(User entity) {
        created.add(entity);
    }

    public void addUpdated(User entity) {
        updated.add(entity);
    }

    public void addDeleted(User entity) {
        deleted.add(entity);
    }

    public void commit() {
        created.forEach(User::create);
        updated.forEach(User::update);
        deleted.forEach(User::delete);

        created.clear();
        updated.clear();
        deleted.clear();
    }

    public void track(User user) {

        if (user.getId() != null) {
            idMap.put(user.getId(), user);
        }

        usernameMap.put(user.getName(), user);
    }

    public List<User> getUsers() {

        Set<User> users = new HashSet<>(idMap.values());
        users.addAll(usernameMap.values());

        return new ArrayList<>(users);
    }

    public User getById(Long id) {
        return idMap.get(id);
    }

    public User getByUsername(String username) {
        return usernameMap.get(username);
    }

    public void removeFromCreated(User user) {
        created.remove(user);
    }

    public void removeFromUpdated(User user) {
        updated.remove(user);
    }

}
