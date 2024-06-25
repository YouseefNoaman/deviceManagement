package org.example.devicemanagement.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceDTO {

	@NotBlank
	@Size(max = 255)
	private String name;

	@NotBlank
	@Size(max = 255)
	private String brand;

	@Min(value = 0, message = "No devices of this type found")
	@Max(value = 100, message = "Warehouse is full")
	private Integer quantity;

}
