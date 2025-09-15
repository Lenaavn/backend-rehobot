package com.reho.persistence.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "servicita")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_servicio", nullable = false)
    private int idServicio;

    @Column(name = "id_cita", nullable = false)
    private int idCita;

    @Column(length = 500)
    private String comentario;

    @Column(columnDefinition = "DECIMAL(2,1)")
    private Double valoracion;
    
    @Column(name = "activo")
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_cita", referencedColumnName = "id", insertable = false, updatable = false)
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "id_servicio", referencedColumnName = "id", insertable = false, updatable = false)
    private Servicio servicio;
}


