package com.test.ibm.testibm.repository;

import com.test.ibm.testibm.entity.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class PersonaRepository {

    @Autowired
    EntityManager em;

    public List<Persona> getPersonas(){
       return em.createQuery("FROM Persona").getResultList();
    }

    public long persistPersona(Persona persona){
        em.persist(persona);
        return persona.getId();
    }

    public Persona borrarPersona(long id){
        Persona persona = em.find(Persona.class, id);
        if(persona!=null){
            em.remove(persona);
        }
        return persona;
    }

    public Persona actualizarPersona(long id, Persona persona){
        persona.setId(id);
        Persona person = em.merge(persona);
        return person;
    }
}
