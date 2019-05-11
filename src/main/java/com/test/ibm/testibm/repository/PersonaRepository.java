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

    public void borrarPersona(long id){
        Persona persona = em.find(Persona.class, id);
        em.remove(persona);
    }

    public Persona actualizarPersona(long id, Persona persona){
        persona.setId(id);
        em.merge(persona);
        return persona;
    }
}
