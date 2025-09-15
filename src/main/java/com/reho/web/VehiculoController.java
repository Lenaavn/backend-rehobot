package com.reho.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reho.persistence.entities.Vehiculo;
import com.reho.service.UsuarioService;
import com.reho.service.VehiculoService;
import com.reho.service.dto.VehiculoDTO;
import com.reho.service.mapper.VehiculoMapper;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

	@Autowired
	private VehiculoService vehiculoService;

	@Autowired
	private VehiculoMapper vehiculoMapper;
	
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<List<VehiculoDTO>> list() {
		List<VehiculoDTO> vehiculos = this.vehiculoService.findAll().stream().map(vehiculoMapper::toDTO).collect(Collectors.toList());

		return ResponseEntity.ok(vehiculos);
	}

	@GetMapping("/{idVehiculo}")
	public ResponseEntity<VehiculoDTO> findById(@PathVariable int idVehiculo) {
		if (this.vehiculoService.existsVehiculo(idVehiculo)) {
			return ResponseEntity.ok(vehiculoMapper.toDTO(this.vehiculoService.findById(idVehiculo).get()));
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	// ResponseEntity<?> para permitir diferentes tipos de respuesta
	public ResponseEntity<?> create(@RequestBody VehiculoDTO vehiculoDTO) {

	    if (vehiculoDTO.getIdUsuario() == null) {
	        return ResponseEntity.badRequest().body("El campo 'idUsuario' no puede ser nulo.");
	    }

	    if (!usuarioService.existsUsuario(vehiculoDTO.getIdUsuario())) {
	        return ResponseEntity.badRequest().body("El usuario especificado no existe.");
	    }
	    
	    // Validar que la matrícula sea válida
	    try {
	        vehiculoDTO.setMatricula(vehiculoService.validateMatricula(vehiculoDTO.getMatricula()));
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage()); // Enviar mensaje de error si la matrícula es inválida
	    }

	    // Convertir la matrícula a mayúsculas
	    vehiculoDTO.setMatricula(vehiculoDTO.getMatricula().toUpperCase());

	    // Validar que la matrícula sea válida
	    String matriculaValida = vehiculoService.validateMatricula(vehiculoDTO.getMatricula());
	    if (matriculaValida == null) {
	        return ResponseEntity.badRequest().body("La matrícula no tiene un formato válido.");
	    }

	    // Validar si ya existe un vehículo con la misma matrícula
	    if (vehiculoService.existsByMatricula(vehiculoDTO.getMatricula())) {
	        return ResponseEntity.badRequest().body("Ya existe un vehículo con la misma matrícula.");
	    }

	    // Crear el vehículo
	    VehiculoDTO savedVehiculo = this.vehiculoService.create(vehiculoMapper.toEntity(vehiculoDTO));
	    return ResponseEntity.ok(savedVehiculo);
	}


	@PutMapping("/{idVehiculo}")
	// ResponseEntity<?> para permitir diferentes tipos de respuesta
	public ResponseEntity<?> update(@PathVariable int idVehiculo, @RequestBody Vehiculo vehiculo) {
		if (vehiculo.getId() == null || idVehiculo != vehiculo.getId()) {
			return ResponseEntity.badRequest().body("El ID de la URL no coincide con el ID del cuerpo del vehiculo.");
		}

		if (vehiculo.getIdUsuario() == null) {
			return ResponseEntity.badRequest().body("El campo 'idUsuario' no puede ser nulo.");
		}

		Vehiculo existingVehiculo = vehiculoService.findById(vehiculo.getId()).get();

		if (!vehiculo.getIdUsuario().equals(existingVehiculo.getIdUsuario())) {
			return ResponseEntity.badRequest().body("No se permite cambiar el 'idUsuario' del vehículo.");
		}

		if (!usuarioService.existsUsuario(vehiculo.getIdUsuario())) {
	        return ResponseEntity.badRequest().body("El usuario especificado no existe.");
	    }

		if (vehiculo.getMarca() == null) {
			return ResponseEntity.badRequest().body("El campo 'marca' no puede ser nulo.");
		}

		if (vehiculo.getModelo() == null) {
			return ResponseEntity.badRequest().body("El campo 'modelo' no puede ser nulo.");
		}

		if (vehiculo.getMatricula() == null) {
			return ResponseEntity.badRequest().body("El campo 'matricula' no puede ser nulo.");
		}

		if (vehiculo.getMatricula().length() > 8) {
			return ResponseEntity.badRequest().body("La matricula puede tener como máximo 8 caracteres");
		}

		// Verificar si la matrícula ha cambiado
	    if (!existingVehiculo.getMatricula().equals(vehiculo.getMatricula())) {
	        // Validar que no exista otro vehículo con la nueva matrícula
	        if (vehiculoService.existsByMatricula(vehiculo.getMatricula())) {
	            return ResponseEntity.badRequest().body("Ya existe un vehículo con la misma matrícula.");
	        }
	    }

		vehiculo.setMatricula(vehiculo.getMatricula().toUpperCase());

		String matriculaValida = vehiculoService.validateMatricula(vehiculo.getMatricula());
		if (matriculaValida == null) {
			return ResponseEntity.badRequest().body("La matrícula no tiene un formato válido.");
		}

		if (!vehiculoService.existsVehiculo(idVehiculo)) {
			return ResponseEntity.notFound().build();
		}

		VehiculoDTO updatedVehiculo = vehiculoService.save(vehiculo);

		return ResponseEntity.ok(updatedVehiculo);
	}

	@DeleteMapping("/{idVehiculo}")
	public ResponseEntity<Vehiculo> delete(@PathVariable int idVehiculo) {
		if (this.vehiculoService.delete(idVehiculo)) {
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}
	
	// Obtener vehículos de un usuario específico
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Vehiculo>> obtenerVehiculosPorUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(vehiculoService.obtenerVehiculosPorUsuario(idUsuario));
    }
    
    @PatchMapping("/{idVehiculo}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable int idVehiculo) {
        boolean exito = vehiculoService.desactivar(idVehiculo);
        return exito ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
