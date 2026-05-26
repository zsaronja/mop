package hr.pacman.mop.service;

import hr.pacman.mop.dto.CartResponse;
import hr.pacman.mop.model.Cart;
import hr.pacman.mop.model.CartItem;
import hr.pacman.mop.repository.CartRepository;
import hr.pacman.mop.mapper.CartMapper;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final RedisTemplate<String, CartResponse> redisTemplate;
    private final CartRepository cartRepository;

    private static final String CART_KEY_PREFIX = "cart:";
    private static final long TTL_MINUTES = 30;

    @CircuitBreaker(name = "redisService", fallbackMethod = "getCartFromDb")
    @Override
    public CartResponse getCart(String userId) {
        String key = CART_KEY_PREFIX + userId;
        CartResponse cartResponse = null;
        // 1. pokušaj Redis
        try {
            cartResponse = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.warn("Redis is down, fallback to DB ");
            logger.warn("Error accessing Redis for userId {}: {}", userId, e.getMessage());
        }

        if (cartResponse == null) {
            // 2. fallback DB
            try {

                Cart cart = cartRepository.findById(userId).orElse(new Cart(userId));
                cartResponse = CartMapper.toDto(cart);
            } catch (Exception e) {
                logger.error("DB failed as well");
                logger.error("Error accessing cart for userId {}: {}", userId, e.getMessage());
                e.printStackTrace();
            }
            redisTemplate.opsForValue().set(key, cartResponse, TTL_MINUTES, TimeUnit.MINUTES);
        }
        return cartResponse;
    }

    public CartResponse getCartFromDb(String userId, Exception ex) {
        logger.error("Redis failed, fallback to DB");

        Cart cart = cartRepository.findById(userId)
                .orElse(new Cart(userId));

        CartResponse response = CartMapper.toDto(cart);

        return response;

    }

    @Override
    @CircuitBreaker(name = "redisService", fallbackMethod = "addItemFallback")
    public CartResponse addItem(String userId, CartItem item) {

        Cart cart = cartRepository.findById(userId).orElseGet(() -> new Cart(userId));

        cart.addItem(item);

        // save to DB
        try {
            cartRepository.save(cart);
        } catch (Exception e) {
            logger.error("DB failed");
            logger.error("Error adding item {} to cart for userId {}: {}", item.getProductId(), userId, e.getMessage());
            e.printStackTrace();
        }

        CartResponse response = CartMapper.toDto(cart);

        // save to Redis
        try {
            redisTemplate.opsForValue().set(CART_KEY_PREFIX + userId, response, TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.error("Redis unavailable, skipping cache");
            e.printStackTrace();
        }

        return response;
    }

    public CartResponse addItemFallback(String userId, CartItem item, Exception ex) {
        Cart cart = cartRepository.findById(userId).orElse(new Cart(userId));

        cart.addItem(item);
        cartRepository.save(cart);

        return CartMapper.toDto(cart);
    }

    @Override
    public CartResponse removeItem(String userId, String productId, Integer quantity) {

        Cart cart = cartRepository.findById(userId).orElse(new Cart(userId));

        cart.removeItem(productId, quantity);

        // save to DB
        try {
            cartRepository.save(cart);
        } catch (Exception e) {
            logger.error("DB failed");
            logger.error("Error removing item {} quantity {} from cart for userId {}: {}", productId, quantity, userId,
                    e.getMessage());
            e.printStackTrace();
        }

        CartResponse response = CartMapper.toDto(cart);

        // save to Redis
        try {
            redisTemplate.opsForValue().set(CART_KEY_PREFIX + userId, response, TTL_MINUTES, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.warn("Redis unavailable, skipping cache");
            e.printStackTrace();
        }

        return response;
    }

}