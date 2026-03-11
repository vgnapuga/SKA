package com.ska.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ska.model.Device;


@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByUuid(UUID uuid);

    List<Device> getAllByOwnerId(Long ownerId);

}
