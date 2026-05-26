package hr.pacman.mop.mapper;

import hr.pacman.mop.dto.CartItemResponse;
import hr.pacman.mop.dto.CartResponse;
import hr.pacman.mop.model.Cart;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public static CartResponse toDto(Cart cart) {

        List<CartItemResponse> items = cart.getItems().values().stream()
                .map(item -> new CartItemResponse(
                        item.getProductId(),
                        item.getQuantity()))
                .toList();

        return new CartResponse(
            // cart.getUserId(), 
            null, items);
    }
}