package com.keepgoing.delivery.delivery.infrastructure.persistence.repository;

import com.keepgoing.delivery.delivery.domain.entity.Delivery;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryStatus;
import com.keepgoing.delivery.delivery.domain.repository.DeliveryRepository;
import com.keepgoing.delivery.delivery.infrastructure.persistence.entity.DeliveryEntity;
import com.keepgoing.delivery.delivery.infrastructure.persistence.mapper.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final DeliveryJpaRepository jpaRepository;
    private final DeliveryMapper mapper;

    @Override
    public Delivery save(Delivery delivery) {
        DeliveryEntity jpaEntity = mapper.toJpaEntity(delivery);
        DeliveryEntity saved = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(saved);
    }

    @Override
    public Optional<Delivery> findByIdAndIsDeletedFalse(UUID id) {
        return jpaRepository.findByIdAndDeletedFalse(id)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Optional<Delivery> findByOrderIdAndIsDeletedFalse(UUID orderId) {
        return jpaRepository.findByOrderIdAndDeletedFalse(orderId)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Delivery> findByStatusAndIsDeletedFalse(DeliveryStatus status) {
        return jpaRepository.findByStatusAndDeletedFalse(status).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Delivery> findByHubDeliveryPersonUserId(Long deliveryPersonUserId) {
        // 허브 배송담당자의 배송은 DeliveryRoute를 통해 조회해야 함
        // DeliveryRouteRepository에서 조회 후 deliveryId로 조회하는 방식
        // 또는 JPQL로 JOIN 쿼리 작성
        throw new UnsupportedOperationException("DeliveryRouteRepository를 통해 조회하세요.");
    }

    @Override
    public List<Delivery> findByVendorDeliveryPersonIdAndIsDeletedFalse(Long vendorDeliveryPersonId) {
        return jpaRepository.findByVendorDeliveryPersonIdAndDeletedFalse(vendorDeliveryPersonId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<Delivery> findInProgressDeliveriesByHub(UUID hubId) {
        return jpaRepository.findInProgressDeliveriesByHub(hubId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return jpaRepository.existsByOrderId(orderId);
    }

}