package com.reho.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reho.persistence.entities.Cita;
import com.reho.persistence.entities.Pago;
import com.reho.persistence.entities.enums.Estado;
import com.reho.persistence.entities.enums.MetodoPago;
import com.reho.persistence.repository.CitaRepository;
import com.reho.persistence.repository.PagoRepository;

@Service
public class CitaService {

	@Autowired
	private CitaRepository citaRepository;

	@Autowired
	private PagoRepository pagoRepository;

	public List<Cita> findAll() {
		return this.citaRepository.findByActivoTrue();
	}

	public boolean existsCita(int idCita) {
		return this.citaRepository.existsById(idCita);
	}

	public Optional<Cita> findById(int idCita) {
		return this.citaRepository.findById(idCita);
	}

	public Cita create(Cita cita) {
		// Establecer estado predeterminado de la cita
		if (cita.getEstado() == null) {
			cita.setEstado(Estado.NO_PAGADA);
		}

		// Guardar la cita inicialmente para obtener su ID
		Cita citaGuardada = citaRepository.save(cita);

		// Crear el pago y asociarlo a la cita
		Pago pago = new Pago();
		pago.setMonto(0.0);
		pago.setMetodoPago(MetodoPago.SIN_DETERMINAR);

		// Relación bidireccional: asociar la cita al pago
		pago.setCita(citaGuardada);
		Pago pagoGuardado = pagoRepository.save(pago);

		// Actualizar la cita con el ID del pago
		citaGuardada.setPago(pagoGuardado);
		citaGuardada.setIdPago(pagoGuardado.getId()); // Actualizar idPago manualmente

		// Guardar nuevamente la cita para reflejar los cambios en la base de datos
		return citaRepository.save(citaGuardada);
	}

	public Cita save(Cita cita) {
		// Verificar que el pago esté asociado
		if (cita.getPago() == null) {
			throw new IllegalArgumentException("La cita debe tener un pago asociado.");
		}

		return citaRepository.save(cita);
	}

	public boolean delete(int idCita) {
		boolean result = false;

		if (this.citaRepository.existsById(idCita)) {
			this.citaRepository.deleteById(idCita);
			result = true;
		}

		return result;
	}

	// Método para obtener citas pagadas
	public List<Cita> findCitasPagadas() {
		return citaRepository.findAll().stream().filter(cita -> cita.getEstado() == Estado.PAGADA)
				.collect(Collectors.toList());
	}

	// Método para obtener citas no pagadas
	public List<Cita> findCitasNoPagadas() {
		return citaRepository.findAll().stream().filter(cita -> cita.getEstado() == Estado.NO_PAGADA)
				.collect(Collectors.toList());
	}

	// Método para obtener citas por id del usuaio a traves de vehiculo
	public List<Cita> findCitasPorUsuario(int idUsuario) {
		return citaRepository.findByVehiculo_IdUsuario(idUsuario);
	}

	public List<LocalTime> getHorasOcupadas(int idServicio, LocalDate fecha) {
		return citaRepository.findByServicio_IdAndFecha(idServicio, fecha).stream().map(Cita::getHora)
				.collect(Collectors.toList());
	}
	
	public List<Cita> obtenerCitasDeHoy() {
        LocalDate hoy = LocalDate.now();
        return citaRepository.findByFecha(hoy);
    }

}
