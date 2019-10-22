package com.example.demo;

import com.experiment.application.ConsumeRestApiApplication;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

/**
 * standalone method test
 * trying to inject mock usimg mockito
 * to-do
 *
 * @author Sagarika.padhy478@gmail.com
 */

class ConsumeRestApiApplicationTests {

    //private ConsumeRestApiApplication consumeRestApiApplication;
    @Mock
    ConsumeRestApiApplication consumeRestApiApplication;
    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void testDecryption() {
        String decryptedText = "OeAFFmMliFG5orCUuwAKQ8l4WWFQ67YX";
		consumeRestApiApplication= new ConsumeRestApiApplication();
        String actualText =  consumeRestApiApplication.decryptEncoded("T2VBRkZtTWxpRkc1b3JDVXV3QUtROGw0V1dGUTY3WVg=");
        assertEquals(actualText, decryptedText);
    }
   // @Test
	public void testMockBuildUrl(){
    	consumeRestApiApplication = new ConsumeRestApiApplication();
    	ConsumeRestApiApplication spyConsumerAPPObj= Mockito.spy(consumeRestApiApplication);
    	Mockito.when(spyConsumerAPPObj.buildAPIUrl("EUR")).thenReturn("https://eodhistoricaldata.com/api/real-time/EUR.FOREX?api_token=OeAFFmMliFG5orCUuwAKQ8l4WWFQ67YX&fmt=json");
        String expectedResult="https://eodhistoricaldata.com/api/real-time/EUR.FOREX?api_token=OeAFFmMliFG5orCUuwAKQ8l4WWFQ67YX&fmt=json";
        String actualResult=consumeRestApiApplication.buildAPIUrl("EUR");
        assertEquals(actualResult,expectedResult);
	}

}
