package hr.pacman.mop.service;
import hr.pacman.mop.model.Cart;
import hr.pacman.mop.model.CartItem;

public interface CartService {
    Cart getCart(String userId);
    Cart addItem(String userId, CartItem item);
    Cart removeItem(String userId, String productId, int quantity);
}
