package hr.pacman.mop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private String productId;
    private int quantity;
    //private double price;
    //private String name;
}
