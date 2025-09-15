package com.reho.service.dto;

import java.util.List;

import com.reho.persistence.entities.Usuario;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehiculoDTO {
    private Integer id;
    private Integer idUsuario;
    private String marca;
    private String modelo;
    private String matricula;
    private Usuario usuario;
    private List<CitaSimplificadaDTO> citas;
}


