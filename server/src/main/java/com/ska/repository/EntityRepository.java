package com.ska.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ska.model.syncable.Syncable;


@Repository
public interface EntityRepository extends JpaRepository<Syncable, Long> {

    Optional<Syncable> findByUuid(UUID uuid);

    List<Syncable> getAllByUserId(Long userId);

}
