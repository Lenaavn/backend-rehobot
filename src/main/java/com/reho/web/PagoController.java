package com.reho.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reho.persistence.entities.Pago;
import com.reho.persistence.entities.enums.MetodoPago;
import com.reho.service.PagoService;

@RestController
@RequestMapping("/pagos")
public class PagoController {

	@Autowired
	private PagoService pagoService;

	@GetMapping
	public ResponseEntity<List<Pago>> list() {
		return ResponseEntity.ok(this.pagoService.findAll());
	}

	@GetMapping("/{idPago}")
	public ResponseEntity<Pago> findById(@PathVariable int idPago) {
		if (this.pagoService.existPago(idPago)) {
			return ResponseEntity.ok(this.pagoService.findById(idPago).get());
		}

		return ResponseEntity.notFound().build();

	}

	@PutMapping("/{idPago}")
	// ResponseEntity<?> para permitir diferentes tipos de respuesta
	public ResponseEntity<?> update(@PathVariable int idPago, @RequestBody Pago pago) {
		if (pago.getId() == null || idPago != pago.getId()) {
			return ResponseEntity.badRequest().body("El ID de la URL no coincide con el ID del cuerpo del pago.");
		}

		if (pago.getIdCita() == null) {
			return ResponseEntity.badRequest().body("El campo 'idCita' no puede ser nulo.");
		}

		if (pago.getMetodoPago() == null) {
			return ResponseEntity.badRequest().body("El campo 'metodoPago' no puede ser nulo.");
		}

		if (!this.pagoService.existPago(idPago)) {
			return ResponseEntity.notFound().build();
		}

		// Obtener el Pago existente para mantener datos anteriores no enviados
		Pago pagoExistente = this.pagoService.findById(idPago).get();

		// Mantener la asociación con la cita existente
		pago.setCita(pagoExistente.getCita());

		return ResponseEntity.ok(this.pagoService.save(pago));
	}

	@PutMapping("/{idPago}/pagar")
	// ResponseEntity<?> para permitir diferentes tipos de respuesta
	public ResponseEntity<?> actualizarMetodoPago(@PathVariable int idPago, @RequestParam String nuevoMetodoPago) {

		if (!this.pagoService.existPago(idPago)) {
			return ResponseEntity.notFound().build();
		}

		Pago pago = this.pagoService.findById(idPago).get();

		if (pago.getMetodoPago() != MetodoPago.SIN_DETERMINAR) {
			return ResponseEntity.badRequest().body("El pago ya ha sido realizado y no puede modificarse.");
		}

		if (!nuevoMetodoPago.equalsIgnoreCase("EFECTIVO") && !nuevoMetodoPago.equalsIgnoreCase("TARJETA")) {
			return ResponseEntity.badRequest().body("Método de pago no válido: " + nuevoMetodoPago);
		}

		MetodoPago metodoPago = MetodoPago.valueOf(nuevoMetodoPago.toUpperCase());

		Pago pagoActualizado = this.pagoService.actualizarPagoYCambiarEstadoCita(pago, metodoPago);

		return ResponseEntity.ok(pagoActualizado);
	}
	
	// Obtener pagos de un usuario específico
    @GetMapping("/usuario/{idUsuario}/pays")
    public ResponseEntity<List<Pago>> obtenerPagosPorUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorUsuario(idUsuario));
    }

}
