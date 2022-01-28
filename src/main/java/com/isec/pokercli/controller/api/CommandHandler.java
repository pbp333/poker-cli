package com.isec.pokercli.controller.api;

public interface CommandHandler {

    void apply(Command command);
    void undo();
    void redo();
}
