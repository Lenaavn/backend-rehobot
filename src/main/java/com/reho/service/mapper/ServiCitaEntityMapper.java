package com.reho.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reho.persistence.entities.Cita;
import com.reho.persistence.entities.ServiCita;
import com.reho.persistence.repository.CitaRepository;
import com.reho.service.dto.ServiCitaEntityDTO;

@Component
public class ServiCitaEntityMapper {

    @Autowired
    private CitaRepository citaRepository;

    public ServiCitaEntityDTO toDTO(ServiCita serviCita) {
        ServiCitaEntityDTO dto = new ServiCitaEntityDTO();
        dto.setId(serviCita.getId());
        dto.setIdServicio(serviCita.getIdServicio());
        dto.setIdCita(serviCita.getIdCita());
        dto.setComentario(serviCita.getComentario());
        dto.setValoracion(serviCita.getValoracion());

        String nombreServicio = (serviCita.getServicio() != null && serviCita.getServicio().getNombre() != null)
                ? serviCita.getServicio().getNombre().toString()
                : "Servicio no especificado";
        dto.setServicio(nombreServicio);

        String nombreUsuario = (serviCita.getCita() != null && serviCita.getCita().getVehiculo() != null
                && serviCita.getCita().getVehiculo().getUsuario() != null)
                ? serviCita.getCita().getVehiculo().getUsuario().getNombre()
                : "Usuario no especificado";
        dto.setNombreUsuario(nombreUsuario);

        return dto;
    }

    public ServiCita toEntity(ServiCitaEntityDTO dto) {
        ServiCita serviCita = new ServiCita();
        serviCita.setId(dto.getId());
        serviCita.setIdCita(dto.getIdCita());
        serviCita.setComentario(dto.getComentario());
        serviCita.setValoracion(dto.getValoracion());

        if (dto.getIdCita() > 0) {
            Cita cita = citaRepository.findById(dto.getIdCita())
                    .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + dto.getIdCita()));
            serviCita.setCita(cita);
            serviCita.setIdServicio(cita.getServicio().getId());
        }

        return serviCita;
    }
}






