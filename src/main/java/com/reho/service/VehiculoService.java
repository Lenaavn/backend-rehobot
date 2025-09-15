package com.reho.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reho.persistence.entities.Cita;
import com.reho.persistence.entities.Usuario;
import com.reho.persistence.entities.Vehiculo;
import com.reho.persistence.repository.CitaRepository;
import com.reho.persistence.repository.UsuarioRepository;
import com.reho.persistence.repository.VehiculoRepository;
import com.reho.service.dto.VehiculoDTO;
import com.reho.service.mapper.VehiculoMapper;

@Service
public class VehiculoService {

	@Autowired
	private VehiculoRepository vehiculoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CitaRepository citaRepository;

	@Autowired
	private VehiculoMapper vehiculoMapper;

	public List<Vehiculo> findAll() {
		return this.vehiculoRepository.findByActivoTrue();
	}

	public boolean existsVehiculo(int idVehiculo) {
		return this.vehiculoRepository.existsById(idVehiculo);
	}

	public Optional<Vehiculo> findById(int idVehiculo) {
		return this.vehiculoRepository.findById(idVehiculo);
	}

	public VehiculoDTO create(Vehiculo vehiculo) {
		// Validar que el usuario existe
		Usuario usuario = usuarioRepository.findById(vehiculo.getIdUsuario()).get();

		vehiculo.setUsuario(usuario);

		// Garantizar que la lista de citas no sea null
		if (vehiculo.getCitas() == null) {
			vehiculo.setCitas(new ArrayList<>());
		}

		Vehiculo savedVehiculo = vehiculoRepository.save(vehiculo);
		return vehiculoMapper.toDTO(savedVehiculo);
	}

	public VehiculoDTO save(Vehiculo vehiculo) {
		// Validar que el vehículo existe
		Vehiculo existingVehiculo = vehiculoRepository.findById(vehiculo.getId()).get();

		// Validar que el usuario existe
		Usuario usuario = usuarioRepository.findById(vehiculo.getIdUsuario()).get();
		
		// Asociar el usuario al vehículo
		existingVehiculo.setUsuario(usuario);

		// Actualizar propiedades del vehículo
		if (vehiculo.getIdUsuario() != null) {
			existingVehiculo.setIdUsuario(vehiculo.getIdUsuario());
		}

		if (vehiculo.getMarca() != null) {
			existingVehiculo.setMarca(vehiculo.getMarca());
		}

		if (vehiculo.getModelo() != null) {
			existingVehiculo.setModelo(vehiculo.getModelo());
		}

		if (vehiculo.getMatricula() != null) {
			existingVehiculo.setMatricula(vehiculo.getMatricula());
		}

		// Actualizar citas asociadas
		if (vehiculo.getCitas() != null && !vehiculo.getCitas().isEmpty()) {
			for (Cita nuevaCita : vehiculo.getCitas()) {
				if (nuevaCita.getId() != null) {
					// Actualizar citas existentes
					Cita citaExistente = citaRepository.findById(nuevaCita.getId()).orElseThrow(
							() -> new IllegalArgumentException("La cita con ID " + nuevaCita.getId() + " no existe."));
					citaExistente.setVehiculo(existingVehiculo); // Asegurar asociación
				}
			}
		}

		// Guardar el vehículo actualizado
		Vehiculo updatedVehiculo = vehiculoRepository.save(existingVehiculo);
		return vehiculoMapper.toDTO(updatedVehiculo);
	}

	public boolean delete(int idVehiculo) {
		boolean result = false;

		if (this.vehiculoRepository.existsById(idVehiculo)) {
			this.vehiculoRepository.deleteById(idVehiculo);
			result = true;
		}

		return result;
	}

	public boolean existsByMatricula(String matricula) {
		return vehiculoRepository.existsByMatricula(matricula);
	}

	/*
	 * El método .matches() comprueba si una cadena de texto (String) cumple con un
	 * patrón específico definido mediante una expresión regular (regex).
	 */

	public String validateMatricula(String matricula) {
	    if (matricula == null || matricula.isEmpty()) {
	        throw new IllegalArgumentException("La matrícula no puede estar vacía.");
	    }
	    
	    matricula = matricula.toUpperCase();

	    String patternActual = "^[0-9]{4}[A-Z]{3}$"; // Ejemplo: 1234ABC
	    String patternAntigua = "^[A-Z]{1,2}[0-9]{1,4}[A-Z]{1,2}$"; // Ejemplo: A123BC

	    if (matricula.matches(patternActual) || matricula.matches(patternAntigua)) {
	        return matricula;
	    }

	    throw new IllegalArgumentException("La matrícula '" + matricula + "' no tiene un formato válido.");
	}

	
	// Obtener los vehículos de un usuario específico
    public List<Vehiculo> obtenerVehiculosPorUsuario(int idUsuario) {
        return vehiculoRepository.findByIdUsuario(idUsuario);
    }
    
    public boolean desactivar(int id) {
		Optional<Vehiculo> vehi = vehiculoRepository.findById(id);
		if (vehi.isPresent()) {
			Vehiculo vehiculo = vehi.get();
			vehiculo.setActivo(false);
			vehiculoRepository.save(vehiculo);
			return true;
		}
		return false;
	}

}
