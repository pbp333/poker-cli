package com.isec.pokercli.services.persistence.entity.audit;

import com.isec.pokercli.services.persistence.entity.user.User;
import com.isec.pokercli.services.persistence.session.DbSessionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AuditMapper {

    public static void insert(Audit audit) {

        var user = User.getByUsername(audit.getOwner().getName())
                .orElseThrow(() -> new IllegalArgumentException("User is not valid"));

        try {
            final String sql = "INSERT INTO audit(owner_id, type, log) VALUES (?, ?, ?)";

            Connection conn = DbSessionManager.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, user.getId());
            pstmt.setString(2, audit.getType().name());
            pstmt.setString(3, audit.getLog());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<Audit> findBy(AuditSearchParameters searchParameters) {

        List<Audit> audits = new ArrayList<>();

        try {
            Connection conn = DbSessionManager.getConnection();

            final StringBuilder sqlBuilder = new StringBuilder("SELECT id, owner_id, type, log, created_at FROM audit WHERE 1 = 1 ");

            if (searchParameters.getUser() != null && !searchParameters.getUser().isBlank()) {

                User user = User.getByUsername(searchParameters.getUser())
                        .orElseThrow(() -> new IllegalArgumentException("User is not valid"));

                sqlBuilder.append("AND owner_id = ").append(user.getId());
            }

            if (searchParameters.getType() != null) {
                sqlBuilder.append("AND type = ").append(searchParameters.getType().name());
            }

            if (searchParameters.getNumberOfMessages() != null) {
                sqlBuilder.append("LIMIT ").append(searchParameters.getNumberOfMessages());
            }

            PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString(), Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                User user = User.getByUsername(searchParameters.getUser())
                        .orElseThrow(() ->
                                new IllegalStateException("User present in audit is no longer present, this should never happen"));

                Audit audit = Audit.builder().id(rs.getLong(1)).owner(user)
                        .type(LogType.valueOf(rs.getString(3))).log(rs.getString(4))
                        .createdAt(rs.getTimestamp(5).toLocalDateTime()).build();
                audits.add(audit);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return audits;

    }

}
