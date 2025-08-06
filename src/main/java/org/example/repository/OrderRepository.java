package org.example.repository;

import org.example.model.Order;
import org.example.model.OrderStatus;
import org.example.model.Product;
import org.example.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Order order) {
        String sql = "INSERT INTO orders (user_id, product_id, quantity, status, created_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                order.getBuyer().getId(),
                order.getProduct().getId(),
                order.getQuantity(),
                order.getStatus().name(),
                Timestamp.valueOf(order.getCreatedAt() != null ? order.getCreatedAt() : LocalDateTime.now()));
    }

    public void saveAll(List<Order> orders) {
        for (Order order : orders) {
            save(order);
        }
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, orderRowMapper());
    }

    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        List<Order> orders = jdbcTemplate.query(sql, orderRowMapper(), id);
        return orders.stream().findFirst();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Order> findByBuyerId(Long userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper(), userId);
    }

    public List<Order> findByStatus(OrderStatus status) {
        String sql = "SELECT * FROM orders WHERE status = ?";
        return jdbcTemplate.query(sql, orderRowMapper(), status.name());
    }

    public List<Order> findByBuyerIdAndStatus(Long userId, OrderStatus status) {
        String sql = "SELECT * FROM orders WHERE user_id = ? AND status = ?";
        return jdbcTemplate.query(sql, orderRowMapper(), userId, status.name());
    }

    private RowMapper<Order> orderRowMapper() {
        return (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setQuantity(rs.getInt("quantity"));
            order.setStatus(OrderStatus.valueOf(rs.getString("status")));
            order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            User user = new User();
            user.setId(rs.getLong("user_id"));
            order.setBuyer(user);

            Product product = new Product();
            product.setId(rs.getLong("product_id"));
            order.setProduct(product);

            return order;
        };
    }
}
