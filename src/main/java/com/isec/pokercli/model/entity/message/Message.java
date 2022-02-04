package com.isec.pokercli.model.entity.message;

import com.isec.pokercli.model.session.DbSessionManager;

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

    private static MessageUnitOfWork unitOfWork = MessageUnitOfWork.getInstance();

    private Message() {

    }

    public static Message from(Long fromUserId, Long toUserId, String content) {

        var message = new Message();

        message.fromUserId = fromUserId;
        message.toUserId = toUserId;
        message.content = content;
        message.createdAt = LocalDateTime.now();

        message.status = MessageStatus.SENT;

        unitOfWork.addCreated(message);

        return message;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getFromUserId() {
        return fromUserId;
    }

    @Override
    public Long getToUserId() {
        return toUserId;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public MessageStatus getStatus() {
        return status;
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
        m.id = rs.getLong(1);
        m.fromUserId = rs.getLong(2);
        m.toUserId = rs.getLong(3);
        m.content = rs.getString(4);
        m.createdAt = rs.getTimestamp(5).toLocalDateTime();
        final String statusStr = rs.getString(6);
        Optional<MessageStatus> status = MessageStatus.getByString(statusStr);
        if (status.isPresent()) {
            m.status = status.get();
        }
        return m;
    }

    protected Long create() {
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
            this.id = Long.valueOf(key);

            return this.id;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1L;
    }

    protected void update() {
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

    protected void delete() {
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

    public void remove() {
        unitOfWork.addDeleted(this);
    }
}
