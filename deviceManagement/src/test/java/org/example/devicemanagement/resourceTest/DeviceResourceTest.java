package resourceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.devicemanagement.model.DeviceDTO;
import org.example.devicemanagement.rest.DeviceResource;
import org.example.devicemanagement.service.DeviceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DeviceResourceTest {

	@Mock
	private DeviceService deviceService;

	@InjectMocks
	private DeviceResource deviceResource;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@Before
	public void setup() {
		MockitoAnnotations.openMocks(this);
		JacksonTester.initFields(this, new ObjectMapper());
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.deviceResource).build();
		this.objectMapper = new ObjectMapper();
	}

	@After
	public void after() throws Exception {
		MockitoAnnotations.openMocks(this).close();
	}

	@Test
	public void testGetAllDevices() throws Exception {

		final DeviceDTO device1 = new DeviceDTO("Device 1", "brand1", 1);
		final DeviceDTO device2 = new DeviceDTO("Device 2", "brand2", 2);

		final Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
		when(this.deviceService.findAll(pageable)).thenReturn(List.of(device1, device2));

		// When and Then
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/devices").param("page", "0").param("size", "10")
						.param("sort", "id,asc"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$.[0].name").value(device1.getName()))
				.andExpect(jsonPath("$[0].brand").value(device1.getBrand()))
				.andExpect(jsonPath("$[1].name").value(device2.getName()))
				.andExpect(jsonPath("$[1].brand").value(device2.getBrand())).andDo(print());
	}

	@Test
	public void testGetDevice_returnDevice() throws Exception {

		final DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setName("Test Device");
		deviceDTO.setBrand("Test Brand");
		deviceDTO.setQuantity(8);

		final UUID createdId = UUID.randomUUID();
		when(this.deviceService.get(createdId)).thenReturn(deviceDTO);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/api/v1/devices/{id}", createdId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value(deviceDTO.getName()));

		verify(this.deviceService, times(1)).get(createdId);
	}

	@Test
	public void testGetEmptyDevice() throws Exception {

		final DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setName("Test Device");
		deviceDTO.setBrand("Test Brand");
		deviceDTO.setQuantity(8);

		final UUID createdId = UUID.randomUUID();
		when(this.deviceService.get(createdId)).thenReturn(null);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/tutorials/{id}", createdId))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testCreateDevice() throws Exception {
		// Given
		final DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setName("Test Device");
		deviceDTO.setBrand("Test Brand");
		deviceDTO.setQuantity(10);

		final UUID createdId = UUID.randomUUID();
		when(this.deviceService.create(deviceDTO)).thenReturn(createdId);

		// Convert deviceDTO to JSON
		final String jsonDevice = this.objectMapper.writeValueAsString(deviceDTO);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/v1/devices").contentType(MediaType.APPLICATION_JSON)
						.content(jsonDevice).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andDo(print());
		// .andExpect(jsonPath("$.name").value(createdId));

		// verify(this.deviceService, times(1)).create(deviceDTO);
	}

	@Test
	public void testUpdateDevice() throws Exception {
		// Given
		final UUID deviceId = UUID.randomUUID();
		final DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setName("Updated Device");
		deviceDTO.setBrand("Updated Brand");
		deviceDTO.setQuantity(3);

		// Convert deviceDTO to JSON
		final String jsonDevice = this.objectMapper.writeValueAsString(deviceDTO);

		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/api/v1/devices/{id}", deviceId)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonDevice))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testPatchDevice() throws Exception {
		// Given
		final UUID deviceId = UUID.randomUUID();
		final DeviceDTO deviceDTO = new DeviceDTO();
		deviceDTO.setName("Patched Device");
		deviceDTO.setBrand("Patched Brand");
		deviceDTO.setQuantity(9);

		// Convert deviceDTO to JSON
		final String jsonDevice = this.objectMapper.writeValueAsString(deviceDTO);

		// When and Then
		this.mockMvc
				.perform(MockMvcRequestBuilders.patch("/api/v1/devices/{id}", deviceId)
						.contentType(MediaType.APPLICATION_JSON).content(jsonDevice))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testDeleteDevice() throws Exception {
		// Given
		final UUID deviceId = UUID.randomUUID();

		// When and Then
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/devices/{id}", deviceId))
				.andExpect(status().isNoContent());
	}
}
