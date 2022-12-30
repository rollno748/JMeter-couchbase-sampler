# Jmeter Couchbase Sampler 

## Introduction
This plugin adds feature to Jmeter to load test couchbase no sql database using couchbase sdk client library.

## Jmeter Target
1. Jmeter version 5.5 or above
2. Java 8 or above

## Required Components

- Apache JMeter
- Couchbase connection string

## Jar Dependencies Required
* java-client-3.4.1

## Installation Instructions

- Download the source code from the Gitlab.
- Just do a mvn clean install (Git bash is required)
- Jar will be generated under the target directory (jmeter-couchbase-sampler-x.jar).
- Copy the Jar to `<Jmeter Installed Directory>/lib/ext/`

## Usage Instructions
1. Add Config element -> Couchbase Config 
2. Provide the connection string details (couchbase hostname, credentials, bucket and other connection options required)
3. Provide a name in "**Bucket Object**" field to export the connecting object to sampler element. 
4. To establish connection with multiple bucket, add more than one config element with different object name.
5. Add Sampler -> Couchbase sampler element in **Thread Group**
6. provide the Exported object name in the step 3 and select the query type
7. provide scope/collection information - if required
8. Use the query field to perform the Data Operations/Query Operations using N1QL

## References
Below are the references which guided to build this plugin.
- [x] https://docs.couchbase.com/java-sdk/current/hello-world/start-using-sdk.html


## 💲 Donate
<a href="https://www.buymeacoffee.com/rollno748" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-green.png" alt="Buy Me A Coffee" style="max-width:20%;" ></a> 

Please rate a :star2: if you like it.

Please open up a :beetle: - If you experienced something.


## Tools used 
* IntelliJ IDEA
* Markdown editor online (https://dillinger.io/)


