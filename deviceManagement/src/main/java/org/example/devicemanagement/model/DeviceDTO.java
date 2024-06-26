package org.example.devicemanagement.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1398949794185445462L;
	
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
