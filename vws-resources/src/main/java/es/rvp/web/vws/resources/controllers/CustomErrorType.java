/*
 *
 */
package es.rvp.web.vws.resources.controllers;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomErrorType.
 *
 * @author Rodrigo Villamil Pérez
 */
public class CustomErrorType {

    /** The error message. */
    private final String errorMessage;

    /**
     * Instantiates a new custom error type.
     *
     * @param errorMessage the error message
     */
    public CustomErrorType(final String errorMessage){
        this.errorMessage = errorMessage;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
