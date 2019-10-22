package com.example.demo;


import com.experiment.application.ConsumeRestApiApplication;
import com.experiment.model.APIResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Trying to test end to end using wiremock
 * Wire mock used to stub consumer API response
 * Mockito used to mock standalone java class
 * to-do
 * @author Sagarika.padhy478@gmail.com
 */

@TestPropertySource("/application-test.properties")
public class IntegrationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationTest.class);
    @Autowired
    private WireMockServer wireMockServer;
    private final String MOCK_URL_PATH = "/api/real-time/";
    //private ConsumeRestApiApplication consumeRestApiApplication;

    @Mock
    ConsumeRestApiApplication consumeRestApiApplication;
    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }


    //@Test
    public void integrationTest() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        stubResponse(MOCK_URL_PATH, "api_success_message.json");
        //Mockito.when(consumeRestApiApplication.runScheduledJob()).thenReturn()
        consumeRestApiApplication = new ConsumeRestApiApplication();
        List<APIResponse> apiResponses = new ArrayList<>();
        apiResponses = consumeRestApiApplication.callConsumerAPI();
        wireMockServer.stop();

    }

    private void stubResponse(String path, String responseFile) {
        if (this.wireMockServer == null) {
            throw new RuntimeException("Please start mock server");
        } else {
            MappingBuilder mappingBuilder = WireMock.any(WireMock.urlMatching(path + ".*")).willReturn(WireMock.aResponse().withStatus(200)
                    .withBody(responseFile == null ? "" : this.readLocalFile(responseFile)));
            this.wireMockServer.stubFor(mappingBuilder);
        }
    }

    private String readLocalFile(String responseFile) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(responseFile);
        if (inputStream == null) {
            Assertions.fail("file missing " + responseFile);
        }
        Scanner scanner = new Scanner(inputStream);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
        }
        scanner.close();
        return sb.toString();
    }
    @Autowired
    ApplicationContext applicationContext;


}
