package com.sparta.vendor.infrastructure.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.vendor.application.command.SearchVendorCommand;
import com.sparta.vendor.domain.entity.QVendor;
import com.sparta.vendor.domain.entity.Vendor;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VendorRepositoryCustomImpl implements VendorRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Vendor> searchVendors(SearchVendorCommand command, Pageable pageable) {
        QVendor vendor = QVendor.vendor;

        // 데이터 쿼리
        List<Vendor> content = queryFactory
            .selectFrom(vendor)
            .where(
                vendorIdEq(command.vendorId()),
                vendorNameContains(command.vendorName()),
                vendorTypeEq(command.vendorType()),
                addressContains(command.address()),
                vendorZipCodeEq(command.zipCode()),
                hubIdEq(command.hubId())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifiers(pageable))
            .fetch();

        // 전체 개수 쿼리
        Long total = queryFactory
            .select(vendor.count())
            .from(vendor)
            .where(
                vendorIdEq(command.vendorId()),
                vendorNameContains(command.vendorName()),
                vendorTypeEq(command.vendorType()),
                addressContains(command.address()),
                vendorZipCodeEq(command.zipCode()),
                hubIdEq(command.hubId())
            )
            .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression vendorIdEq(UUID vendorId) {
        return vendorId != null ? QVendor.vendor.id.eq(vendorId) : null;
    }

    private BooleanExpression vendorNameContains(String vendorName) {
        return vendorName != null ? QVendor.vendor.vendorName.containsIgnoreCase(vendorName) : null;
    }

    private BooleanExpression vendorTypeEq(Enum<?> vendorType) {
        return vendorType != null ? QVendor.vendor.vendorType.eq(
            (com.sparta.vendor.domain.vo.VendorType) vendorType) : null;
    }

    private BooleanExpression addressContains(String address) {
        if (address == null) {
            return null;
        }
        return QVendor.vendor.address.city.containsIgnoreCase(address)
            .or(QVendor.vendor.address.street.containsIgnoreCase(address));
    }

    private BooleanExpression vendorZipCodeEq(String zipCode) {
        return zipCode != null ? QVendor.vendor.address.zipCode.containsIgnoreCase(zipCode) : null;
    }


    private BooleanExpression hubIdEq(UUID hubId) {
        return hubId != null ? QVendor.vendor.hubId.id.eq(hubId) : null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        QVendor vendor = QVendor.vendor;

        if (pageable.getSort().isEmpty()) {
            // 기본 정렬: createdAt DESC, updatedAt DESC
            return new OrderSpecifier[]{
                vendor.createdAt.desc(),
                vendor.updatedAt.desc()
            };
        }

        return pageable.getSort().stream()
            .map(order -> {
                Order qOrder = order.isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createdAt":
                        return new OrderSpecifier<>(qOrder, vendor.createdAt);
                    case "updatedAt":
                        return new OrderSpecifier<>(qOrder, vendor.updatedAt);
                    case "vendorName":
                        return new OrderSpecifier<>(qOrder, vendor.vendorName);
                    default:
                        // 알 수 없는 필드이면 기본 정렬 추가
                        return new OrderSpecifier<>(qOrder, vendor.createdAt);
                }
            })
            .toArray(OrderSpecifier[]::new);
    }
}
