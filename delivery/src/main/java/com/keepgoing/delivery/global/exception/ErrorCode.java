package com.keepgoing.delivery.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Delivery 관련
    DELIVERY_ALREADY_EXISTS(HttpStatus.CONFLICT, "7001", "이미 해당 주문에 대한 배송이 존재합니다."),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "7002", "배송 정보를 찾을 수 없습니다."),
    DELIVERY_BY_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "7003", "해당 주문의 배송 정보를 찾을 수 없습니다."),
    HUB_DELIVERY_PERSON_NOT_AVAILABLE(HttpStatus.NO_CONTENT, "7004", "배정 가능한 허브 배송 담당자가 없습니다."),
    DELIVERY_PERSON_ASSIGN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "7005", "배송 담당자 배정 실패"),

    DELIVERY_ALREADY_DELETED(HttpStatus.CONFLICT, "7006", "이미 삭제된 배송입니다."),
    DELIVERY_DELETE_ONLY_BEFORE_START(HttpStatus.BAD_REQUEST, "7007", "배송 시작 전에만 삭제할 수 있습니다."),
    DELIVERY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "7008", "해당 순서의 경로를 찾을 수 없습니다."),
    DELIVERY_INVALID_ROUTE(HttpStatus.BAD_REQUEST, "7009", "경로 정보는 null일 수 없습니다."),
    DELIVERY_REQUIRED_FIELDS_MISSING(HttpStatus.BAD_REQUEST, "7010", "배송 생성 시 필수 필드가 누락되었습니다."),
    DELIVERY_INVALID_ID(HttpStatus.BAD_REQUEST, "7011", "배송 ID는 필수입니다."),
    DELIVERY_INVALID_HUB(HttpStatus.BAD_REQUEST, "7012", "출발/도착 허브 ID는 필수입니다."),
    DELIVERY_INVALID_DISTANCE_OR_TIME(HttpStatus.BAD_REQUEST, "7013", "거리와 소요 시간은 필수입니다."),
    DELIVERY_INVALID_ROUTE_SEQ(HttpStatus.BAD_REQUEST, "7014", "경로 순서는 필수입니다."),
    DELIVERY_INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "7015", "현재 상태에서는 해당 동작을 수행할 수 없습니다."),
    DELIVERY_CAN_START_ONLY_FROM_HUB_WAITING(HttpStatus.BAD_REQUEST, "7016", "HUB_WAITING 상태에서만 출발할 수 있습니다."),
    DELIVERY_CAN_ARRIVE_DEST_ONLY_FROM_HUB_IN_PROGRESS(HttpStatus.BAD_REQUEST, "7017", "HUB_IN_PROGRESS 상태에서만 도착 허브에 도착할 수 있습니다."),
    DELIVERY_CAN_START_VENDOR_ONLY_FROM_DEST_HUB_ARRIVED(HttpStatus.BAD_REQUEST, "7018", "DEST_HUB_ARRIVED 상태에서만 가맹점 배송을 시작할 수 있습니다."),
    DELIVERY_CAN_COMPLETE_ONLY_FROM_VENDOR_IN_PROGRESS(HttpStatus.BAD_REQUEST, "7019", "VENDOR_IN_PROGRESS 상태에서만 배송을 완료할 수 있습니다."),
    DELIVERY_RECORD_ONLY_AFTER_ARRIVED(HttpStatus.BAD_REQUEST, "7020", "ARRIVED 상태에서만 실제 거리/시간을 기록할 수 있습니다."),
    DELIVERY_REQUIRED_ACTUAL_DISTANCE_OR_TIME(HttpStatus.BAD_REQUEST, "7021", "실제 거리와 시간은 필수입니다."),
    DELIVERY_ROUTE_CAN_MOVE_ONLY_FROM_WAITING(HttpStatus.BAD_REQUEST, "7022", "WAITING 상태에서만 이동할 수 있습니다."),
    DELIVERY_ROUTE_CAN_ARRIVE_ONLY_FROM_MOVING(HttpStatus.BAD_REQUEST, "7023", "MOVING 상태에서만 도착 처리할 수 있습니다."),

    // DeliveryPerson 관련
    DELIVERY_PERSON_ALREADY_REGISTERED(HttpStatus.CONFLICT, "7101", "이미 등록된 배송 담당자입니다."),
    DELIVERY_PERSON_NOT_FOUND(HttpStatus.NOT_FOUND, "7102", "배송 담당자를 찾을 수 없습니다."),
    HUB_DELIVERY_PERSON_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "7103", "해당 허브의 배송 담당자 최대인원(10명)을 초과할 수 없습니다."),
    DELIVERY_PERSON_ALREADY_DELETED(HttpStatus.CONFLICT, "7104", "이미 삭제된 배송 담당자입니다."),
    DELIVERY_PERSON_INVALID_TYPE(HttpStatus.BAD_REQUEST, "7105", "배송담당자 타입이 올바르지 않습니다."),
    DELIVERY_PERSON_REQUIRED_FIELDS_MISSING(HttpStatus.BAD_REQUEST, "7106", "배송 담당자 생성 시 필수 필드가 누락되었습니다."),
    DELIVERY_PERSON_REQUIRED_SLACK_ID(HttpStatus.BAD_REQUEST, "7107", "slackId는 필수입니다."),
    DELIVERY_PERSON_REQUIRED_USER_ID(HttpStatus.BAD_REQUEST, "7108", "userId는 필수입니다."),
    DELIVERY_PERSON_REQUIRED_TYPE(HttpStatus.BAD_REQUEST, "7109", "type은 필수입니다."),
    DELIVERY_PERSON_VENDOR_HUB_REQUIRED(HttpStatus.BAD_REQUEST, "7110", "업체 배송 담당자의 hubId는 필수입니다."),
    DELIVERY_PERSON_INVALID_HUB_ID(HttpStatus.BAD_REQUEST, "7111", "허브 ID는 필수입니다."),
    DELIVERY_PERSON_INVALID_TYPE_CHANGE(HttpStatus.BAD_REQUEST, "7112", "동일한 타입으로는 변경할 수 없습니다."),
    DELIVERY_PERSON_REQUIRED_NEW_TYPE(HttpStatus.BAD_REQUEST, "7113", "변경할 타입은 필수입니다."),
    DELIVERY_PERSON_CANNOT_CHANGE_HUB(HttpStatus.BAD_REQUEST, "7114", "업체 배송 담당자만 허브를 변경할 수 있습니다."),
    DELIVERY_PERSON_HUB_REQUIRED_FOR_VENDOR_CONVERT(HttpStatus.BAD_REQUEST, "7115", "업체 배송 담당자로 전환 시 소속 허브는 필수입니다."),
    DELIVERY_PERSON_ASSIGN_ONLY_AFTER_DEST_ARRIVED(HttpStatus.BAD_REQUEST, "7116", "목적지 허브 도착 후에만 업체 배송담당자를 배정할 수 있습니다."),
    DELIVERY_PERSON_ALREADY_ASSIGNED(HttpStatus.CONFLICT, "7117", "이미 배달원이 지정되어 있습니다."),
    DELIVERY_PERSON_REQUIRED_DELIVERY_ID(HttpStatus.BAD_REQUEST, "7118", "배달원 ID는 필수입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
