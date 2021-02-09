# Accounting System Service

This application stores data about customers and their orders and send the data needed to create invoices to the client in the JSON format. The application is designed like an REST server which reads and saves data to a database. The application uses Spring Boot to create REST server and managing the database. The database used is Amazon DynamoDB NoSQL database.

## Database model

Amazon recommends use of Single-Table Design for DynamoDB because of performance reasons. So the application database includes only one table - CustomerOrder - where the primary key is composed of the partition key - CustomerId - and sort key - KeyType - which includes type of the record.

The table structure with data examples looks like this:

<table>

<thead>

<tr>

<th colspan="2">Primary key</th>

<th th="" colspan="3">Attributes - "(column name) example value"</th>

</tr>

<tr>

<th>Partition key</th>

<th>Sort key</th>

<th></th>

<th></th>

<th></th>

</tr>

</thead>

<tbody>

<tr>

<td>customer1</td>

<td>CUSTOMER_DATA#customer1</td>

<td>(name) Test customer</td>

<td>(address) Široká 15</td>

<td></td>

</tr>

<tr>

<td>customer2</td>

<td>CUSTOMER_DATA#customer2</td>

<td>(name )Test customer 2</td>

<td>(address) Hladká 78</td>

<td></td>

</tr>

<tr>

<td>customer2</td>

<td>ORDER_DATA#ref-20213658</td>

<td>(orderDate) Thu Feb 04 12:18:18 CET 2021</td>

<td>(orderCurrency) 203</td>

<td>(orderReferenceNumber) ref-20213658</td>

</tr>

<tr>

<td>customer2</td>

<td>ORDER_DATA#ref-2054558</td>

<td>(orderDate) Thu Feb 04 12:18:20 CET 2021</td>

<td>(orderCurrency) 203</td>

<td>(orderReferenceNumber) ref-2054558</td>

</tr>

<tr>

<td>customer2</td>

<td>ORDER_ITEM#ref-2054558#454566-89</td>

<td>(productCode) 454566-89</td>

<td>(productQuantity) 4</td>

<td>(productUnitPrice) 4100</td>

</tr>

<tr>

<td>customer2</td>

<td>ORDER_ITEM#ref-2054558#454566-8999</td>

<td>(productCode) 45556-89</td>

<td>(productQuantity) 1</td>

<td>(productUnitPrice) 100</td>

</tr>

</tbody>

</table>

Record of type CUSTOMER_DATA includes a name and an address of a customer.  
Record of type ORDER_DATA includes data about an order - date, currency, reference number (ID).  
Record of type ORDER_ITEM includes products from the order - product code (ID), product quantity (how many was bought) and product unit price (price for one piece for which the product was bought in the smallest currency unit as integer)

Local DynamoDB database server is included in the project. It is started on the application start and test data are inserted (AccountingSystemTestDBSetup class).

## REST interface

A client can call the following REST endpoints:

<table>

<thead>

<tr>

<th>HTTP Method</th>

<th>Endpoint URL</th>

<th>Action</th>

<th>Request body (JSON)</th>

</tr>

</thead>

<tbody>

<tr>

<td>GET</td>

<td>http://localhost:8045/invoices/{customerId}/{refNum}</td>

<td>get data from an order for invoice by customer ID and order reference number</td>

<td></td>

</tr>

<tr>

<td>GET</td>

<td>http://localhost:8045/customers/</td>

<td>get all customers</td>

<td></td>

</tr>

<tr>

<td>GET</td>

<td>http://localhost:8045/customers/{id}</td>

<td>get a cutomer by ID</td>

<td></td>

</tr>

<tr>

<td>PUT</td>

<td>http://localhost:8045/customers/</td>

<td>save new customer</td>

<td>{"name": "Test customer 2","address": "Hladká 78, Olomouc"}</td>

</tr>

<tr>

<td>PUT</td>

<td>http://localhost:8045/customers/{id}</td>

<td>update a customer</td>

<td>{"id": "CUSTOMER2","name": "Test customer 2","address": "Hladká 78, Olomouc"}</td>

</tr>

<tr>

<td>DELETE</td>

<td>http://localhost:8045/customers/{id}</td>

<td>delete a customer</td>

<td></td>

</tr>

<tr>

