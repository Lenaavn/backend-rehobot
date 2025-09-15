package com.reho.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reho.persistence.entities.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo,Integer>{
	
	List<Vehiculo> findByIdUsuario(int idUsuario);
	
	boolean existsByMatricula(String matricula);
	List<Vehiculo> findByActivoTrue();
	
}
