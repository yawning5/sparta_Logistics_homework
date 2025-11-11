package com.sparta.vendor.presentation.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponseDTO<T>(
    List<T> content,  //실제 데이터
    long totalElements, // 전체 데이터 개수
    int totalPages, // 전체 페이지 수
    int pageNumber, // 현재 페이지 번호
    int pageSize, // 페이지 크기
    boolean first, // 첫 페이지 여부
    boolean last,  // 마지막 페이지 여부
    boolean empty,  // 콘텐츠 비어있는지 여부
    int numberOfElements // 현재 페이지 데이터 개수
) {

    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return new PageResponseDTO<>(
            page.getContent(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize(),
            page.isFirst(),
            page.isLast(),
            page.isEmpty(),
            page.getNumberOfElements()
        );
    }
}
