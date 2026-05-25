package hr.pacman.mop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hr.pacman.mop.model.Cart;

public interface CartRepository extends JpaRepository<Cart, String> {
}
