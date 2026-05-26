package hr.pacman.mop.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEvent {

    private String userId;
    private String productId;
    private int quantity;
    private String action; // ADD / REMOVE
}