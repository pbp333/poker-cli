package com.isec.pokercli.application.logging;

public abstract class LoggingService {

    private LoggingService service;

    public LoggingService(LoggingService service) {
        this.service = service;
    }

}
