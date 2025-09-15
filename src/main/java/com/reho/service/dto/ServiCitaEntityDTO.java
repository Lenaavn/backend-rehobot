package com.reho.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiCitaEntityDTO {

    private Integer id;
    private int idServicio;
    private int idCita;
    private String servicio; 
    private String comentario;
    private Double valoracion;
    private String nombreUsuario;
}

