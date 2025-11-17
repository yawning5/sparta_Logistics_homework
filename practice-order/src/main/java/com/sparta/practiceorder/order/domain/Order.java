package com.sparta.practiceorder.order.domain;

import com.sparta.practiceorder.order.domain.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    UUID productId;

    UUID supplierId;

    UUID receiverId;

    Integer quantity;

    OrderStatus orderStatus;

    public static Order create(UUID productId, UUID supplierId, UUID receiverId, int quantity) {
        return new Order(
            null,
            productId,
            supplierId,
            receiverId,
            quantity,
            OrderStatus.PENDING
        );
    }
}
