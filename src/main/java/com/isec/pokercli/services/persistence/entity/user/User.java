package com.isec.pokercli.services.persistence.entity.user;

import com.isec.pokercli.services.persistence.entity.message.Message;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class User {

    private Long id;
    private String name;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean online;

    private static UserUnitOfWork unitOfWork = UserUnitOfWork.getInstance();

    private User() {

    }

    public static User from(String username) {

        var user = new User();

        user.name = username;
        user.balance = BigDecimal.ZERO;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();

        unitOfWork.addCreated(user);

        return user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isOnline() {
        return online;
    }

    public void login() {
        this.online = true;
        unitOfWork.track(this);
    }

    public void logout() {
        this.online = false;
    }

    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        unitOfWork.addUpdated(this);
    }

    public void removeBalance(BigDecimal amount) {

        if (amount.compareTo(balance) < 1) {
            throw new IllegalStateException("User does not have sufficient balance");
        }

        this.balance = this.balance.subtract(amount);
        unitOfWork.addUpdated(this);
    }

    public static List<User> getAll() {

        List<User> users = unitOfWork.getUsers();
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        try {
            String sql = "SELECT id, name, balance, created_at, updated_at FROM user";

            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User user = mapUserFromDb(rs);
                if (!userIds.contains(user.getId())) {
                    users.add(user);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public static User getById(Long id) {

        var user = unitOfWork.getById(id);

        if (user != null) {
            return user;
        }

        try {
            final String sql = "SELECT id, name, balance, created_at, updated_at FROM cliuser " +
                    "WHERE id=?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapUserFromDb(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static User getByUsername(String username) {

        var user = unitOfWork.getByUsername(username);

        if (user != null) {
            return user;
        }

        try {

            final String sql = "SELECT id, name, balance, created_at, updated_at FROM cliuser " +
                    "WHERE name=?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapUserFromDb(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static User mapUserFromDb(ResultSet rs) throws SQLException {
        User user = new User();
        user.id = Long.valueOf(rs.getInt(1));
        user.name = rs.getString(2);
        user.balance = rs.getBigDecimal(3);
        user.createdAt = rs.getTimestamp(4).toLocalDateTime();
        user.updatedAt = rs.getTimestamp(5).toLocalDateTime();

        return user;
    }

    protected int create() {
        try {
            final String sql = "INSERT INTO cliuser(name, balance) VALUES (?, ?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, getName());
            pstmt.setBigDecimal(2, getBalance());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            this.id = Long.valueOf(key);

            return key;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected void update() {
        try {
            final String sql = "UPDATE cliuser SET name = ?, balance = ?, updated_at = ? " +
                    "WHERE id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, getName());
            pstmt.setBigDecimal(2, getBalance());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(4, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void delete() {
        try {
            final String sql = "DELETE FROM cliuser where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, getId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove() {
        this.unitOfWork.addDeleted(this);
    }

    public void read(List<Message> messages) {
        messages.stream().forEach(message -> {
            var origin = User.getById(message.getFromUserId());
            System.out.println("From - " + origin.getName() + ": " + message.getContent());
            message.markAsRead();
        });
    }
}
