package com.sparta.product.infrastructure.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.product.application.command.SearchProductCommand;
import com.sparta.product.domain.entity.Product;
import com.sparta.product.domain.entity.QProduct;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(SearchProductCommand command, Pageable pageable) {
        QProduct product = QProduct.product;

        List<Product> content = queryFactory
            .selectFrom(product)
            .where(
                productIdEq(command.productId()),
                nameContains(command.name()),
                descriptionContains(command.description()),
                priceBetween(command.minPrice(), command.maxPrice()),
                vendorIdEq(command.vendorId()),
                hubIdEq(command.hubId())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifiers(pageable))
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(product.count())
            .from(product)
            .where(
                productIdEq(command.productId()),
                nameContains(command.name()),
                descriptionContains(command.description()),
                priceBetween(command.minPrice(), command.maxPrice()),
                vendorIdEq(command.vendorId()),
                hubIdEq(command.hubId())
            );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression productIdEq(UUID productId) {
        return productId != null ? QProduct.product.id.eq(productId) : null;
    }

    private BooleanExpression nameContains(String name) {
        return name != null ? QProduct.product.productName.containsIgnoreCase(name) : null;
    }

    private BooleanExpression descriptionContains(String description) {
        return description != null ? QProduct.product.productDescription.containsIgnoreCase(description) : null;
    }

    private BooleanExpression priceBetween(BigInteger minPrice, BigInteger maxPrice) {
        if (minPrice == null && maxPrice == null) return null;
        if (minPrice != null && maxPrice != null)
            return QProduct.product.productPrice.between(minPrice, maxPrice);
        if (minPrice != null)
            return QProduct.product.productPrice.goe(minPrice);
        return QProduct.product.productPrice.loe(maxPrice);
    }

    private BooleanExpression vendorIdEq(UUID vendorId) {
        return vendorId != null ? QProduct.product.vendorId.id.eq(vendorId) : null;
    }

    private BooleanExpression hubIdEq(UUID hubId) {
        return hubId != null ? QProduct.product.hubId.id.eq(hubId) : null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        QProduct product = QProduct.product;

        if (pageable.getSort().isEmpty()) {
            // 기본 정렬: createdAt DESC, updatedAt DESC
            return new OrderSpecifier[]{
                product.createdAt.desc(),
                product.updatedAt.desc()
            };
        }

        return pageable.getSort().stream()
            .map(order -> {
                Order qOrder = order.isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createdAt":
                        return new OrderSpecifier<>(qOrder, product.createdAt);
                    case "updatedAt":
                        return new OrderSpecifier<>(qOrder, product.updatedAt);
                    case "name":
                        return new OrderSpecifier<>(qOrder, product.productName);
                    default:
                        return new OrderSpecifier<>(qOrder, product.createdAt);
                }
            })
            .toArray(OrderSpecifier[]::new);
    }
}
