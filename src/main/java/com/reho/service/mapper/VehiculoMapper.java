package com.reho.service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.reho.persistence.entities.Usuario;
import com.reho.persistence.entities.Vehiculo;
import com.reho.service.dto.CitaSimplificadaDTO;
import com.reho.service.dto.VehiculoDTO;

@Component
public class VehiculoMapper {

	// Convierte un Vehiculo (entidad) a VehiculoDTO
	public VehiculoDTO toDTO(Vehiculo vehiculo) {
		VehiculoDTO dto = new VehiculoDTO();
		dto.setId(vehiculo.getId());
		dto.setIdUsuario(vehiculo.getUsuario() != null ? vehiculo.getUsuario().getId() : null);
		dto.setMarca(vehiculo.getMarca());
		dto.setModelo(vehiculo.getModelo());
		dto.setMatricula(vehiculo.getMatricula());

		// Mapear Usuario (si existe)
		if (vehiculo.getUsuario() != null) {
			Usuario usuario = new Usuario();
			usuario.setId(vehiculo.getUsuario().getId());
			usuario.setNombre(vehiculo.getUsuario().getNombre());
			usuario.setEmail(vehiculo.getUsuario().getEmail());
			usuario.setContrasena(vehiculo.getUsuario().getContrasena());
			usuario.setRol(vehiculo.getUsuario().getRol());
			usuario.setTelefono(vehiculo.getUsuario().getTelefono());
			dto.setUsuario(usuario);
		}

		// Mapear la lista de Citas a CitaSimplificadaDTO
		List<CitaSimplificadaDTO> citasSimplificadas = vehiculo.getCitas() != null
				? vehiculo.getCitas().stream().map(cita -> {
					CitaSimplificadaDTO citaDTO = new CitaSimplificadaDTO();
					citaDTO.setId(cita.getId());
					citaDTO.setFecha(cita.getFecha());
					citaDTO.setHora(cita.getHora());
					citaDTO.setEstado(cita.getEstado());
					return citaDTO;
				}).collect(Collectors.toList())
				: List.of(); // Manejar el caso de lista nula

		dto.setCitas(citasSimplificadas);
		return dto;
	}

	// Convierte un VehiculoDTO a Vehiculo (entidad)
	public Vehiculo toEntity(VehiculoDTO dto) {
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setId(dto.getId());
		vehiculo.setIdUsuario(dto.getIdUsuario());
		vehiculo.setMarca(dto.getMarca());
		vehiculo.setModelo(dto.getModelo());
		vehiculo.setMatricula(dto.getMatricula());
		return vehiculo;
	}
}
