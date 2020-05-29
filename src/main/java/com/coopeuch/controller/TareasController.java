package com.coopeuch.controller;


import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.coopeuch.model.Tarea;
import com.coopeuch.model.repository.TareaRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TareasController {
	
	private final TareaRepository tareaRepository;

	/**
	 * Obtenemos todos las tareas
	 * 
	 * @return 404 si no hay tareas, 200 y lista de tareas si hay una o más
	 */
	@GetMapping("/tarea")
	public ResponseEntity<?> obtenerTodas() {
		List<Tarea> result = tareaRepository.findAll();

		if (result.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(result);
		}

	}

	/**
	 * Obtenemos una Tarea en base a su ID
	 * 
	 * @param id
	 * @return 404 si no encuentra la tarea, 200 y la tarea si la encuentra
	 */
	@GetMapping("/tarea/{id}")
	public ResponseEntity<?> obtenerTarea(@PathVariable Long id) {
		Tarea result = tareaRepository.findById(id).orElse(null);
		if (result == null)
			return ResponseEntity.notFound().build();
		else
			return ResponseEntity.ok(result);
	}

	/**
	 * Insertamos una nueva Tarea
	 * 
	 * @param nueva tarea
	 * @return 201 y la nueva tarea insertado
	 */
	@PostMapping("/tarea")
	public ResponseEntity<?> nuevaTarea(@RequestBody Tarea nuevaTarea) {
		
		if(nuevaTarea.getDescripcion().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}else {
			nuevaTarea.setFechaCracion(Date.from(Instant.now()));
			Tarea saved = tareaRepository.save(nuevaTarea);
			return ResponseEntity.status(HttpStatus.CREATED).body(saved);
		}
		
	}

	/**
	 * 
	 * @param editar
	 * @param id
	 * @return 200 Ok si la edición tiene éxito, 404 si no se encuentra la Tarea
	 */
	@PutMapping("/tarea/{id}")
	public ResponseEntity<?> editarTarea(@RequestBody Tarea editarTarea, @PathVariable Long id) {
		
		if (editarTarea.getDescripcion().isEmpty() || editarTarea.getVigente()==null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}else {
			return tareaRepository.findById(id).map(t -> {
				t.setDescripcion(editarTarea.getDescripcion());
				t.setVigente(editarTarea.getVigente());
				return ResponseEntity.ok(tareaRepository.save(t));
			}).orElseGet(() -> {
				return ResponseEntity.notFound().build();
			});	
		}
		
	}

	/**
	 * Borra una tarea del modelo en base a su id
	 * 
	 * @param id
	 * @return Código 204 sin contenido
	 */
	@DeleteMapping("/tarea/{id}")
	public ResponseEntity<?> borrarTarea(@PathVariable Long id) {
		tareaRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
