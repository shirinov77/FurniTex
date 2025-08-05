package org.example.repository;

import org.example.model.Cart;
import org.example.model.Product;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CartRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Cart> cartRowMapper = new RowMapper<Cart>() {
        @Override
        public Cart mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cart cart = new Cart();

            cart.setId(rs.getLong("id"));

            User user = new User();
            user.setId(rs.getLong("user_id"));
            cart.setUser(user);

            Product product = new Product();
            product.setId(rs.getLong("product_id"));
            cart.setProduct(product);

            cart.setQuantity(rs.getInt("quantity"));

            return cart;
        }
    };

    public List<Cart> findAll() {
        String sql = "SELECT * FROM carts";
        return jdbcTemplate.query(sql, cartRowMapper);
    }

    public Optional<Cart> findById(Long id) {
        String sql = "SELECT * FROM carts WHERE id = ?";
        List<Cart> result = jdbcTemplate.query(sql, cartRowMapper, id);
        return result.stream().findFirst();
    }

    public Optional<Cart> findByUserId(Long userId) {
        String sql = "SELECT * FROM carts WHERE user_id = ?";
        List<Cart> result = jdbcTemplate.query(sql, cartRowMapper, userId);
        return result.stream().findFirst();
    }

    public List<Cart> findAllByUser(User user) {
        String sql = "SELECT * FROM carts WHERE user_id = ?";
        return jdbcTemplate.query(sql, cartRowMapper, user.getId());
    }

    public Optional<Cart> findByUserAndProduct(User user, Product product) {
        String sql = "SELECT * FROM carts WHERE user_id = ? AND product_id = ?";
        List<Cart> result = jdbcTemplate.query(sql, cartRowMapper, user.getId(), product.getId());
        return result.stream().findFirst();
    }

    public Cart save(Cart cart) {
        if (cart.getId() != null && findById(cart.getId()).isPresent()) {
            update(cart);
        } else {
            String sql = "INSERT INTO carts (user_id, product_id, quantity) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql,
                    cart.getUser().getId(),
                    cart.getProduct().getId(),
                    cart.getQuantity());
            // IDni qaytarish uchun generated key olish kerak bo‘lardi, ammo bu soddalashtirilgan
        }
        return cart;
    }

    public int update(Cart cart) {
        String sql = "UPDATE carts SET quantity = ? WHERE id = ?";
        return jdbcTemplate.update(sql, cart.getQuantity(), cart.getId());
    }

    public int delete(Cart cart) {
        String sql = "DELETE FROM carts WHERE id = ?";
        return jdbcTemplate.update(sql, cart.getId());
    }

    public List<Cart> findAllByUserIdWithPrice(Long userId) {
        String sql = "SELECT c.id, c.product_id, c.user_id, c.quantity, p.price as product_price " +
                "FROM carts c JOIN products p ON c.product_id = p.id " +
                "WHERE c.user_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Cart cart = new Cart();
            cart.setId(rs.getLong("id"));
            cart.setProductId(rs.getLong("product_id"));
            cart.setUserId(rs.getLong("user_id"));
            cart.setQuantity(rs.getInt("quantity"));
            cart.setProductPrice(rs.getDouble("product_price"));
            return cart;
        }, userId);
    }

    public void deleteAll(List<Cart> cartItems) {

    }
}
