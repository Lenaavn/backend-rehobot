package com.reho.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reho.persistence.entities.Cita;
import com.reho.persistence.entities.ServiCita;
import com.reho.persistence.repository.CitaRepository;
import com.reho.persistence.repository.ServiCitaRepository;
import com.reho.service.dto.ServiCitaEntityDTO;
import com.reho.service.mapper.ServiCitaEntityMapper;

@Service
public class ServiCitaService {

	@Autowired
	private ServiCitaRepository serviCitaRepository;

	@Autowired
	private CitaRepository citaRepository;

	@Autowired
	private ServiCitaEntityMapper serviCitaEntityMapper;

	public List<ServiCitaEntityDTO> findAll() {
		return serviCitaRepository.findByActivoTrue().stream().filter(ServiCita::isActivo) // Mostrar solo las activas
				.map(serviCitaEntityMapper::toDTO).collect(Collectors.toList());
	}

	public Optional<ServiCitaEntityDTO> findById(int id) {
		return serviCitaRepository.findById(id).map(serviCitaEntityMapper::toDTO);
	}

	public ServiCitaEntityDTO create(ServiCitaEntityDTO dto) {
		if (dto.getIdCita() <= 0) {
			throw new IllegalArgumentException("El 'idCita' no puede ser nulo o negativo.");
		}

		Cita cita = citaRepository.findById(dto.getIdCita())
				.orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + dto.getIdCita()));

		dto.setIdServicio(cita.getServicio().getId());

		ServiCita serviCita = serviCitaEntityMapper.toEntity(dto);
		ServiCita savedServiCita = serviCitaRepository.save(serviCita);

		ServiCita completeServiCita = serviCitaRepository.findById(savedServiCita.getId())
				.orElseThrow(() -> new IllegalStateException("Error al cargar el ServiCita completo."));

		return serviCitaEntityMapper.toDTO(completeServiCita);
	}

	public ServiCitaEntityDTO save(ServiCitaEntityDTO dto) {
		ServiCita existingServiCita = serviCitaRepository.findById(dto.getId())
				.orElseThrow(() -> new IllegalArgumentException("ServiCita no encontrada con ID: " + dto.getId()));

		if (dto.getIdCita() > 0) {
			Cita cita = citaRepository.findById(dto.getIdCita())
					.orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + dto.getIdCita()));
			existingServiCita.setIdCita(dto.getIdCita());
			existingServiCita.setIdServicio(cita.getServicio().getId());
		}

		existingServiCita.setComentario(dto.getComentario());
		existingServiCita.setValoracion(dto.getValoracion());

		ServiCita updatedServiCita = serviCitaRepository.save(existingServiCita);

		ServiCita completeServiCita = serviCitaRepository.findById(updatedServiCita.getId())
				.orElseThrow(() -> new IllegalStateException("Error al cargar el ServiCita completo."));

		return serviCitaEntityMapper.toDTO(completeServiCita);
	}

	public boolean delete(int id) {
		if (serviCitaRepository.existsById(id)) {
			serviCitaRepository.deleteById(id);
			return true;
		}
		return false;
	}

	public boolean existServiCita(int id) {
		return serviCitaRepository.existsById(id);
	}

	public List<ServiCitaEntityDTO> findTopFiveValoraciones() {
	    return serviCitaRepository.findTop5ByActivoTrueOrderByValoracionDesc().stream()
	            .map(serviCitaEntityMapper::toDTO)
	            .collect(Collectors.toList());
	}


	// Obtener los valoraciones de un usuario específico
	public List<ServiCitaEntityDTO> obtenerPagosPorUsuario(int idUsuario) {
		return serviCitaRepository.findByCitaVehiculoIdUsuario(idUsuario).stream().filter(ServiCita::isActivo)
				.map(serviCitaEntityMapper::toDTO)
				.collect(Collectors.toList());
	}

	public boolean desactivar(int id) {
		Optional<ServiCita> valo = serviCitaRepository.findById(id);
		if (valo.isPresent()) {
			ServiCita serviCita = valo.get();
			serviCita.setActivo(false);
			serviCitaRepository.save(serviCita);
			return true;
		}
		return false;
	}

}
