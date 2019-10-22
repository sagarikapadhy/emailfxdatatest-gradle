## Requirement

A Calibre client has asked for FX data to be sourced from:

_https://eodhistoricaldata.com/knowledgebase/list-supported-currencies/_

and delivered as an email file attachment at 8am, 12pm and 4pm each day.


The FX rates of interest are AUDUSD, AUDNZD, AUDHKD, AUDKRW and AUDJPY

The email file attachment will have:

a filename of obsval_YYYYMMDD_HHMM.csv e.g. obsval_20191015_0800.csv

a header row of FOREX, VALUE

a row for each FX pair e.g. AUDUSD, .65

Using the following tools, create a Java 8 program that delivers the above functionality:

**Spring Boot

JUnit

Logback 

Gradle

GitHub**  

Please note:

other additional tools or libraries can be used as part of the experiment

your approach to unit testing is of interest 

you should not have hardcoded API tokens and email addresses in your program

unrecoverable errors should send an email with exception details 

## How to run the jar file
**java -jar emailfxdatatest-1.0.0-SNAPSHOT.jar -DtargetEmail={email id where email will be delivered}**
example:- java -jar emailfxdatatest-1.0.0-SNAPSHOT.jar -DtargetEmail=xyz@gmail.com

## approach (gradle)
This is a gradle project.
writing csv file in classpath and deleting after email sent. Can stream the content. 