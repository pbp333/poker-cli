package com.isec.pokercli.model.entity.message;

import java.util.Arrays;
import java.util.Optional;

public enum MessageStatus {
    SENT, READ;

    public static Optional<MessageStatus> getByString(String status) {
        return Arrays.stream(MessageStatus.values()).filter(e -> e.name().equals(status)).findFirst();
    }
}
