package com.reho.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reho.persistence.entities.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio,Integer>{
	
	List<Servicio> findByActivoTrue();

}
