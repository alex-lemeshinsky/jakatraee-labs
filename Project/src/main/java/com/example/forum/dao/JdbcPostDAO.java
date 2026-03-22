package com.example.forum.dao;

import com.example.forum.model.Post;
import com.example.forum.model.User;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless
public class JdbcPostDAO implements PostDAO {

    @Resource(lookup = "jdbc/forumDS")
    private DataSource dataSource;

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO posts (content, author_username, topic_id, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, post.getContent());
            ps.setString(2, post.getAuthor().getUsername());
            ps.setLong(3, post.getTopicId());
            ps.setTimestamp(4, new Timestamp(post.getCreatedAt().getTime()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    post.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при збереженні повідомлення", e);
        }
    }

    @Override
    public List<Post> findAllByTopicId(Long topicId) {
        String sql = "SELECT * FROM posts WHERE topic_id = ? ORDER BY created_at";
        List<Post> posts = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, topicId); // Захист від ін'єкцій
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapRowToPost(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET content = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, post.getContent());
            // Встановлюємо поточний час як дату оновлення
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setLong(3, post.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> findFiltered(Long topicId, int page, int size) {
        List<Post> results = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM posts WHERE topic_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, topicId);
            ps.setInt(2, size);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRowToPost(rs));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return results;
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToPost(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Post mapRowToPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(rs.getLong("id"));
        post.setContent(rs.getString("content"));
        post.setTopicId(rs.getLong("topic_id"));
        post.setCreatedAt(new java.util.Date(rs.getTimestamp("created_at").getTime()));

        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null) {
            post.setUpdatedAt(new java.util.Date(updatedTs.getTime()));
        }

        String authorName = rs.getString("author_username");
        post.setAuthor(new User(authorName, "user"));

        return post;
    }
}