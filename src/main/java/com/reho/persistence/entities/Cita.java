package com.reho.persistence.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.reho.persistence.entities.enums.Estado;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cita")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_vehiculo", nullable = false)
    private Integer idVehiculo;

    @Column(name = "id_servicio", nullable = false)
    private Integer idServicio;

    @Column(name = "id_pago", nullable = true) 
    private Integer idPago;

    @Column(columnDefinition = "DATE", nullable = false)
    private LocalDate fecha;

    @Column(columnDefinition = "TIME", nullable = false)
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", referencedColumnName = "id", insertable = false, updatable = false)
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "id_servicio", referencedColumnName = "id", insertable = false, updatable = false)
    private Servicio servicio;

    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Pago pago;
 
}


