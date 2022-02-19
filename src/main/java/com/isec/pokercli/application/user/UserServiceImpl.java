package com.isec.pokercli.application.user;

import com.isec.pokercli.application.audit.AuditSearchImpl;
import com.isec.pokercli.application.audit.AuditService;
import com.isec.pokercli.services.payment.PaymentServiceFactory;
import com.isec.pokercli.services.persistence.entity.audit.Audit;
import com.isec.pokercli.services.persistence.entity.audit.AuditType;
import com.isec.pokercli.services.persistence.entity.message.Message;
import com.isec.pokercli.services.persistence.entity.user.User;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService {

    private final AuditService auditService;

    public UserServiceImpl() {
        this.auditService = new AuditSearchImpl();
    }

    @Override
    public void createUser(String username) {

        User user = User.from(username);
        user.login();

        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.USER).owner(user).log("User created").build());
    }

    @Override
    public void removeUser(String username) {

        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        user.remove();
        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.USER).owner(user).log("User removed").build());
    }

    @Override
    public void addPayment(String username, BigDecimal amount, String paymentMethod) {

        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        BigDecimal amountAfterCut = amount.multiply(BigDecimal.valueOf(0.95));

        PaymentServiceFactory.buildPaymentService(paymentMethod).addPayment(username, amountAfterCut);
        user.addBalance(amountAfterCut);
        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.USER).owner(user).log("User payment added").build());
    }

    @Override
    public void login(String username) {
        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"));
        user.login();

        user.read(Message.getNotReadByDestination(user.getId()));
        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.USER).owner(user).log("User login").build());

    }

    @Override
    public void logout(String username) {
        var user = User.getByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        user.logout();
        DbSessionManager.getUnitOfWork().commit();

        auditService.entry(Audit.builder().type(AuditType.USER).owner(user).log("User logout").build());
    }
}
