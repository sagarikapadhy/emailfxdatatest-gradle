package com.experiment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency implements Serializable {

    private String code;
    private String close;

    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public String getClose() {
        return close;
    }


    public void setClose(String close) {
        this.close = close;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Currency [code=");
        builder.append(code);
        builder.append(", close=");
        builder.append(close);
        builder.append("]");
        return builder.toString();
    }


}
