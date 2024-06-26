package org.example.devicemanagement.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "Devices")
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class Device {

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue
	@UuidGenerator
	private UUID id;

	@Column(nullable = false, unique = true)
	@NotBlank
	private String name;

	@Column(nullable = false)
	@NotBlank
	private String brand;

	@Column(nullable = false)
	@Min(value = 0, message = "No devices of this type found")
	@Max(value = 100, message = "Warehouse is full")
	private Integer quantity;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private OffsetDateTime dateCreated;

	@LastModifiedDate
	@Column(nullable = false)
	private OffsetDateTime lastUpdated;

	public Device(final String name, final String brand, final Integer quantity, final OffsetDateTime dateCreated) {
		this.name = name;
		this.brand = brand;
		this.quantity = quantity;
		this.dateCreated = dateCreated;
	}

	public UUID getId() {
		return this.id;
	}

	public @NotBlank String getName() {
		return this.name;
	}

	public @NotBlank String getBrand() {
		return this.brand;
	}

	public @Min(value = 0, message = "No devices of this type found") @Max(value = 100, message = "Warehouse is full") Integer getQuantity() {
		return this.quantity;
	}

	public OffsetDateTime getDateCreated() {
		return this.dateCreated;
	}

	public OffsetDateTime getLastUpdated() {
		return this.lastUpdated;
	}

	public void setId(final UUID id) {
		this.id = id;
	}

	public void setName(@NotBlank final String name) {
		this.name = name;
	}

	public void setBrand(@NotBlank final String brand) {
		this.brand = brand;
	}

	public void setQuantity(
			@Min(value = 0, message = "No devices of this type found") @Max(value = 100, message = "Warehouse is full") final Integer quantity) {
		this.quantity = quantity;
	}

	public void setDateCreated(final OffsetDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setLastUpdated(final OffsetDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
