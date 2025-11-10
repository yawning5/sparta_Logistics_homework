package com.keepgoing.delivery.deliveryperson.infrastructure.persistence.repository;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
import com.keepgoing.delivery.deliveryperson.domain.repository.DeliveryPersonRepository;
import com.keepgoing.delivery.deliveryperson.infrastructure.persistence.entity.DeliveryPersonEntity;
import com.keepgoing.delivery.deliveryperson.infrastructure.persistence.mapper.DeliveryPersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DeliveryPersonRepositoryImpl implements DeliveryPersonRepository {

    private final DeliveryPersonJpaRepository deliveryPersonJpaRepository;
    private final DeliveryPersonMapper mapper;

    @Override
    public DeliveryPerson save(DeliveryPerson deliveryPerson) {
        DeliveryPersonEntity deliveryPersonEntity = mapper.toJpaEntity(deliveryPerson);

        DeliveryPersonEntity saved = deliveryPersonJpaRepository.save(deliveryPersonEntity);

        return mapper.toDomainEntity(saved);
    }

    @Override
    public Optional<DeliveryPerson> findByIdAndIsDeletedFalse(Long id) {
        return deliveryPersonJpaRepository.findByIdAndDeletedFalse(id)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<DeliveryPerson> findByTypeAndIsDeletedFalse(DeliveryPersonType type) {
        return deliveryPersonJpaRepository.findByTypeAndDeletedFalse(type).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryPerson> findByTypeAndHubIdAndIsDeletedFalse(DeliveryPersonType type, UUID hubId) {
        return deliveryPersonJpaRepository.findByTypeAndHubIdAndDeletedFalse(type, hubId).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int countByTypeAndHubId(DeliveryPersonType type, UUID hubId) {
        return deliveryPersonJpaRepository.countByTypeAndHubId(type, hubId);
    }

    @Override
    public int findMaxSeqByTypeAndHubId(DeliveryPersonType type, UUID hubId) {
        Integer maxSeq = deliveryPersonJpaRepository.findMaxSeqByTypeAndHubId(type, hubId);
        return maxSeq != null ? maxSeq : 0;
    }
}