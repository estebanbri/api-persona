package com.test.ibm.testibm.service;

import com.test.ibm.testibm.entity.Persona;
import com.test.ibm.testibm.exceptions.ApiException;
import com.test.ibm.testibm.exceptions.BussinessFunctionalException;
import com.test.ibm.testibm.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonaService {

    @Autowired
    PersonaRepository personaRepository;

    private static final String INPUT_ERROR = "Input Error";

    @Transactional
    public long persistPersona(Persona persona){
        if(!("Masculino".equals(persona.getSexo()) || "Femenino".equals(persona.getSexo()))){
            throw new BussinessFunctionalException(new ApiException(INPUT_ERROR,"/personas"));
        }
        long id = personaRepository.persistPersona(persona);
        return id;
    }

    public List<Persona> getPersonas(){
        return personaRepository.getPersonas();
    }

    @Transactional
    public Persona actualizarPersona(long id, Persona persona){
        if(!("Masculino".equals(persona.getSexo()) || "Femenino".equals(persona.getSexo()))){
            throw new BussinessFunctionalException(new ApiException(INPUT_ERROR,"/personas"));
        }
        return personaRepository.actualizarPersona(id, persona);
    }

    @Transactional
    public Persona borrarPersona(long id){
        return personaRepository.borrarPersona(id);
    }

}
