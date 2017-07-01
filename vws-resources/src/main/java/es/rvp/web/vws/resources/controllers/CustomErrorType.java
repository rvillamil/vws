package es.rvp.web.vws.resources.controllers;

/**
 * @author Rodrigo Villamil Pérez
 */
public class CustomErrorType {

    private final String errorMessage;

    public CustomErrorType(final String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}