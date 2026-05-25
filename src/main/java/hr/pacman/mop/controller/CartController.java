package hr.pacman.mop.controller;

import hr.pacman.mop.model.Cart;
import hr.pacman.mop.model.CartItem;
import hr.pacman.mop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/{userId}/items")
    public Cart addItem(@PathVariable String userId,
            @RequestBody CartItem item) {
        return cartService.addItem(userId, item);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public Cart removeItem(@PathVariable String userId,
            @PathVariable String productId,
            @RequestParam int quantity) {
        return cartService.removeItem(userId, productId, quantity);
    }
}