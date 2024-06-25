package org.example.devicemanagement.repos;

import org.example.devicemanagement.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

	boolean existsByNameIgnoreCase(String name);

}
