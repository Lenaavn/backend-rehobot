package com.reho.service.dto;

import com.reho.persistence.entities.enums.Nombre;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiCitaDTO {
	
	private Integer id;
    private Integer idServicio;
    private Integer idCita;
    private Nombre servicioNombre; 
    private Double servicioPrecio;
    private String servicioDescripcion;

}
