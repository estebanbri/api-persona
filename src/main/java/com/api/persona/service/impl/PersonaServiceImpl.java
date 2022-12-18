package com.api.persona.service.impl;

import com.api.persona.model.Persona;
import com.api.persona.repository.PersonaRepository;
import com.api.persona.service.PersonaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.api.persona.config.CacheConfigurationProperties.PERSONAS_CACHE;

@Service
public class PersonaServiceImpl implements PersonaService {

    private static final Logger logger = LoggerFactory.getLogger(PersonaServiceImpl.class);

    @Autowired
    private PersonaRepository personaRepository;

    @Cacheable(value = PERSONAS_CACHE) // Por defecto si tu metodo tiene unico parametro va a ser tu KEY y el valor es el objeto retornado del metodo, en caso de tener mas de dos parametros necesitas especificarle cual es la key por medio del atributo key="#id" (Soporta SPEL para navegar por un objeton en caso de un objeto complejo))
    public Persona getPersonaById(Long id){
        logger.debug("getPersonaById");
        return personaRepository.findById(id)
                .orElse(null);
    }

    public Persona persistPersona(Persona persona){
        logger.debug("persistPersona");
        return personaRepository.save(persona);
    }

    public List<Persona> getPersonas(){
        logger.debug("getPersonas");
        return personaRepository.findAll();
    }

    public Persona actualizarPersona(long id, Persona personaUpdate){
        logger.debug("actualizarPersona");
        final Optional<Persona> persona = personaRepository.findById(id);
        Persona p = null;
        if (persona.isPresent()) {
            p = persona.get();
            BeanUtils.copyProperties(personaUpdate, p);
            personaRepository.save(p);
        }
        return p;
    }


    // Lo mismo se puede hacer usando una libreria third party (Apache BeanUtils, Dozer, ObjectMapper) en vez de hacerlo directamente usando reflection
    // BeanUtils.copyProperties(fieldsMap, persona) o dozerBeanMapper.map(...) o objectMapper.convertValue(...)
    public Persona actualizarParcialPersona(long id, Map<Object, Object> fields) {
        logger.debug("actualizarParcialPersona");
        Persona p = null;
        Optional<Persona> persona = personaRepository.findById(id);
        if (persona.isPresent()) {
            // Recorro el mapa de fields expresados en clave-valor pasados en el body del request
            fields.forEach( (key,value) -> {
                // Por cada key que seria el field de Persona
                Field field = ReflectionUtils.findField(Persona.class, (String) key);
                if(field == null)  return;
                field.setAccessible(true);
                // Actualizo el field correspondiente a dicha key con el valor de la entry del Map
                ReflectionUtils.setField(field, persona.get(), value);
            });
            p = personaRepository.save(persona.get());
        }

        return p;
    }

    public void borrarPersona(long id){
        logger.debug("borrarPersona");
         personaRepository.deleteById(id);
    }



}
