package com.reho.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.reho.persistence.entities.enums.Estado;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CitaSimplificadaDTO {
    private Integer id;
    private LocalDate fecha;
    private LocalTime hora;
    private Estado estado;   
}
