package org.example.devicemanagement.rest;

import jakarta.validation.Valid;
import org.example.devicemanagement.model.DeviceDTO;
import org.example.devicemanagement.service.DeviceService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviceResource {

	private final DeviceService deviceService;

	public DeviceResource(final DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	@GetMapping
	public ResponseEntity<List<DeviceDTO>> getAllDevices(@RequestParam(defaultValue = "0") final int page,
			@RequestParam(defaultValue = "10") final int size,
			@RequestParam(defaultValue = "id,asc") final String[] sort) {
		final Sort.Direction sortDirection = sort[1].equalsIgnoreCase("desc")
				? Sort.Direction.DESC
				: Sort.Direction.ASC;
		final Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort[0]));

		return ResponseEntity.ok(this.deviceService.findAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DeviceDTO> getDevice(@PathVariable(name = "id") final UUID id) {
		return ResponseEntity.ok(this.deviceService.get(id));
	}

	@PostMapping
	public ResponseEntity<UUID> createDevice(@RequestBody @Valid final DeviceDTO deviceDTO) {
		final UUID createdId = this.deviceService.create(deviceDTO);
		return new ResponseEntity<>(createdId, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UUID> updateDevice(@PathVariable(name = "id") final UUID id,
			@RequestBody @Valid final DeviceDTO deviceDTO) {
		this.deviceService.update(id, deviceDTO);
		return ResponseEntity.ok(id);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<UUID> patchDevice(@PathVariable(name = "id") final UUID id,
			@RequestBody @Valid final DeviceDTO deviceDTO) {
		this.deviceService.update(id, deviceDTO);
		return ResponseEntity.ok(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDevice(@PathVariable(name = "id") final UUID id) {
		this.deviceService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
