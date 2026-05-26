package hr.pacman.mop.model;

import java.util.Map;
import java.util.HashMap;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor

public class Cart {

    @Id
    private String userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "productId")
    private Map<String, CartItem> items = new HashMap<>();

    public Cart(String userId) {
        this.userId = userId;
    }

    public void addItem(CartItem newItem) {
        CartItem existing = items.get(newItem.getProductId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
        } else {
            newItem.setCart(this);
            items.put(newItem.getProductId(), newItem);
        }

    }

    public void removeItem(String productId, int quantityToRemove) {
        if (quantityToRemove <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        CartItem item = items.get(productId);
        if (item != null) {
            int quantityAvailable = item.getQuantity();
            if (quantityToRemove > quantityAvailable) {
                throw new IllegalArgumentException("Not enough quantity to remove");
            }
            int remaining = quantityAvailable - quantityToRemove;
            if (remaining > 0) {
                item.setQuantity(remaining);
            } else {
                items.remove(productId);
            }
        }
    }
}