package com.keepgoing.hub.infrastructure.repository;

import com.keepgoing.hub.domain.entity.Hub;
import com.keepgoing.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HubRepositoryAdapter implements HubRepository {

    private final JpaHubRepository jpaHubRepository;

    @Override
    public Hub save(Hub hub) {
        return jpaHubRepository.save(hub);
    }

    @Override
    public Optional<Hub> findById(UUID id) {
        return jpaHubRepository.findById(id);
    }

    @Override
    public List<Hub> findAll() {
        return jpaHubRepository.findAll();
    }

    @Override
    public void delete(Hub hub) {
        jpaHubRepository.delete(hub);
    }
}