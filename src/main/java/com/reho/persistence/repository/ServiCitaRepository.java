package com.reho.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reho.persistence.entities.ServiCita;

public interface ServiCitaRepository extends JpaRepository<ServiCita,Integer>{

	List<ServiCita> findTop5ByOrderByValoracionDesc();
	List<ServiCita> findByCitaVehiculoIdUsuario(int idUsuario);
	List<ServiCita> findByActivoTrue();
	List<ServiCita> findTop5ByActivoTrueOrderByValoracionDesc();
	
	List<ServiCita> findByIdCita(int idCita);




	
}
