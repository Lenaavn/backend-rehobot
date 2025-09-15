package com.reho.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reho.persistence.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{
	
	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByTelefono(String telefono);

	boolean existsById(Integer id);
	boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);
    List<Usuario> findByActivoTrue();
	
}
