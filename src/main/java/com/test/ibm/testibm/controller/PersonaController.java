package com.test.ibm.testibm.controller;


import com.test.ibm.testibm.entity.Persona;
import com.test.ibm.testibm.exceptions.ApiException;
import com.test.ibm.testibm.exceptions.BussinessFunctionalException;
import com.test.ibm.testibm.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class PersonaController {

    @Autowired
    PersonaService personaService;

    private static final String INPUT_ERROR = "Input Error";

    @PostMapping("/personas")
    public ResponseEntity<?> persistirPersona(@RequestBody Persona persona){
        if(!("Masculino".equals(persona.getSexo()) || "Femenino".equals(persona.getSexo()))){
            throw new BussinessFunctionalException(new ApiException(INPUT_ERROR,"/personas"));
        }
        long id = personaService.persistPersona(persona);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/personas/" + id)
                .build();
    }

    @GetMapping("/personas")
    public ResponseEntity<?> obtenerPersonas(){
        List<Persona> personas = personaService.getPersonas();
        if(personas.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(personas);
    }

    @PutMapping("/personas/{id}")
    public ResponseEntity<?> actualizarPersona(@PathVariable long id, @RequestBody Persona persona){
        if(!("Masculino".equals(persona.getSexo()) || "Femenino".equals(persona.getSexo()))){
            throw new BussinessFunctionalException(new ApiException(INPUT_ERROR,"/personas"));
        }
        Persona person = personaService.actualizarPersona(id, persona);
        return ResponseEntity.status(HttpStatus.OK)
                .body(person);
    }

    @DeleteMapping("/personas/{id}")
    public ResponseEntity<?> borrarPersona(@PathVariable long id){
        Persona persona = personaService.borrarPersona(id);
        if(persona==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(persona);
    }
}
