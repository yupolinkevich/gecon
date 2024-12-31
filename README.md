# GeCon 

## Overview

This application facilitates the generation of homogeneous content (e.g., citations, places to visit, wishes, recipes, etc.) using AI, and it's further processing, such as storing in a database. 

## Core Features

- **AI-Powered Content Generation**: Generates homogeneous content via Spring AI + OpenAI API.
- **Asynchronous Processing**: Handles content generation and processing asynchronously
- **Ensuring content uniqueness** with each next call to AI by
  - content deduplication based on checksums 
  - a bit of randomization to prompt 



## Technologies Used

- **Java 17** 
- **Maven**
- **Spring Boot 3.4.x**
- **OpenAI API**
  - gpt-4o-mini 
- **MongoDB Atlas**



## Modules

### 1. generator
This module provides the core functionality, e.g:
- **`AsyncContentPopulator`**: Handles asynchronous content generation and processing.
- **`ChecksumInterceptor`**: An aspect that injects checksum values into generated content objects to allow deduplication based on checksums.
- ......

### 2. demo
This module demonstrates the usage of generator on such domains:
- **Citations**: AI-generated quotes for the provided topic.
- **Tourist Spots**: AI-generated lists of places to visit for the provided city.


## Prerequisites

- Java 17 and above installed.
- MongoDB Atlas account and database configured.
- OpenAI API key.

