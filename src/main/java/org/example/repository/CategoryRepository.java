package org.example.repository;

import org.example.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Category> findAll() {
        String sql = "SELECT * FROM category";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    public Optional<Category> findById(Long id) {
        String sql = "SELECT * FROM category WHERE id = ?";
        List<Category> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void save(Category category) {
        String sql = "INSERT INTO category(name, description) VALUES(?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            category.setId(keyHolder.getKey().longValue());
        }
    }

    public void update(Category category) {
        String sql = "UPDATE category SET name = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(sql, category.getName(), category.getDescription(), category.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM category WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
