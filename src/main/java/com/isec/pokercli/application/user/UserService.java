package com.isec.pokercli.application.user;

import java.math.BigDecimal;

public interface UserService {

    void createUser(String name);

    void removeUser(String name);

    void addPayment(String user, BigDecimal amount, String paymentMethod);

    void login(String username);

    void logout(String username);
}
