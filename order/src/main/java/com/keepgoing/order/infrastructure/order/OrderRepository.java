package com.keepgoing.order.infrastructure.order;

import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {

//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Query(
//    """
//      UPDATE Order o
//          SET o.orderState = :after,
//              o.version = o.version + 1,
//              o.updatedAt = :now
//      WHERE o.id = :orderId
//          AND o.orderState = :before
//          AND o.version = :version
//    """
//    )
//    int updateOrderStateToPaidForPayment(
//        @Param("orderId") UUID orderId,
//        @Param("before") OrderState before,
//        @Param("after") OrderState after,
//        @Param("version") Long version,
//        @Param("now")LocalDateTime now
//    );
}
