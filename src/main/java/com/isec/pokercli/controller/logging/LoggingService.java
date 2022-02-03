package com.isec.pokercli.controller.logging;

public abstract class LoggingService {

    private LoggingService service;

    public LoggingService(LoggingService service) {
        this.service = service;
    }

}
