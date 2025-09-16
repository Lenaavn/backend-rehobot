package com.reho.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reho.persistence.entities.enums.Estado;
import com.reho.persistence.entities.enums.Nombre;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CitaDTO {

	private Integer id;
	private int idVehiculo;
	private String vehiculo;
	private int idServicio;
	private Nombre servicio;
	private String nombreUsuario;
	private int idPago;
    private boolean usuarioActivo;
	
	@JsonFormat( pattern = "yyyy-MM-dd")
	private LocalDate fecha;
	
	@JsonFormat( pattern = "HH:mm")
	private LocalTime hora;
	
	private Estado estado;
}
