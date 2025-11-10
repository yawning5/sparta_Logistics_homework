package com.keepgoing.delivery.delivery.infrastructure.persistence.repository;

import com.keepgoing.delivery.delivery.domain.entity.DeliveryRoute;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryRouteStatus;
import com.keepgoing.delivery.delivery.domain.repository.DeliveryRouteRepository;
import com.keepgoing.delivery.delivery.infrastructure.persistence.entity.DeliveryRouteEntity;
import com.keepgoing.delivery.delivery.infrastructure.persistence.mapper.DeliveryRouteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepository {

    private final DeliveryRouteJpaRepository jpaRepository;
    private final DeliveryRouteMapper mapper;

    @Override
    public void save(DeliveryRoute route) {
        DeliveryRouteEntity jpaEntity = mapper.toJpaEntity(route);
        DeliveryRouteEntity saved = jpaRepository.save(jpaEntity);
        mapper.toDomainEntity(saved);
    }

    @Override
    public void saveAll(List<DeliveryRoute> routes) {
        List<DeliveryRouteEntity> jpaEntities = routes.stream()
                .map(mapper::toJpaEntity)
                .toList();

        List<DeliveryRouteEntity> saved = jpaRepository.saveAll(jpaEntities);

        saved.stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public Optional<DeliveryRoute> findByIdAndIsDeletedFalse(UUID id) {
        return jpaRepository.findByIdAndDeletedFalse(id)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<DeliveryRoute> findByDeliveryId(UUID deliveryId) {
        return jpaRepository.findByDeliveryId(deliveryId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<DeliveryRoute> findByDeliveryIdAndIsDeletedFalse(UUID deliveryId) {
        return jpaRepository.findByDeliveryIdAndDeletedFalse(deliveryId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<DeliveryRoute> findByDeliveryIdOrderByRouteSeq(UUID deliveryId) {
        return jpaRepository.findByDeliveryIdOrderByRouteSeq(deliveryId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<DeliveryRoute> findByDeliveryPersonIdAndIsDeletedFalse(Long deliveryPersonId) {
        return jpaRepository.findByDeliveryPersonIdAndDeletedFalse(deliveryPersonId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<DeliveryRoute> findByDeliveryPersonIdAndStatus(Long deliveryPersonId, DeliveryRouteStatus status) {
        return jpaRepository.findByDeliveryPersonIdAndStatus(deliveryPersonId, status).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<DeliveryRoute> findByDepartureHubIdAndIsDeletedFalse(UUID departureHubId) {
        return jpaRepository.findByDepartureHubIdAndDeletedFalse(departureHubId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<DeliveryRoute> findByArrivalHubIdAndIsDeletedFalse(UUID arrivalHubId) {
        return jpaRepository.findByArrivalHubIdAndDeletedFalse(arrivalHubId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

}