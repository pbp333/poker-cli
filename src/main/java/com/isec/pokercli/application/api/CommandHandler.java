package com.isec.pokercli.application.api;

public interface CommandHandler {

    void apply(Command command);

    void undo();

    void redo();
}
