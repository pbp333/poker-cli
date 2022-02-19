package com.isec.pokercli.application.logging;

public abstract class LogDecorator implements LoggerSearch {

    protected final LoggerSearch search;

    public LogDecorator(LoggerSearch search) {
        this.search = search;
    }

}
