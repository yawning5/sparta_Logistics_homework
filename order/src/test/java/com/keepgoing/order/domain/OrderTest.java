package com.keepgoing.order.domain;

import com.keepgoing.order.domain.order.Order;
import java.time.LocalDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 생성할 때, supplier가 null이라면 IllegalArgumentException 발생")
    @Test
    void createWithIllegalArgumentException() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 11, 5, 12, 0, 0);

        // when // then
        Assertions.assertThatThrownBy(
                () -> Order.create(
                    null,
                    null,
                    UUID.fromString("7027b9af-2c0c-4f04-b04d-6d2e27bd1928"),
                    "김이 필요한 집",
                    UUID.fromString("b8e2e021-2e59-4c8f-9c17-a138e8e61c9c"),
                    "맛있는 김",
                    10,
                    100000,
                    now,
                    LocalDateTime.of(2025, 11, 6, 12, 0, 0),
                    "맛있는 김으로 배송해주세요."
                ))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성할 때, receiver null이라면 IllegalArgumentException 발생")
    @Test
    void createWithIllegalArgumentException2() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 11, 5, 12, 0, 0);

        // when // then
        Assertions.assertThatThrownBy(
                () -> Order.create(
                    UUID.fromString("4e88649d-37ae-4b0f-b2a2-8b79e6912a9c"),
                    "김이 맛있는 집",
                    null,
                    null,
                    UUID.fromString("b8e2e021-2e59-4c8f-9c17-a138e8e61c9c"),
                    "맛있는 김",
                    10,
                    100000,
                    now,
                    LocalDateTime.of(2025, 11, 6, 12, 0, 0),
                    "맛있는 김으로 배송해주세요."
                ))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성할 때, product가 null이라면 IllegalArgumentException 발생")
    @Test
    void createWithIllegalArgumentException3() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 11, 5, 12, 0, 0);

        // when // then
        Assertions.assertThatThrownBy(
                () -> Order.create(
                    UUID.fromString("4e88649d-37ae-4b0f-b2a2-8b79e6912a9c"),
                    "김이 맛있는 집",
                    UUID.fromString("7027b9af-2c0c-4f04-b04d-6d2e27bd1928"),
                    "김이 필요한 집",
                    null,
                    null,
                    10,
                    100000,
                    now,
                    LocalDateTime.of(2025, 11, 6, 12, 0, 0),
                    "맛있는 김으로 배송해주세요."
                ))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성할 때, 주문 수량이 1보다 작은 경우 IllegalArgumentException 발생")
    @Test
    void createWithIllegalArgumentException4() {
        // given

        LocalDateTime now = LocalDateTime.of(2025, 11, 5, 12, 0, 0);

        // when // then
        Assertions.assertThatThrownBy(
                () -> Order.create(
                    UUID.fromString("4e88649d-37ae-4b0f-b2a2-8b79e6912a9c"),
                    "김이 맛있는 집",
                    UUID.fromString("7027b9af-2c0c-4f04-b04d-6d2e27bd1928"),
                    "김이 필요한 집",
                    UUID.fromString("b8e2e021-2e59-4c8f-9c17-a138e8e61c9c"),
                    "맛있는 김",
                    0,
                    100000,
                    now,
                    LocalDateTime.of(2025, 11, 6, 12, 0, 0),
                    "맛있는 김으로 배송해주세요."
                ))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성할 때, 주문 금액이 음수인 경우 IllegalArgumentException 발생")
    @Test
    void createWithIllegalArgumentException5() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 11, 5, 12, 0, 0);

        // when // then
        Assertions.assertThatThrownBy(
                () -> Order.create(
                    UUID.fromString("4e88649d-37ae-4b0f-b2a2-8b79e6912a9c"),
                    "김이 맛있는 집",
                    UUID.fromString("7027b9af-2c0c-4f04-b04d-6d2e27bd1928"),
                    "김이 필요한 집",
                    UUID.fromString("b8e2e021-2e59-4c8f-9c17-a138e8e61c9c"),
                    "맛있는 김",
                    1,
                    -1,
                    now,
                    LocalDateTime.of(2025, 11, 6, 12, 0, 0),
                    "맛있는 김으로 배송해주세요."
                ))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성할 때, 납부 일자가 null인 경우 IllegalArgumentException 발생")
    @Test
    void createWithIllegalArgumentException6() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 11, 5, 12, 0, 0);

        // when // then
        Assertions.assertThatThrownBy(
                () -> Order.create(
                    UUID.fromString("4e88649d-37ae-4b0f-b2a2-8b79e6912a9c"),
                    "김이 맛있는 집",
                    UUID.fromString("7027b9af-2c0c-4f04-b04d-6d2e27bd1928"),
                    "김이 필요한 집",
                    UUID.fromString("b8e2e021-2e59-4c8f-9c17-a138e8e61c9c"),
                    "맛있는 김",
                    1,
                    100000,
                    now,
                    null,
                    null
                    ))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성할 때, 납부 일자가 현재 시간보다 이전인 경우 IllegalArgumentException 발생")
    @Test
    void createWithIllegalArgumentException7() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 11, 5, 12, 0, 0);

        // when // then
        Assertions.assertThatThrownBy(
                () -> Order.create(
                    UUID.fromString("4e88649d-37ae-4b0f-b2a2-8b79e6912a9c"),
                    "김이 맛있는 집",
                    UUID.fromString("7027b9af-2c0c-4f04-b04d-6d2e27bd1928"),
                    "김이 필요한 집",
                    UUID.fromString("b8e2e021-2e59-4c8f-9c17-a138e8e61c9c"),
                    "맛있는 김",
                    0,
                    100000,
                    now,
                    LocalDateTime.of(2025, 11, 2, 12, 0, 0),
                    "맛있는 김으로 배송해주세요."
                ))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성할 때, createAt, orderedAt은 입력으로 들어온 값으로 설정된다.")
    @Test
    void create() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 11, 5, 12, 0, 0);

        // when
        Order order = Order.create(
            UUID.fromString("4e88649d-37ae-4b0f-b2a2-8b79e6912a9c"),
            "김이 맛있는 집",
            UUID.fromString("7027b9af-2c0c-4f04-b04d-6d2e27bd1928"),
            "김이 필요한 집",
            UUID.fromString("b8e2e021-2e59-4c8f-9c17-a138e8e61c9c"),
            "맛있는 김",
            1,
            100000,
            now,
            LocalDateTime.of(2025, 11, 6, 12, 0, 0),
            "맛있는 김으로 배송해주세요."
        );

        // then
        Assertions.assertThat(order.getOrderedAt()).isEqualTo(now);
    }

}