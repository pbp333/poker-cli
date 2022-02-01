package com.isec.pokercli.model.entity.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IUser {

    Long getId();

    String getName();

    BigDecimal getBalance();

    BigDecimal getVirtualBalance();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

}
