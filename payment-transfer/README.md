# Master Card Payments Coding Assignment

## Introduction
We are developing an Intra Payment solution for Modern Bank PLC. 
The application will be a simple HTTP-based REST API with basic 
account operations and payment transfer operation.

System should be:

• accessible by Restful Webservices

• able to tell account balance in real time

• able to get mini statement for last 20 transactions

• able to transfer money in real time

• able to fetch accounts details from accounts service (new / deleted)


##
This is a simple spring-boot application with basic auth spring security.
Using H2 database hence no need to install any database

Create accounts with POST create url(present in both swagger and postman collection) before proceed with other operations.

## Postman Collection
The complete API collection (MasteCard_Assignment.postman_collection.json) is added in the workspace which you can directly import and test our APIs

##Swagger
You can test the application using http://localhost:8080/payment-app/swagger-ui.html# post successful start of the server

