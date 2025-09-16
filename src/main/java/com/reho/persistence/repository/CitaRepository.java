package com.reho.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reho.persistence.entities.Cita;

public interface CitaRepository extends JpaRepository<Cita,Integer>{
	
	 List<Cita> findByVehiculo_IdUsuario(int idUsuario);
	 List<Cita> findByServicio_IdAndFecha(int idServicio, LocalDate fecha);
	 List<Cita> findByFecha(LocalDate fecha);
	 List<Cita> findByActivoTrue();
	 
	 @Query("SELECT c FROM Cita c WHERE c.vehiculo.idUsuario = :idUsuario")
	 List<Cita> findByUsuarioId(@Param("idUsuario") int idUsuario);



}
