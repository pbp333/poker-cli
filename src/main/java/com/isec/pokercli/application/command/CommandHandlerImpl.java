package com.isec.pokercli.application.command;

import com.isec.pokercli.application.api.Command;
import com.isec.pokercli.application.api.CommandHandler;

import java.util.Stack;

public class CommandHandlerImpl implements CommandHandler {

    private Stack<Command> undo = new Stack<>();
    private Stack<Command> redo = new Stack<>();

    @Override
    public void apply(Command command) {
        command.execute();
        undo.push(command);
        redo.clear();
    }

    @Override
    public void undo() {
        var command = undo.pop();
        if (command != null) {
            command.undo();
            redo.push(command);
        }

    }

    @Override
    public void redo() {
        var command = redo.pop();
        if (command != null) {
            command.execute();
            undo.push(command);
        }
    }
}
