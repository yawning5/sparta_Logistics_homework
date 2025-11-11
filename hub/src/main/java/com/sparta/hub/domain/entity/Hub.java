package com.sparta.hub.domain.entity;

import com.sparta.hub.application.command.UpdateHubCommand;
import com.sparta.hub.application.exception.ErrorCode;
import com.sparta.hub.application.exception.ForbiddenOperationException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_hub")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String hubStatus;  // ACTIVE, INACTIVE 등

    // 삭제 여부 판단
    private boolean isDeleted() {
        return getDeletedBy() != null && getDeletedAt() != null;
    }

    public void checkDeleted() {
        if (isDeleted()) {
            throw new ForbiddenOperationException(ErrorCode.HUB_ALREADY_DELETED);
        }
    }

    // 생성 팩토리 메서드
    private Hub(String name, String address, Double latitude, Double longitude, String hubStatus) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hubStatus = hubStatus;
    }

    public static Hub create(String name, String address, Double latitude, Double longitude, String hubStatus) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("허브명은 필수입니다.");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("주소는 필수입니다.");
        }
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("좌표 정보는 필수입니다.");
        }
        if (hubStatus == null || hubStatus.isBlank()) {
            throw new IllegalArgumentException("허브 상태는 필수입니다.");
        }

        return new Hub(name, address, latitude, longitude, hubStatus);
    }

    // 업데이트 로직 (Command 객체 기반)
    public void updateHub(UpdateHubCommand command) {
        if (command.name() != null) {
            this.name = command.name();
        }
        if (command.address() != null) {
            this.address = command.address();
        }
        if (command.latitude() != null) {
            this.latitude = command.latitude();
        }
        if (command.longitude() != null) {
            this.longitude = command.longitude();
        }
        if (command.hubStatus() != null) {
            this.hubStatus = command.hubStatus();
        }
    }
}