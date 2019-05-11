package com.test.ibm.testibm.exceptions;

public class BussinessFunctionalException extends RuntimeException {

    private static final String ERROR_NEGOCIO = "Error de Negocio";
    private ApiException apiException;

    public BussinessFunctionalException(ApiException apiException) {
       this.apiException = apiException;
    }

    public static String getErrorTecnico() {
        return ERROR_NEGOCIO;
    }

    public ApiException getApiException() {
        return apiException;
    }

    public void setApiException(ApiException apiException) {
        this.apiException = apiException;
    }
}
