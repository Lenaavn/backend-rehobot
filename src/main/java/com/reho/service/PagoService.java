package com.reho.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reho.persistence.entities.Cita;
import com.reho.persistence.entities.Pago;
import com.reho.persistence.entities.Servicio;
import com.reho.persistence.entities.enums.Estado;
import com.reho.persistence.entities.enums.MetodoPago;
import com.reho.persistence.repository.CitaRepository;
import com.reho.persistence.repository.PagoRepository;

@Service
public class PagoService {

	@Autowired
	private PagoRepository pagoRepository;

	@Autowired
	private CitaRepository citaRepository;

	public List<Pago> findAll() {
		return this.pagoRepository.findAll();
	}

	public boolean existPago(int idPago) {
		return this.pagoRepository.existsById(idPago);
	}

	public Optional<Pago> findById(int idPago) {
		return this.pagoRepository.findById(idPago);
	}

	public Pago save(Pago pago) {

		Pago existingPago = pagoRepository.findById(pago.getId()).get();

		if (pago.getCita() == null) {
			throw new IllegalArgumentException("El Pago debe estar asociado a una Cita");
		}

		if (pago.getIdCita() != null) {
			existingPago.setIdCita(pago.getIdCita());
		}

		if (pago.getMonto() != null) {
			existingPago.setMonto(pago.getMonto());
		}

		if (pago.getMetodoPago() != null) {
			existingPago.setMetodoPago(pago.getMetodoPago());
		}

		existingPago.setCita(pago.getCita());

		return this.pagoRepository.save(existingPago);
	}

	public Pago actualizarPagoYCambiarEstadoCita(Pago pago, MetodoPago metodoPago) {
	    // Actualizar el método de pago
	    pago.setMetodoPago(metodoPago);

	    // Obtener la cita asociada al pago
	    Cita cita = pago.getCita();
	    if (cita != null) {
	        // Obtener el servicio asociado a la cita
	        Servicio servicio = cita.getServicio();
	        if (servicio != null) {
	            // Calcular el monto total con el precio del servicio
	            double montoTotal = servicio.getPrecio();
	            pago.setMonto(montoTotal); // Actualizar el monto en el pago

	            // Cambiar el estado de la cita a PAGADA
	            cita.setEstado(Estado.PAGADA);

	            // Sincronizar el pago con la cita (si no está ya sincronizado)
	            if (cita.getPago() == null || !cita.getPago().equals(pago)) {
	                cita.setPago(pago);
	            }

	            // Guardar los cambios en la cita
	            this.citaRepository.save(cita);

	        } else {
	            throw new IllegalStateException("El servicio asociado a la cita no existe.");
	        }
	    } else {
	        throw new IllegalStateException("La cita asociada al pago no existe.");
	    }

	    // Guardar el pago con el nuevo monto y método
	    return this.pagoRepository.save(pago);
	}

	// Obtener los pagos de un usuario específico
    public List<Pago> obtenerPagosPorUsuario(int idUsuario) {
        return pagoRepository.findByCitaVehiculoUsuarioId(idUsuario);
    }

}
