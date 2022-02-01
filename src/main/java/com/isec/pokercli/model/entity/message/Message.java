package com.isec.pokercli.model.entity.message;

import com.isec.pokercli.model.entity.DbSessionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Message implements IMessage {
    Long id;
    Long fromUserId;
    Long toUserId;
    String content;
    LocalDateTime createdAt;
    MessageStatus status;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    @Override
    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public static List<Message> getAll() {
        List<Message> result = new ArrayList<Message>();
        try {
            final String sql = "SELECT id, from_user_id, to_user_id, content, created_at, status FROM message";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Message message = map(rs);
                result.add(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Message getById(Long id) {
        Message result = null;
        try {
            final String sql = "SELECT id, from_user_id, to_user_id, content, created_at, status FROM message WHERE id = ?";
            Connection conn = DbSessionManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = map(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static Message map(ResultSet rs) throws SQLException {
        Message m = new Message();
        m.setId(rs.getLong(1));
        m.setFromUserId(rs.getLong(2));
        m.setToUserId(rs.getLong(3));
        m.setContent(rs.getString(4));
        m.setCreatedAt(rs.getTimestamp(5).toLocalDateTime());
        final String statusStr = rs.getString(6);
        Optional<MessageStatus> status = MessageStatus.getByString(statusStr);
        if (status.isPresent()) {
            m.setStatus(status.get());
        }
        return m;
    }

    public int create() {
        try {
            final String sql = "INSERT INTO message(from_user_id, to_user_id, content, status) VALUES (?, ?, ?, ?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, getFromUserId());
            pstmt.setLong(2, getToUserId());
            pstmt.setString(3, getContent());
            pstmt.setString(4, getStatus().name());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int key = rs.getInt(1);
            setId(Long.valueOf(key));

            return key;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void update() {
        try {
            final String sql = "UPDATE message SET from_user_id=?, to_user_id=?, content=?, status=? where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, getFromUserId());
            pstmt.setLong(2, getToUserId());
            pstmt.setString(3, getContent());
            pstmt.setString(4, getStatus().name());
            pstmt.setLong(5, getId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            final String sql = "DELETE FROM message where id = ?";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, getId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
