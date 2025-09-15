package com.reho.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reho.persistence.entities.Pago;

public interface PagoRepository extends JpaRepository<Pago,Integer>{
	
	List<Pago> findByCitaVehiculoUsuarioId(int idUsuario);

}
