package com.isec.pokercli.cli.controller;

import com.isec.pokercli.cli.actions.ExitAction;

public interface CliController {

    void handleAction(String[] action) throws ExitAction;

}
