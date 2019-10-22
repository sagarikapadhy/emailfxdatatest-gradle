package com.experiment.errorHandling;

import com.experiment.application.ConsumeRestApiApplication;
import com.experiment.model.APIResponse;

public class CustomErrorHandler extends RuntimeException {

    public CustomErrorHandler(String errorMessage) {
        super(errorMessage);
        ConsumeRestApiApplication consumeRestApiApplication =new ConsumeRestApiApplication();
        APIResponse apiResponse =new APIResponse();
        apiResponse.setErrorMessage(errorMessage);
       // consumeRestApiApplication.sendEmailWithAttachments("",new JSONObject((Map) apiResponse);

    }

    public CustomErrorHandler() {
    }
}
