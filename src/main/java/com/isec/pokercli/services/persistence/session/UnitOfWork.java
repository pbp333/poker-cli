package com.isec.pokercli.services.persistence.session;

import com.isec.pokercli.services.persistence.entity.game.GameUnitOfWork;
import com.isec.pokercli.services.persistence.entity.message.MessageUnitOfWork;
import com.isec.pokercli.services.persistence.entity.user.UserUnitOfWork;

public class UnitOfWork {

    private GameUnitOfWork gameUnitOfWork = GameUnitOfWork.getInstance();
    private UserUnitOfWork userUnitOfWork = UserUnitOfWork.getInstance();
    private MessageUnitOfWork messageUnitOfWork = MessageUnitOfWork.getInstance();

    private static final UnitOfWork instance = new UnitOfWork();

    private UnitOfWork() {

    }

    public static UnitOfWork getInstance() {
        return instance;
    }

    public void commit() {
        this.gameUnitOfWork.commit();
        this.userUnitOfWork.commit();
        this.messageUnitOfWork.commit();
    }
}
