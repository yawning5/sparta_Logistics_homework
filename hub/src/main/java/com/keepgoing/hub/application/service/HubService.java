package com.keepgoing.hub.application.service;

import com.keepgoing.hub.application.command.CreateHubCommand;
import com.keepgoing.hub.application.command.UpdateHubCommand;
import com.keepgoing.hub.application.dto.HubResponse;
import com.keepgoing.hub.application.exception.ErrorCode;
import com.keepgoing.hub.application.exception.ForbiddenOperationException;
import com.keepgoing.hub.domain.entity.Hub;
import com.keepgoing.hub.domain.repository.HubRepository;
import com.keepgoing.hub.infrastructure.repository.JpaHubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HubService {

    private final HubRepository hubRepository;
    private final JpaHubRepository jpaHubRepository;

    public HubResponse createHub(CreateHubCommand command, Long createdBy) {
        command.validate();
        Hub hub = Hub.create(command.name(), command.address(), command.latitude(), command.longitude(), command.hubStatus());
        hubRepository.save(hub);
        return HubResponse.from(hub);
    }

    @Transactional(readOnly = true)
    public HubResponse getHub(UUID id) {
        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new ForbiddenOperationException(ErrorCode.HUB_NOT_FOUND));
        hub.checkDeleted();
        return HubResponse.from(hub);
    }

    @Transactional(readOnly = true)
    public Page<HubResponse> searchHubs(String name, String address, String hubStatus, Pageable pageable) {
        Specification<Hub> spec = (root, query, cb) -> {
            Predicate predicate = cb.isNull(root.get("deletedAt"));
            if (name != null && !name.isBlank()) {
                predicate = cb.and(predicate, cb.like(root.get("name"), "%" + name + "%"));
            }
            if (address != null && !address.isBlank()) {
                predicate = cb.and(predicate, cb.like(root.get("address"), "%" + address + "%"));
            }
            if (hubStatus != null && !hubStatus.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("hubStatus"), hubStatus));
            }
            return predicate;
        };

        Page<Hub> page = jpaHubRepository.findAll(spec, pageable);
        return page.map(HubResponse::from);
    }

    public HubResponse updateHub(UUID id, UpdateHubCommand command, Long updatedBy) {
        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new ForbiddenOperationException(ErrorCode.HUB_NOT_FOUND));
        hub.checkDeleted();
        hub.updateHub(command);
        return HubResponse.from(hub);
    }

    public void deleteHub(UUID id, Long deletedBy) {
        Hub hub = hubRepository.findById(id)
                .orElseThrow(() -> new ForbiddenOperationException(ErrorCode.HUB_NOT_FOUND));
        hub.checkDeleted();
        hub.delete(deletedBy);
    }
}