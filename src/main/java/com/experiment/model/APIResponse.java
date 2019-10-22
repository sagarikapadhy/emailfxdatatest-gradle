package com.experiment.model;


import java.io.Serializable;

public class APIResponse implements Serializable {
    private String FOREX;
    private String VALUE;
    private String errorMessage;

    public String getFOREX() {
        return FOREX;
    }

    public void setFOREX(String FOREX) {
        this.FOREX = FOREX;
    }

    public String getVALUE() {
        return VALUE;
    }

    public void setVALUE(String VALUE) {
        this.VALUE = VALUE;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public APIResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public APIResponse(String FOREX, String VALUE, String errorMessage) {
        this.FOREX = FOREX;
        this.VALUE = VALUE;
        this.errorMessage = errorMessage;
    }
    public APIResponse(){}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("errorMessage [errorMessage=");
        builder.append(errorMessage);
        builder.append("]");
        return builder.toString();
    }
}
