package com.example.forum.dao;

import com.example.forum.model.Topic;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class JdbcTopicDAO implements TopicDAO {

    @Resource(lookup = "jdbc/forumDS")
    private DataSource dataSource;

    @Override
    public Long save(Topic topic) {
        String sql = "INSERT INTO topics (title, description, closed, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, topic.getTitle());
            ps.setString(2, topic.getDescription());
            ps.setBoolean(3, topic.isClosed());
            ps.setTimestamp(4, new Timestamp(topic.getCreatedAt().getTime()));

            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    topic.setId(id);
                    return id;
                } else {
                    throw new SQLException("Не вдалося створити тему, ID не отримано.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Topic> findById(Long id) {
        String sql = "SELECT * FROM topics WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToTopic(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при пошуку теми за ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Topic> findAll() {
        String sql = "SELECT * FROM topics ORDER BY id ASC";
        List<Topic> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToTopic(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при отриманні тем", e);
        }
        return list;
    }

    @Override
    public void update(Topic topic) {
        String sql = "UPDATE topics SET title = ?, description = ?, closed = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, topic.getTitle());
            ps.setString(2, topic.getDescription());
            ps.setBoolean(3, topic.isClosed());
            ps.setLong(4, topic.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating topic failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при оновленні теми ID: " + topic.getId(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM topics WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при видаленні теми ID: " + id, e);
        }
    }

    @Override
    public List<Topic> findByTitle(String titleQuery) {
        String sql = "SELECT * FROM topics WHERE title ILIKE ?";
        List<Topic> topics = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + titleQuery + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        topics.add(mapRowToTopic(rs));
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return topics;
    }

    @Override
    public List<Topic> findFiltered(String search, int page, int size) {
        List<Topic> results = new ArrayList<>();
        int offset = (page - 1) * size;

        String sql = "SELECT * FROM topics " +
                "WHERE (? IS NULL OR title ILIKE ? OR description ILIKE ?) " +
                "ORDER BY created_at DESC " +
                "LIMIT ? OFFSET ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = (search == null || search.isEmpty()) ? null : "%" + search + "%";
            ps.setString(1, search);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setInt(4, size);
            ps.setInt(5, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRowToTopic(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при фільтрації тем", e);
        }
        return results;
    }

    private Topic mapRowToTopic(ResultSet rs) throws SQLException {
        Topic topic = new Topic();
        topic.setId(rs.getLong("id"));
        topic.setTitle(rs.getString("title"));
        topic.setDescription(rs.getString("description"));
        topic.setClosed(rs.getBoolean("closed"));
        topic.setCreatedAt(new java.util.Date(rs.getTimestamp("created_at").getTime()));
        return topic;
    }
}