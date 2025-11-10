package com.keepgoing.hub.domain.repository;

import com.keepgoing.hub.domain.entity.Hub;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository {

    Hub save(Hub hub);

    Optional<Hub> findById(UUID id);

    List<Hub> findAll();

    void delete(Hub hub);
}
