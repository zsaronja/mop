package hr.pacman.mop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Cart {

    @Id
    private String userId;

    @ElementCollection
    private List<CartItem> items = new ArrayList<>();

    public Cart(String userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public void addItem(CartItem newItem) {

        for (CartItem item : items) {
            if (Objects.equals(item.getProductId(), newItem.getProductId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }

    public void removeItem(String productId, int quantityToRemove) {
        if (quantityToRemove <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        for (CartItem item : items) {
            if (Objects.equals(item.getProductId(), productId)) {

                int quantityAvailable = item.getQuantity();
                if (quantityToRemove > quantityAvailable) {
                    throw new IllegalArgumentException("Not enough quantity to remove");
                }

                int remaining = quantityAvailable - quantityToRemove;

                if (remaining > 0) {
                    item.setQuantity(remaining);
                } else {
                    items.remove(item);
                }
                return;
            }
        }
    }

}
