package com.isec.pokercli.services.payment;

import java.math.BigDecimal;

public interface PaymentService {

    void addPayment(String user, BigDecimal amount);

    void cancelPayment(String user, BigDecimal amount);
}
