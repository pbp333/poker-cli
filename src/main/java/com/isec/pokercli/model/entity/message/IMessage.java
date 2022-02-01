package com.isec.pokercli.model.entity.message;

import java.time.LocalDateTime;

public interface IMessage {

    Long getId();

    Long getFromUserId();

    Long getToUserId();

    String getContent();

    LocalDateTime getCreatedAt();

    MessageStatus getStatus();
}