<td>GET</td>

<td>http://localhost:8045/orders/{customerId}</td>

<td>get all customer's orders</td>

<td></td>

</tr>

<tr>

<td>GET</td>

<td>http://localhost:8045/orders/{customerId}/{refNum}</td>

<td>get an order by reference number</td>

<td></td>

</tr>

<tr>

<td>PUT</td>

<td>http://localhost:8045/orders/{customerId}</td>

<td>save an order to a customer</td>

<td>{"referenceNumber": "ref-2021365868","currency": "203","date": "2021-02-04T11:18:18.940+00:00"}</td>

</tr>

<tr>

<td>PUT</td>

<td>http://localhost:8045/orders/{customerId}/{refNum}</td>

<td>update an order of a customer</td>

<td>{"referenceNumber": "ref-2021365868","currency": "203","date": "2021-02-04T11:18:18.940+00:00"}</td>

</tr>

<tr>

<td>DELETE</td>

<td>http://localhost:8045/orders/{customerId}/{refNum}</td>

<td>delete an order</td>

<td></td>

</tr>

<tr>

<td>GET</td>

<td>http://localhost:8045/orderItems/{customerId}/{refNum}</td>

<td>get all product on a order</td>

<td></td>

</tr>

<tr>

<td>PUT</td>

<td>http://localhost:8045/orderItems/{customerId}/{refNum}</td>

<td>add a product to an order</td>

<td>{"productCode": "454566-89","productQuantity": 4,"productUnitPrice": 4100}</td>

</tr>

<tr>

<td>PUT</td>

<td>http://localhost:8045//orderItems/{customerId}/{refNum}/{productCode}"</td>

<td>update a product in an order</td>

<td>{"productCode": "454566-89","productQuantity": 4,"productUnitPrice": 4100}</td>

</tr>

<tr>

<td>DELETE</td>

<td>http://localhost:8045/orderItems/{customerId}/{refNum}/{productCode}</td>

<td>delete product</td>

<td></td>

</tr>

</tbody>

</table>

A response is sent in the JSON format. For example the response to "curl http://localhost:8045/invoices/customer2/ref-2054558" call looks like this:

{"customer":{"id":"customer2","name":"Test customer 2","address":"Hladká 78, Olomouc"},"order":{"referenceNumber":"ref-2054558","currency":"203","date":"2021-02-04T11:18:18.953+00:00"},"orderItems":[{"productCode":"454566-89","productQuantity":4,"productUnitPrice":4100},{"productCode":"454566-8999","productQuantity":1,"productUnitPrice":48800}]}

In better format:

<pre>{
  "customer": {
    "id": "customer2",
    "name": "Test customer 2",
    "address": "Hladká 78, Olomouc"
  },
  "order": {
    "referenceNumber": "ref-2054558",
    "currency": "203",
    "date": "2021-02-04T11:18:18.953+00:00"
  },
  "orderItems": [
    {
      "productCode": "454566-89",
      "productQuantity": 4,
      "productUnitPrice": 4100
    },
    {
      "productCode": "454566-8999",
      "productQuantity": 1,
      "productUnitPrice": 48800
    }
  ]
}</pre>

## Build

The project uses Maven build tool. It can be build by running "mvn package" (if the Maven is installed).

## Runnable jar

You can unpack zip with runnable version and run it from the command line:

java -jar AccountingSystem-1.0.jar

It is needed to use Java 13 version or higher. You can specify the location of Java with running the following command from the command line (change the path):

"C:\Program Files\Java\jdk-13.0.2\bin\java" -jar AccountingSystem-1.0.jar

You can send request to REST interface using any REST client (e.g. curl).

You can try:

curl http://localhost:8045/invoices/customer2/ref-2054558

Note: Spring plugin generates runnable jar with all dependent libraries during the build with "mvn package". But there are some native-libs (dll, so) for sqlite4java-1.0.392.jar library which is used by DynamoDB. I was not able to make the library load these native libraries from created jar file. So the native libraries are outside jar in the directory native-libs with library sqlite4java-1.0.392.jar in libs directory. And I added link to them in the jar MANIFEST.MF file ("Class-Path: libs/sqlite4java-1.0.392.jar").

## What is missing in the application

Better logging. More validation of input data.