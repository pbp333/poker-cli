package com.isec.pokercli.model.entity.user;

import com.isec.pokercli.model.session.DbSessionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User implements IUser {

    private Long id;
    private String name;
    private BigDecimal balance;
    private BigDecimal virtualBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private static UserUnitOfWork userUnitOfWork = UserUnitOfWork.getInstance();

    private User() {

    }

    public static User from(String username) {

        var user = new User();

        user.name = username;
        user.balance = BigDecimal.ZERO;
        user.virtualBalance = BigDecimal.ZERO;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();

        userUnitOfWork.addCreated(user);

        return user;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public BigDecimal getVirtualBalance() {
        return virtualBalance;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        userUnitOfWork.addUpdated(this);
    }

    @Override
    public void removeBalance(BigDecimal amount) {

        if (amount.compareTo(balance) < 1) {
            throw new IllegalStateException("User does not have sufficient balance");
        }

        this.balance = this.balance.subtract(amount);
        userUnitOfWork.addUpdated(this);
    }

    public static List<User> getAll() {
        List<User> result = new ArrayList<>();
        try {
            final String sql = "SELECT id, name, balance, virtual_balance, created_at, updated_at FROM user";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User user = mapUserFromDb(rs);
                result.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static User getById(Long id) {

        try {
            final String sql = "SELECT id, name, balance, virtual_balance, created_at, updated_at FROM user WHERE id=?";
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

        try {

            final String sql = "SELECT id, name, balance, virtual_balance, created_at, updated_at FROM cliuser WHERE name=?";
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
        user.virtualBalance = rs.getBigDecimal(4);
        user.createdAt = rs.getTimestamp(5).toLocalDateTime();
        user.updatedAt = rs.getTimestamp(6).toLocalDateTime();

        return user;
    }

    protected int create() {
        try {
            final String sql = "INSERT INTO cliuser(name, balance, virtual_balance) VALUES (?, ?, ?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, getName());
            pstmt.setBigDecimal(2, getBalance());
            pstmt.setBigDecimal(3, getVirtualBalance());
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
            final String sql = "UPDATE cliuser SET name=?, balance=?, virtual_balance, updated_at where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, getName());
            pstmt.setBigDecimal(2, getBalance());
            pstmt.setBigDecimal(3, getVirtualBalance());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
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
        this.userUnitOfWork.addDeleted(this);
    }
}
