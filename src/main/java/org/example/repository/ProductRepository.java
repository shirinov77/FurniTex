package org.example.repository;

import org.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class));
    }

    public Optional<Product> findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        List<Product> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), id);
        return result.stream().findFirst();
    }

    public int save(Product product) {
        String sql = "INSERT INTO products (name, description, price, image_url, category_id) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategoryId());
    }

    public int update(Long id, Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, image_url = ?, category_id = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategoryId(),
                id);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Product> findAllByCategoryId(Long categoryId) {
        String sql = "SELECT * FROM products WHERE category_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), categoryId);
    }
}
