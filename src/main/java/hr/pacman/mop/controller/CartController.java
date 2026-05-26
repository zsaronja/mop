package hr.pacman.mop.controller;

import hr.pacman.mop.dto.CartResponse;
import hr.pacman.mop.model.CartItem;
import hr.pacman.mop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getCart(@AuthenticationPrincipal Jwt jwt) {

        return addPreferredUsername(cartService.getCart(jwt.getSubject()), jwt);
    }

    @PostMapping("/items")
    public CartResponse addItem(@AuthenticationPrincipal Jwt jwt,
            @RequestBody CartItem item) {

        return addPreferredUsername(cartService.addItem(jwt.getSubject(), item), jwt);
    }

    @DeleteMapping("/items/{productId}")
    public CartResponse removeItem(@AuthenticationPrincipal Jwt jwt,
            @PathVariable String productId,
            @RequestParam int quantity) {

        return addPreferredUsername(cartService.removeItem(jwt.getSubject(), productId, quantity), jwt);

    }

    private CartResponse addPreferredUsername(CartResponse response, Jwt jwt) {
        response.setUsername(jwt.getClaimAsString("preferred_username"));
        return response;
    }

}