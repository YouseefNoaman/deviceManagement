package org.example.devicemanagement.service;

import jakarta.validation.Valid;
import org.example.devicemanagement.domain.Device;
import org.example.devicemanagement.model.DeviceDTO;
import org.example.devicemanagement.repos.DeviceRepository;
import org.example.devicemanagement.util.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DeviceService {

	private final DeviceRepository deviceRepository;

	public DeviceService(final DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	public List<DeviceDTO> findAll(final Pageable pageable) {
		final Page<Device> devices = this.deviceRepository.findAll(pageable);
		return devices.stream().map(device -> this.mapToDTO(device, new DeviceDTO())).toList();
	}

	public DeviceDTO get(final UUID id) {
		return this.deviceRepository.findById(id).map(device -> this.mapToDTO(device, new DeviceDTO()))
				.orElseThrow(NotFoundException::new);
	}

	public UUID create(final @Valid DeviceDTO deviceDTO) {
		final Device device = new Device();
		this.mapToEntity(deviceDTO, device);
		return this.deviceRepository.save(device).getId();
	}

	public void update(final UUID id, final DeviceDTO deviceDTO) {
		final Device device = this.deviceRepository.findById(id).orElseThrow(NotFoundException::new);
		this.updateMapToEntity(deviceDTO, device);
		this.deviceRepository.save(device);
	}

	public void delete(final UUID id) {
		this.deviceRepository.deleteById(id);
	}

	private DeviceDTO mapToDTO(final Device device, final DeviceDTO deviceDTO) {
		deviceDTO.setName(device.getName());
		deviceDTO.setBrand(device.getBrand());
		deviceDTO.setQuantity(device.getQuantity());
		return deviceDTO;
	}

	private Device mapToEntity(final DeviceDTO deviceDTO, final Device device) {
		device.setName(deviceDTO.getName());
		device.setBrand(deviceDTO.getBrand());
		device.setQuantity(deviceDTO.getQuantity());
		return device;
	}

	private Device updateMapToEntity(final DeviceDTO deviceDTO, final Device device) {
		device.setName(deviceDTO.getName());
		device.setBrand(deviceDTO.getBrand());
		device.setQuantity(deviceDTO.getQuantity());
		device.setLastUpdated(OffsetDateTime.now());
		return device;
	}

}
