package hr.pacman.mop.service;

import hr.pacman.mop.dto.CartResponse;
import hr.pacman.mop.model.CartItem;

public interface CartService {
    CartResponse getCart(String userId);

    CartResponse addItem(String userId, CartItem item);

    CartResponse removeItem(String userId, String productId, Integer quantity);
}
