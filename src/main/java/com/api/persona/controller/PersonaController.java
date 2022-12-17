package com.api.persona.controller;


import com.api.persona.model.Persona;
import com.api.persona.service.impl.PersonaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/personas")
public class PersonaController {

    @Autowired
    PersonaServiceImpl personaServiceImpl;

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPersonasPorId(@PathVariable Long id){
        Persona persona = personaServiceImpl.getPersonaById(id);
        if(persona == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(persona);
    }

    @PostMapping
    public ResponseEntity<?> persistirPersona(@RequestBody Persona persona){
        Persona p = personaServiceImpl.persistPersona(persona);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .buildAndExpand("/{id}",p.getId()).toUri())
                .build();
    }

    @GetMapping
    public ResponseEntity<?> obtenerPersonas(){
        List<Persona> personas = personaServiceImpl.getPersonas();
        if(personas.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(personas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPersona(@PathVariable long id, @RequestBody Persona persona){
        Persona person = personaServiceImpl.actualizarPersona(id, persona);
        return ResponseEntity.status(HttpStatus.OK)
                .body(person);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcialPersona(@PathVariable long id, @RequestBody Map<Object, Object> fields) {
        return ResponseEntity.ok(personaServiceImpl.actualizarParcialPersona(id, fields));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> borrarPersona(@PathVariable long id){
        personaServiceImpl.borrarPersona(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

