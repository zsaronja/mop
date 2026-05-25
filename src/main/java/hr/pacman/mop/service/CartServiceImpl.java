package hr.pacman.mop.service;

import hr.pacman.mop.model.Cart;
import hr.pacman.mop.model.CartItem;
import hr.pacman.mop.repository.CartRepository;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final RedisTemplate<String, Cart> redisTemplate;
    private final CartRepository cartRepository;

    private static final String CART_KEY_PREFIX = "cart:";
    private static final long TTL_MINUTES = 30;

    @Override
    public Cart getCart(String userId) {
        String key = CART_KEY_PREFIX + userId;
        Cart cart = null;
        // 1. pokušaj Redis
        try {
            cart = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.warn("Redis is down, fallback to DB ");
            logger.warn("Error accessing Redis for userId {}: {}", userId, e.getMessage());
        }

        if (cart == null) {
            // 2. fallback DB
            try {
                cart = cartRepository.findById(userId == null ? "" : userId).orElse(new Cart(userId));
            } catch (Exception e) {
                logger.error("DB failed as well");
                logger.error("Error accessing cart for userId {}: {}", userId, e.getMessage());
                e.printStackTrace();
            }

        }

        return cart;
    }

    @Override
    public Cart addItem(String userId, CartItem item) {

        Cart cart = getCart(userId);

        cart.addItem(item);

        // save to Redis
        try {
            redisTemplate.opsForValue().set(CART_KEY_PREFIX + userId, cart, TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error("Redis unavailable, skipping cache");
            e.printStackTrace();
        }

        // save to DB
        try {
            cartRepository.save(cart);
        } catch (Exception e) {
            logger.error("DB failed");
            logger.error("Error adding item {} to cart for userId {}: {}", item.getProductId(), userId, e.getMessage());
            e.printStackTrace();
        }

        return cart;
    }

    @Override
    public Cart removeItem(String userId, String productId, int quantity) {

        Cart cart = getCart(userId);

        cart.removeItem(productId, quantity);

        // save to Redis
        try {
            redisTemplate.opsForValue().set(CART_KEY_PREFIX + userId, cart, TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.warn("Redis unavailable, skipping cache");
            e.printStackTrace();
        }

        // save to DB
        try {
            cartRepository.save(cart);
        } catch (Exception e) {
            logger.error("DB failed");
            logger.error("Error removing item {} quantity {} from cart for userId {}: {}", productId, quantity, userId,
                    e.getMessage());
            e.printStackTrace();
        }

        return cart;
    }
}