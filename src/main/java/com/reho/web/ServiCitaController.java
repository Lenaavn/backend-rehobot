package com.reho.web;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.reho.persistence.entities.Cita;
import com.reho.persistence.entities.ServiCita;
import com.reho.persistence.entities.Servicio;
import com.reho.persistence.repository.CitaRepository;
import com.reho.persistence.repository.ServicioRepository;
import com.reho.service.ServiCitaService;
import com.reho.service.dto.ServiCitaEntityDTO;

@RestController
@RequestMapping("/servicitas")
public class ServiCitaController {

    @Autowired
    private ServiCitaService serviCitaService;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping
    public ResponseEntity<List<ServiCitaEntityDTO>> list() {
        return ResponseEntity.ok(this.serviCitaService.findAll());
    }

    @GetMapping("/{idServiCita}")
    public ResponseEntity<ServiCitaEntityDTO> findById(@PathVariable int idServiCita) {
        Optional<ServiCitaEntityDTO> serviCitaEntityDTO = serviCitaService.findById(idServiCita);

        if (serviCitaEntityDTO.isPresent()) {
            return ResponseEntity.ok(serviCitaEntityDTO.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ServiCitaEntityDTO dto) {
        if (dto.getIdCita() <= 0) {
            return ResponseEntity.badRequest()
                    .body("El atributo 'idCita' no puede estar vacío ni ser nulo para crear el ServiCita.");
        }

        if (!citaRepository.existsById(dto.getIdCita())) {
            return ResponseEntity.badRequest()
                    .body("La cita con ID " + dto.getIdCita() + " no existe. Por favor, proporcione una cita válida.");
        }

        if (dto.getValoracion() == null) {
            return ResponseEntity.badRequest()
                    .body("El atributo 'valoracion' no puede estar vacío ni ser nulo para crear el ServiCita.");
        }

        List<Double> valoresPermitidos = Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0);
        if (!valoresPermitidos.contains(dto.getValoracion())) {
            return ResponseEntity.badRequest().body(
                    "El atributo 'valoracion' debe ser uno de los siguientes valores: 1.0, 1.5, 2.0, 2.5, ..., 5.0.");
        }

        if (dto.getComentario() == null || dto.getComentario().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El atributo 'comentario' no puede estar vacío ni ser nulo para crear el ServiCita.");
        }

        ServiCitaEntityDTO responseDTO = serviCitaService.create(dto);

        Servicio servicio = servicioRepository.findById(dto.getIdServicio())
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado con ID: " + dto.getIdServicio()));

        responseDTO.setServicio(servicio.getNombre().toString());

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{idServiCita}")
    public ResponseEntity<?> update(@PathVariable int idServiCita, @RequestBody ServiCitaEntityDTO dto) {
        if (idServiCita != dto.getId()) {
            return ResponseEntity.badRequest()
                    .body("El ID de la URL no coincide con el ID del cuerpo del ServiCita.");
        }

        if (!serviCitaService.existServiCita(idServiCita)) {
            return ResponseEntity.notFound().build();
        }

        if (dto.getIdCita() <= 0) {
            return ResponseEntity.badRequest()
                    .body("El atributo 'idCita' no puede estar vacío ni ser nulo para actualizar el ServiCita.");
        }

        if (!citaRepository.existsById(dto.getIdCita())) {
            return ResponseEntity.badRequest()
                    .body("La cita con ID " + dto.getIdCita() + " no existe. Por favor, proporcione una cita válida.");
        }

        if (dto.getComentario() == null || dto.getComentario().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("El atributo 'comentario' no puede estar vacío ni ser nulo para actualizar el ServiCita.");
        }

        List<Double> valoresPermitidos = Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0);
        if (!valoresPermitidos.contains(dto.getValoracion())) {
            return ResponseEntity.badRequest().body(
                    "El atributo 'valoracion' debe ser uno de los siguientes valores: 1.0, 1.5, 2.0, 2.5, ..., 5.0.");
        }

        ServiCitaEntityDTO updatedDTO = serviCitaService.save(dto);

        Cita cita = citaRepository.findById(dto.getIdCita())
                .orElseThrow(() -> new IllegalArgumentException("La cita con ID no es válida: " + dto.getIdCita()));

        updatedDTO.setServicio(cita.getServicio().getNombre().toString());

        updatedDTO.setNombreUsuario(cita.getVehiculo().getUsuario().getNombre());

        return ResponseEntity.ok(updatedDTO);
    }


    @DeleteMapping("/{idServiCita}")
    public ResponseEntity<ServiCita> delete(@PathVariable int idServiCita) {
        if (this.serviCitaService.delete(idServiCita)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/top5")
    public ResponseEntity<List<ServiCitaEntityDTO>> getTopFiveValoraciones() {
        return ResponseEntity.ok(this.serviCitaService.findTopFiveValoraciones());
    }
    
    // Obtener valoraciones de un usuario específico
    @GetMapping("/usuario/{idUsuario}/valoracion")
    public ResponseEntity<List<ServiCitaEntityDTO>> obtenerPagosPorUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(serviCitaService.obtenerPagosPorUsuario(idUsuario));
    }
    
    @PatchMapping("/{idServiCita}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable int idServiCita) {
        boolean exito = serviCitaService.desactivar(idServiCita);
        return exito ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    
}


