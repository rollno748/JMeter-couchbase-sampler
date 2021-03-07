# Jmeter Couchbase Sampler 

## Introduction
This plugin adds feature to Jmeter to load test couchbase no sql database using couchbase sdk client library.

## Jmeter Target
1. Jmeter version 5.1.1 or above
2. Java 8 or above

## Required Components

- DI-Jmeter
- GCP Pubsub configs

## Jar Dependencies Required
* java-client-3.0.5

## Installation Instructions

- Download the source code from the Gitlab.
- Just do a mvn clean install (Git bash is required)
- Jar will be generated under the target directory (jmeter-couchbase-sampler-1.0.jar).
- Copy the Jar to `<Jmeter Installed Directory>/lib/ext/`

## Usage Instructions
1. Add Bucket config element
2. Provide the required informations to connect to couchbase bucket. such as bucket name, credentials, etc.
3. It is possible to add more than one bucket on couchbase config and use teh required bucket to process operations

## References
Below are the references which guided to build this plugin.
- [x] https://docs.couchbase.com/java-sdk/current/hello-world/start-using-sdk.html


## 💲 Donate
<a href="https://www.buymeacoffee.com/rollno748" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-green.png" alt="Buy Me A Coffee" style="max-width:20%;" ></a> 

Please rate a :star2: if you like it.

Please open up a :beetle: - If you experienced something.


## Tools used 
* eclipse
* Markdown editor online (https://dillinger.io/)


