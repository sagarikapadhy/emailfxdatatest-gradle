## Requirement

A client has asked for FX data to be sourced from:

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

## How to run the jar in local machine
** ./gradlew build && java -DtargetEmail=sagarika.padhy@gmail.com -jar build/libs/emailfxdata-gradle-1.0.0-SNAPSHOT.jar
**

## Sequence Diagram
![alt text](https://github.com/sagarikapadhy/emailfxdatatest-gradle/blob/master/src/main/resources/sequence_diagram.png)



## How to run the jar file
** java -DtargetEmail={email id where email will be delivered} -jar emailfxdata-gradle-1.0.0-SNAPSHOT.jar **

`example:- java -DtargetEmail=sagarika.padhy@gmail.com -jar emailfxdata-gradle-1.0.0-SNAPSHOT.jar `

## How to deploy this app as a container in cloud or on-prem kubernetes cluster
** 


Step 1:- Build the docker image
---     "docker build -t <reponame>/spring-boot-app:1.0.0 ."
  
  
  
Step 2:- Push the image to docker hub
---     "docker push <reponame>/spring-boot-app:1.0.0"
  
  
  
Step 3:- Create your kubernetes cluster either locally or in public cloud like AWS/GCP/Azure



Step 4:- Check if nodes are up and running
---      "kubectl get nodes"


Step 5:- Deploy in kuberenetes cluster using the deploymemt yml file
---      "kubectl apply -f web-deploy.yml"


Step 6:- watch the deployment
---      "kubectl get deploy --watch"


Step 7:- expose the service through a node port 
---      "kubectl apply -f web-nodeport.yml"


Step 8:- expose it through a load balancer
---      "kubectl apply -f web-lb.yml"


Step 9:- check the service through CLI
---      "kubectl get svc web-nodeport"


**


## approach (gradle)
This is a gradle project.
writing csv file in classpath and deleting after email sent.


