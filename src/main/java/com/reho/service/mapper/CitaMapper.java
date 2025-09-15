package com.reho.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reho.persistence.entities.Cita;
import com.reho.persistence.entities.Pago;
import com.reho.persistence.entities.Servicio;
import com.reho.persistence.entities.Vehiculo;
import com.reho.persistence.repository.PagoRepository;
import com.reho.persistence.repository.ServicioRepository;
import com.reho.persistence.repository.VehiculoRepository;
import com.reho.service.dto.CitaDTO;

@Component
public class CitaMapper {

	@Autowired
	private VehiculoRepository vehiculoRepository;

	@Autowired
	private ServicioRepository servicioRepository;

	@Autowired
	private PagoRepository pagoRepository;

	public CitaDTO toDTO(Cita cita) {
		CitaDTO dto = new CitaDTO();
		dto.setId(cita.getId());
		dto.setIdVehiculo(cita.getIdVehiculo());
		dto.setVehiculo(cita.getVehiculo() != null ? cita.getVehiculo().getMatricula() : null);
		dto.setNombreUsuario(cita.getVehiculo() != null && cita.getVehiculo().getUsuario() != null
				? cita.getVehiculo().getUsuario().getNombre()
				: null);
		dto.setIdServicio(cita.getIdServicio());
		dto.setServicio(cita.getServicio() != null ? cita.getServicio().getNombre() : null);
		dto.setIdPago(cita.getPago() != null ? cita.getPago().getId() : null); // Obtener ID del pago asociado
		dto.setFecha(cita.getFecha());
		dto.setHora(cita.getHora());
		dto.setEstado(cita.getEstado());
		return dto;
	}

	public Cita toEntity(CitaDTO dto) {
		Cita cita = new Cita();

		cita.setId(dto.getId());
		cita.setIdVehiculo(dto.getIdVehiculo());
		cita.setIdServicio(dto.getIdServicio());
		cita.setFecha(dto.getFecha());
		cita.setHora(dto.getHora());
		cita.setEstado(dto.getEstado());

		Vehiculo vehiculo = vehiculoRepository.findById(dto.getIdVehiculo()).orElseThrow(
				() -> new IllegalArgumentException("Vehículo no encontrado con ID: " + dto.getIdVehiculo()));
		cita.setVehiculo(vehiculo);

		Servicio servicio = servicioRepository.findById(dto.getIdServicio()).orElseThrow(
				() -> new IllegalArgumentException("Servicio no encontrado con ID: " + dto.getIdServicio()));
		cita.setServicio(servicio);

		if (dto.getIdPago() != 0) {
			Pago pago = pagoRepository.findById(dto.getIdPago())
					.orElseThrow(() -> new IllegalArgumentException("Pago no encontrado con ID: " + dto.getIdPago()));
			cita.setPago(pago);
			pago.setCita(cita);
		}
		return cita;
	}
}
