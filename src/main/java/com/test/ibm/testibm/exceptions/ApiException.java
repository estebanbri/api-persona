package com.test.ibm.testibm.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiException {

    private String mensaje;
    private String mensajeAlDesarrollador;
    private String detalle;

    ApiException(){

    }

    public ApiException(String mensaje, String mensajeAlDesarrollador) {
        this.mensaje = mensaje;
        this.mensajeAlDesarrollador = mensajeAlDesarrollador;
    }

    public ApiException(String mensaje, String mensajeAlDesarrollador, String detalle) {
        this.mensaje = mensaje;
        this.mensajeAlDesarrollador = mensajeAlDesarrollador;
        this.detalle = detalle;
    }

    public String getMensajeAlDesarrollador() {
        return mensajeAlDesarrollador;
    }

    public void setMensajeAlDesarrollador(String mensajeAlDesarrollador) {
        this.mensajeAlDesarrollador = mensajeAlDesarrollador;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "MiExepcion{" +
                "mensaje='" + mensaje + '\'' +
                ", mensajeAlDesarrollador='" + mensajeAlDesarrollador + '\'' +
                '}';
    }


}
