package com.reho.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reho.persistence.entities.ServiCita;
import com.reho.persistence.entities.Servicio;
import com.reho.persistence.repository.ServicioRepository;
import com.reho.service.dto.ServiCitaDTO;

@Component
public class ServiCitaMapper {

    @Autowired
    private ServicioRepository servicioRepository;

    public ServiCitaDTO toDTO(ServiCita serviCita) {
        ServiCitaDTO dto = new ServiCitaDTO();

        dto.setId(serviCita.getId());
        dto.setIdCita(serviCita.getIdCita());
        dto.setIdServicio(serviCita.getIdServicio());
        dto.setServicioNombre(serviCita.getServicio().getNombre());
        dto.setServicioPrecio(serviCita.getServicio().getPrecio());
        dto.setServicioDescripcion(serviCita.getServicio().getDescripcion());
       
        return dto;
    }

    public ServiCita toEntity(ServiCitaDTO dto) {
        ServiCita serviCita = new ServiCita();

        serviCita.setId(dto.getId());
        serviCita.setIdCita(dto.getIdCita());
        serviCita.setIdServicio(dto.getIdServicio());

        Servicio servicio = servicioRepository.findById(dto.getIdServicio()).get();
        serviCita.setServicio(servicio);

        return serviCita;
    }
}
