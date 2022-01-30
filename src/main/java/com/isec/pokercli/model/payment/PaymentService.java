package com.isec.pokercli.model.payment;

import java.math.BigDecimal;

public interface PaymentService {

    void addPayment(String user, BigDecimal amount);
}
