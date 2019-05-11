package com.test.ibm.testibm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlers {

    private static final String INPUT_ERROR_SEXO_DETALLE = "Los valores posibles para el campo sexo son: 'Masculino' o 'Femenino'";

    @ExceptionHandler({BussinessFunctionalException.class})
    public ResponseEntity<?> errorTecnicoException(BussinessFunctionalException e){
        ApiException apiException = new ApiException();
        apiException.setMensajeAlDesarrollador(e.getApiException().getMensajeAlDesarrollador());
        apiException.setMensaje(e.getClass().getSimpleName());
        apiException.setDetalle(INPUT_ERROR_SEXO_DETALLE);

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> errorTecnicoException(ConstraintViolationException e){
        ApiException apiException = new ApiException();
        apiException.setMensaje(e.getClass().getSimpleName());
        apiException.setDetalle(e.getMessage());

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}
