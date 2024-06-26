package serviceTest;

import org.example.devicemanagement.domain.Device;
import org.example.devicemanagement.model.DeviceDTO;
import org.example.devicemanagement.repos.DeviceRepository;
import org.example.devicemanagement.service.DeviceService;
import org.example.devicemanagement.util.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeviceServiceTest {

	@Mock
	private DeviceRepository deviceRepository;

	@InjectMocks
	private DeviceService deviceService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testUpdateDevice_Success() {
		final UUID deviceId = UUID.randomUUID();
		final Device existingDevice = new Device("Device1", "Brand1", 10, OffsetDateTime.now());
		existingDevice.setId(deviceId);
		final DeviceDTO updatedDetails = new DeviceDTO("UpdatedDevice", "UpdatedBrand", 15);

		when(this.deviceRepository.findById(deviceId)).thenReturn(Optional.of(existingDevice));
		when(this.deviceRepository.save(any(Device.class))).thenReturn(existingDevice);

		final UUID updatedDeviceId = this.deviceService.update(deviceId, updatedDetails);

		assertEquals(deviceId, updatedDeviceId);
		verify(this.deviceRepository, times(1)).findById(deviceId);
		verify(this.deviceRepository, times(1)).save(existingDevice);
	}

	@Test
	void testUpdateDevice_NotFound() {
		final UUID deviceId = UUID.randomUUID();
		final DeviceDTO updatedDetails = new DeviceDTO("UpdatedDevice", "UpdatedBrand", 15);

		when(this.deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			this.deviceService.update(deviceId, updatedDetails);
		});

		verify(this.deviceRepository, times(1)).findById(deviceId);
		verify(this.deviceRepository, times(0)).save(any(Device.class));
	}

	@Test
	void testFindAllDevices_Success() {
		final Device device1 = new Device(UUID.randomUUID(), "Device1", "Brand1", 10, OffsetDateTime.now(), null);
		final Device device2 = new Device(UUID.randomUUID(), "Device2", "Brand2", 20, OffsetDateTime.now(), null);
		final Page<Device> devicePage = new PageImpl<>(List.of(device1, device2));
		final Pageable pageable = PageRequest.of(0, 2, Sort.by("id"));

		when(this.deviceRepository.findAll(pageable)).thenReturn(devicePage);

		final List<DeviceDTO> deviceDTOs = this.deviceService.findAll(pageable);

		assertEquals(2, deviceDTOs.size());
		assertEquals("Device1", deviceDTOs.get(0).getName());
		assertEquals("Device2", deviceDTOs.get(1).getName());
		verify(this.deviceRepository, times(1)).findAll(pageable);
	}

	@Test
	void testFindAllDevices_Empty() {
		final Pageable pageable = PageRequest.of(0, 2, Sort.by("name"));
		final Page<Device> emptyDevicePage = new PageImpl<>(List.of());

		when(this.deviceRepository.findAll(pageable)).thenReturn(emptyDevicePage);

		final List<DeviceDTO> deviceDTOs = this.deviceService.findAll(pageable);

		Assertions.assertTrue(deviceDTOs.isEmpty());
		verify(this.deviceRepository, times(1)).findAll(pageable);
	}

	@Test
	void testCreateDevice_Success() {
		final DeviceDTO deviceDTO = new DeviceDTO("Device1", "Brand1", 10);
		final Device device = new Device("Device1", "Brand1", 10, OffsetDateTime.now());
		final UUID deviceId = UUID.randomUUID();
		device.setId(deviceId);

		when(this.deviceRepository.save(any(Device.class))).thenAnswer(invocation -> {
			final Device savedDevice = invocation.getArgument(0);
			savedDevice.setId(deviceId);
			return savedDevice;
		});

		final UUID createdId = this.deviceService.create(deviceDTO);

		assertEquals(deviceId, createdId);
		verify(this.deviceRepository, times(1)).save(any(Device.class));
	}
	@Test
	void testCreateAnotherDevice_Success() {
		// Given
		final DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setName("Test Device");

		final UUID expectedId = UUID.randomUUID();

		when(this.deviceRepository.save(any(Device.class))).thenAnswer(invocation -> {
			final Device device = invocation.getArgument(0);
			// Simulate saving the device and setting the ID
			device.setId(expectedId);
			return device;
		});

		// When
		final UUID createdId = this.deviceService.create(deviceDTO);

		// Then
		assertEquals(expectedId, createdId);
	}

	@Test
	void testCreateDevice_ExceptionHandling() {
		// Given
		final DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setName("Test Device");

		when(this.deviceRepository.save(any(Device.class))).thenThrow(new RuntimeException("Failed to save device"));

		// When and Then
		assertThrows(RuntimeException.class, () -> this.deviceService.create(deviceDTO));
	}

	private DeviceDTO mapToDTO(final Device device) {
		final DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setName(device.getName());
		deviceDTO.setBrand(device.getBrand());
		deviceDTO.setQuantity(device.getQuantity());
		return deviceDTO;
	}
}
