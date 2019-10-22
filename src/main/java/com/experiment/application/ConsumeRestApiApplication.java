package com.experiment.application;

import com.experiment.errorHandling.CustomErrorHandler;
import com.experiment.model.APIResponse;
import com.experiment.model.Currency;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.*")
@EnableScheduling
public class ConsumeRestApiApplication {

    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private static ConfigurableApplicationContext ctx;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumeRestApiApplication.class);

    @Value("${encrypted.mail.password}")
    private String encryptedPassword;
    @Value("${encrypted.token}")
    private String encryptedAPIToken;
    @Value("${consumer.baseURL}")
    private String baseURL;
    @Value("#{'${forex.search}'.split(',')}")
    private String[] forexIds;
    @Value("${targetEmail}")
    private String targetEmail;

    public static void main(String[] args) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        ctx = SpringApplication.run(ConsumeRestApiApplication.class, args);


    }

    /**
     * CreateCSVFile is used to create a csv file obsval_yyyyMMdd_hhmm.csv format
     * write forex data into it
     * call send email method
     *
     * @author Sagarika.padhy478@gmail.com
     */

    private void createCsvFile(List<APIResponse> currencyList) throws
            IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, MessagingException, InterruptedException {
        String fileFormat = new SimpleDateFormat("yyyyMMdd_hhmm").format(new
                Date(System.currentTimeMillis()));
        String fullFilePath =
                "obsval_" + fileFormat + ".csv";

        Writer writer =
                Files.newBufferedWriter(Paths.get(fullFilePath)); //
        CSVWriter csvWriter = new CSVWriter(new FileWriter(fullFilePath));


        StatefulBeanToCsv<APIResponse> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
        beanToCsv.write(currencyList);
        LOGGER.info("file created successfully " + fullFilePath);
        writer.close();
        sendEmail(fullFilePath, "");


    }

    /**
     * sendEmail is used to instantiate java mail sender api object
     * get decrypted email password and send email with/without attachment to the sender
     * sender email is passed during app run time as a system variable -DtargetEmail=xyz@gmail.com
     *
     * @author Sagarika.padhy478@gmail.com
     */

    public synchronized void sendEmail(String fullFilePath, String Message)
            throws MessagingException, IOException, InterruptedException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        FileSystemResource file = new FileSystemResource(new File(fullFilePath));

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(targetEmail);
        if (fullFilePath != "") {
            helper.setText(
                    "Check attachment");
            helper.setSubject("Calibre Forex data received at " + new SimpleDateFormat("yyyyMMdd_hhmm").format(new
                    Date(System.currentTimeMillis())));
            helper.addAttachment(file.getFilename(), file);


        } else {
            helper.setText(
                    Message + "\n" + " WARNING- Application has been shut down. Please start again. ");
            helper.setSubject("Error in Forex data retrieval at " + new SimpleDateFormat("yyyyMMdd_hhmm").format(new
                    Date(System.currentTimeMillis())));
        }
        this.javaMailSender.send(mimeMessage);
        new File(fullFilePath).delete();
        LOGGER.info("Email sent with attachment");
        LOGGER.info(fullFilePath + " deleted");

    }

    /**
     * Call downstream API and deserialize the json response
     * Transform to required format
     *
     * @return APIResponse
     */

    public List<APIResponse> callConsumerAPI() {
        RestTemplate restTemplate = new RestTemplate();
        List<APIResponse> currencyList = new ArrayList<>();
        try {
            for (String forexId : forexIds) {
                String url = buildAPIUrl(forexId);
                Currency currency =
                        restTemplate.getForObject(url, Currency.class);
                APIResponse apiResponse = new APIResponse();
                apiResponse.setFOREX(currency.getCode().substring(0, currency.getCode().indexOf(".")));
                apiResponse.setVALUE(currency.getClose());
                currencyList.add(apiResponse);

            }
            return currencyList;

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new CustomErrorHandler(e.getMessage());

        }

    }


    /**
     * runScheduledJob is a cron job scheduled everyday at 8AM,12PM and 4PM
     * This method call downstream API to get real time forex data.
     * API token is a base64 encrypted token which is getting decrypted programmatically.
     * shutting down the application in case of error.
     * cron job detail from properties file.
     * @author Sagarika.padhy478@gmail.com
     */
    @Scheduled(cron = "${cron} ")
    public void runScheduledJob() {
        long startTime = System.currentTimeMillis();
        try {
            List<APIResponse> forexList;
            forexList = callConsumerAPI();
            createCsvFile(forexList);
            LOGGER.info("Total time taken " + (System.currentTimeMillis() - startTime));

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            try {
                sendEmail("", e.getMessage() + " IN " + e.getClass());
                ctx.close();
            } catch (Exception e1) {
                LOGGER.error(e1.getMessage());
                ctx.close();
            }

        }

    }

    /**
     * This is method run when app starts and push the encrypted mail server password
     * email password is a base64 encrypted token which is getting decrypted programmatically.
     *
     * @author Sagarika.padhy478@gmail.com
     */
    @PostConstruct
    public void init() {
        javaMailSender.setPassword(decryptEncoded(encryptedPassword));
    }

    /**
     * Decrypting base64 encrypted string
     *
     * @param encryptedPassword
     * @return decrypted string
     */

    public String decryptEncoded(String encryptedPassword) {
        byte[] byteArray = Base64.decodeBase64(encryptedPassword.getBytes());
        return new String(byteArray);

    }

    /**
     * @param forexId
     * @return formatted url
     */

    public String buildAPIUrl(String forexId) {
        System.out.println(baseURL + forexId + ".FOREX?api_token=" + decryptEncoded(encryptedAPIToken) + "&fmt=json");
        return baseURL + forexId + ".FOREX?api_token=" + decryptEncoded(encryptedAPIToken) + "&fmt=json";
    }

}


