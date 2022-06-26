# Loyalty card wallet API

## Table of contents

* [General info](#general-info)
* [Status](#status)
* [Technologies](#technologies)
* [How to run](#setup)
* [Requirements](#requirements)


## General info


## Status

## Technologies

Project is created with:

* Java 17
* Spring Boot version 2.7.1
* Swagger
* Postgresql
* Liquibase
* Docker
* Lombok
* MapStruct


## How to run

To get this project up and running, navigate to root directory of an application and execute following commands:

* Create a jar file.
```
$ ./gradlew clean build
```
* Then build docker image using already built jar file.
```
$ docker-compose build
```

* Run whole setup using docker compose.
```
$ docker-compose up
```
