package hr.pacman.mop.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartEventProducer {

    private final KafkaTemplate<String, CartEvent> kafkaTemplate;

    private static final String TOPIC = "cart-events";

    public void sendEvent(CartEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}