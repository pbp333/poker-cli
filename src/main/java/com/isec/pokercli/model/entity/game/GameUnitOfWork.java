package com.isec.pokercli.model.entity.game;

import java.util.ArrayList;
import java.util.List;

public class GameUnitOfWork {

    private final List<Game> created = new ArrayList<>();
    private final List<Game> updated = new ArrayList<>();
    private final List<Game> deleted = new ArrayList<>();

    public void addCreated(Game entity) {
        created.add(entity);
    }

    public void addUpdated(Game entity) {
        deleted.remove(entity);
        updated.add(entity);
    }

    public void addDeleted(Game entity) {
        created.remove(entity);
        updated.remove(entity);
        deleted.add(entity);
    }

}
