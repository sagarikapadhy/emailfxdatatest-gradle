package com.experiment.model;


import java.io.Serializable;

public class APIResponse implements Serializable {
    private String FOREX;
    private String VALUE;

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


    public APIResponse(String FOREX, String VALUE, String errorMessage) {
        this.FOREX = FOREX;
        this.VALUE = VALUE;

    }

    public APIResponse() {
    }


}
