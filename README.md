# Partially Persistent List as Web Service with REST API

## About

The following implementation is represents an approach of **Partially Persistent List** (PPL) structure, that can be deployed as REST API web service.
[`Partially Persistent List`]( https://github.com/Voltorane/partially-persistent-list-rest/blob/main/src/main/java/com/example/partiallypersistentlistrest/PartiallyPersistentList.java) was introduced as a [POJO](https://www.javatpoint.com/pojo-in-java) being able to store Integers in and versions of itself. An instance of it would be then saved in the [`Partially Pesistent List Cache`](https://github.com/Voltorane/partially-persistent-list-rest/blob/main/src/main/java/com/example/partiallypersistentlistrest/PartiallyPersistentListCache.java), which would represent a controller between a Resource entry-point and PPL. [`Partially Persistent List Resource`](https://github.com/Voltorane/partially-persistent-list-rest/blob/main/src/main/java/com/example/partiallypersistentlistrest/PartiallyPersistentListResource.java) is an actual REST API resource that handles HTTP requests and generates appropriate response.

## Specification
As the web service represents the functionality only of Partially Persistent List, it's API was wrapped around with REST requests. It supports the following Requests: \

1) Get the available versions  
Request: *GET /lists*  
Response: *{ "versions": [int array of version ids] }*

2) Get the list elements  
Request: *GET /list/{id}*  
Response: *[int array of version element]*\
Codes: \
404 - Invalid version provided

3) Get element on specific index in list  
Request: *GET /list/{id}/{index}*  
Response: *int element*\
Codes: \
404 - Invalid version provided
406 - Index out of bounds for the list

4) Add a new element to the end of the list  
Request: *POST /list/{id}*   
Request body: *{ "newElement": <int\> }*  
Response: *{ "listVersion": <int>\ }*
Codes: \
400 - Request body is invalid
404 - Invalid version provided

5) Update an element’s value  
Request: *PUT /list/{id}*  
Request body: *{ "oldValue": <int/>, "newValue": <int/> }*  
Response: *{ "listVersion": <int/> }*
Codes: \
400 - Request body is invalid
404 - Invalid version provided
406 - oldValue is not in the list


6) Remove an element by value  
Request: *DELETE /list/{id}*  
Request body: *{ "oldElement": <int\> }*  
Response: *{ "listVersion": <int\> }* \
**Attention, minght not work wiht some Application servers!**
Codes: \
400 - Either application server just doesn't support DELETE requests with body **OR** request body is invalid (check by the response body message)
404 - Invalid version provided
406 - oldValue is not in the list

7) Remove an element by value  
Request: *DELETE /list/{id}/{index}* 
Response: *{ "listVersion": <int\> }* \
Codes:
404 - Invalid version provided
406 - Index out of bounds for the list

## Deployment
For this web service the *Jakarta EE* was used as REST API framework for Java and *GlassFish 7.0.2* in order to deploy the application server.