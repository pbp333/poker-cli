package com.isec.pokercli.services.persistence.entity.message;

import java.time.LocalDateTime;

public interface IMessage {

    Long getId();

    Long getFromUserId();

    Long getToUserId();

    String getContent();

    LocalDateTime getCreatedAt();

    MessageStatus getStatus();
}
