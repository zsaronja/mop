package hr.pacman.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    //private String userId;
    private String username;
    private List<CartItemResponse> items;
}