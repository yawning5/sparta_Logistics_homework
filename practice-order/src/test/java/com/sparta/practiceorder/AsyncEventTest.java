package com.sparta.practiceorder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.practiceorder.order.dto.OrderCreateRequest;
import com.sparta.practiceorder.order.event.OrderCreatedEvent;
import com.sparta.practiceorder.payment.event.PaymentEventListener;
import com.sparta.practiceorder.order.service.OrderService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@Slf4j
@SpringBootTest
public class AsyncEventTest {

    @Autowired
    private OrderService orderService;

    @MockitoSpyBean
    private PaymentEventListener paymentEventListener;

    @Test
    @DisplayName("이벤트가 비동기로 처리된다")
    void events_ShouldBeProcessedAsynchronously() throws InterruptedException {
        // given
        OrderCreateRequest request = createValidRequest();
        String mainThread = Thread.currentThread().getName();
        log.info("메인 스레드: {}", mainThread);

        // when
        orderService.createOrder(request);

        // 비동기 처리를 위한 대기
        // 실무에서는 Awaitility 라이브러리를 사용하는 것이 더 안정적입니다.
        Thread.sleep(1000);

        // then
        verify(paymentEventListener, times(1))
            .handleOrderCreatedAsync(any(OrderCreatedEvent.class));
        // 로그를 확인하면 "event-async-" 접두사가 붙은 스레드 이름을 볼 수 있습니다.
        // 이를 통해 비동기로 실행되었음을 확인할 수 있습니다.
    }

    private OrderCreateRequest createValidRequest() {
        return OrderCreateRequest.builder()
            .productId(UUID.randomUUID())
            .build();
    }

    @Test
    @DisplayName("비동기 처리 중 예외가 발생해도 메인 로직에 영향을 주지 않는다")
    void whenAsyncEventThrowsException_MainFlowShouldNotBeAffected() {
        // given
        OrderCreateRequest request = createValidRequest();

        // Mock을 설정하여 결제 처리 중 예외가 발생하도록 합니다.
        doThrow(new RuntimeException("결제 실패"))
            .when(paymentEventListener)
            .handleOrderCreatedAsync(any(OrderCreatedEvent.class));

        // when & then
        // 비동기 작업에서 예외가 발생해도, 메인 로직(주문 생성)은 성공합니다.
        // 이는 사용자 경험 측면에서 중요합니다.
        assertThatCode(() -> orderService.createOrder(request))
            .doesNotThrowAnyException();

        // 다만 실무에서는 비동기 작업의 실패를 감지하고 대응하는 로직이 필요합니다.
        // 예: 재시도, 관리자 알림, 수동 처리를 위한 큐 저장 등
    }
}
