package com.coopeuch.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coopeuch.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

}
