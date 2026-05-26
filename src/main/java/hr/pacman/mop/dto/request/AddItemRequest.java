
package hr.pacman.mop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemRequest {

    @NotBlank(message = "Product ID must not be empty")
    private String productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
