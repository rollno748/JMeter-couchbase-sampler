# JMeter Couchbase Sampler

## Introduction
This plugin adds feature to JMeter to load test couchbase (no sql database) using couchbase sdk client library.

## Jmeter Target
1. Jmeter version 5.5 or above
2. Java 8 or above

## Required Components

- Apache JMeter
- Couchbase connection string

## Jar Dependencies Required
* java-client-3.4.1 (lib/ext)
* core-io-2.4.1 (lib)
* reactor-core-3.5.0 (lib)
* reactive-streams-1.0.4 (lib)

## Installation Instructions

- Download the source code from the Gitlab.
- Just do a mvn clean install (Git bash is required)
- Jar will be generated under the target directory (jmeter-couchbase-sampler-x.jar).
- Copy the Jar to `<Jmeter Installed Directory>/lib/ext/`

## Usage Instructions
1. Add Config element -> Couchbase Config
2. Provide the connection string details (couchbase capella/on-premise hostname, credentials, bucket, scope, collection and other connection options required)
3. Provide a name in "**Bucket Object**" field to export the connecting object to sampler element.
4. To establish connection with multiple bucket, add more than one config element with different object name.
5. Add Sampler -> Couchbase sampler element in **Thread Group**
6. provide the Exported object name in the step 3 and select the query type (N1QL/Data operations)
7. provide scope/collection information - if required
8. Use the query field to perform the Data Operations/Query Operations using N1QL

## Data Operations
1. Data operations enables the simplest way to retrieve or mutate data where the key is known.
2. It covers CRUD operations, document expiration, and optimistic locking with CAS
3. The supported CRUD operations were INSERT, GET, UPSERT, REMOVE
4. Default timeout is set to 5 minutes for any CRUD operations
5. The parameter field is ignored when the query type is not N1QL

## SQL++ (Formerly N1QL) Operations
- The SQL++ mode is considered to write formal SQL queries on couchbase
- The query is always performed at the Cluster level, using the query method
- The scope and collection parameters were ignored when the query type is selected as N1QL
- Default query timeout is set to 5 minutes.


## References
Below are the references which guided to build this plugin.
- [x] https://docs.couchbase.com/java-sdk/current/hello-world/start-using-sdk.html


## 💲 Support Me
<!-- [<a href="https://www.buymeacoffee.com/rollno748" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" height="45px" width="162px" alt="Buy Me A Coffee"></a>](https://www.buymeacoffee.com/rollno748) -->
If this project help you reduce time to develop, you can give me a cup of coffee :) 

[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://ko-fi.com/rollno748)

Please rate a :star2: if you like it / benefits you.

Please open up a :beetle: - If you experienced something.


## Tools used
* IntelliJ IDEA
* Markdown editor online (https://dillinger.io/)
