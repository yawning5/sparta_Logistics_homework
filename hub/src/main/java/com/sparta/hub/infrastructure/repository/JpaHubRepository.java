package com.sparta.hub.infrastructure.repository;

import com.sparta.hub.domain.entity.Hub;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaHubRepository extends JpaRepository<Hub, UUID>, JpaSpecificationExecutor<Hub> {
}
