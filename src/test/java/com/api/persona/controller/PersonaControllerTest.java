package com.api.persona.controller;

import com.api.persona.model.Persona;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.api.persona.exceptions.BussinessFunctionalException;
import com.api.persona.service.impl.PersonaServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonaController.class)
public class PersonaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonaServiceImpl service;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void givenEmptyList_whenGetPersonasEsInvocado_thenReturnStatusCode_204() throws Exception {

        List<Persona> personasMock = Collections.EMPTY_LIST;

        when(service.getPersonas()).thenReturn(personasMock);

        mockMvc.perform(get("/v1/personas"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenPersonasMocked_whenGetPersonasEsInvocado_thenReturnStatusCode_200_y_PersonasMockedEnJson() throws Exception {
        List<Persona> personasMock = Arrays.asList(
                new Persona("Esteban", "Briceño", 30, "Masculino")
        );

        when(service.getPersonas()).thenReturn(personasMock);

        mockMvc.perform(get("/v1/personas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json("[{\n" +
                        "\t\"nombre\": \"Esteban\",\n" +
                        "\t\"apellido\": \"Briceño\",\n" +
                        "\t\"edad\":30,\n" +
                        "\t\"sexo\":\"Masculino\"\n" +
                        "}]"));
    }

    @Test
    public void givenPersonasMocked_whenPersistirPersonasEsInvocado_thenReturnStatusCode_201() throws Exception {

        Persona personaMock = new Persona("Esteban", "Briceño", 30, "Masculino");

        when(service.persistPersona(personaMock)).thenReturn(personaMock);

        mockMvc.perform(post("/v1/personas")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(personaMock)))
                .andExpect(status().isCreated());
    }

    @Test(expected = BussinessFunctionalException.class)
    public void givenPersonasMockedErrorSintaxisSexo_whenPersistirPersonasEsInvocado_thenBussinessFunctionalExceptionEsEsperada() throws Exception {

        Persona personaMock = new Persona("Esteban", "Briceño", 30, "M");

        when(service.persistPersona(personaMock)).thenThrow(BussinessFunctionalException.class);

        service.persistPersona(personaMock);
    }


}