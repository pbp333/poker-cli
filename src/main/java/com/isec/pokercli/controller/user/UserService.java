package com.isec.pokercli.controller.user;

import java.math.BigDecimal;

public interface UserService {

    void createUser(String name);

    void removeUser(String name);

    void addPayment(String user, BigDecimal amount, String PaymentMethod);
}
