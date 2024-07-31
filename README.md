# Exchange Rate Application
This application provides exchange rate information and allows users to retrieve conversion history by transaction ID or date. The application is built using Spring Boot and can be run locally or using Docker.

## Prerequisites
- Java 17 or higher  
- Docker and Docker Compose  
- Gradle (if running locally without Docker)

## Running the Application

### Running Locally
Clone the repository:

```
git clone https://github.com/barrida/foreign-exchange-application.git
cd https://github.com/barrida/foreign-exchange-application.git
```

#### Build:

You can build the application using the Gradle wrapper included in the project.

On Unix-based systems:
```
./gradlew build
```

On Windows:

```
.\gradlew.bat build 
```

#### Run:

After building the application, you can run it using the following command:

```
./gradlew bootRun
```

#### Access the application:

Once the application is running, you can access the Swagger UI at http://localhost:8080/swagger-ui.html.

### Running with Docker

#### Build and run the application using Docker Compose:

Ensure Docker and Docker Compose are installed on your machine. Then, use the following command to build and start the application:

```
docker-compose up --build
```

#### Access the application:

Once the application is running, you can access the Swagger UI at http://localhost:8080/swagger-ui.html.

## Interact with the service

You can interact with the service via different methods. Try various valid and invalid currencies, transaction dates, negative amounts etc.  

### Swagger
Use the ```Swagger UI``` (http://localhost:8080/swagger-ui.html) to explore the available endpoints, view API documentation, and test the API. You can send requests directly from the Swagger interface.

### Postman
Use ```Postman```. Import the "Exchange Rate and Currency Conversion API.postman_collection.json" collection in the root directory and explore the endpoints.

### curl

Use ```curl```. Here are the example requests:

```
curl --location 'http://localhost:8080/v1/exchange-rate?sourceCurrency=EUR&targetCurrency=GBP'
curl --location --request POST 'http://localhost:8080/v1/convert-currency?sourceCurrency=TRY&targetCurrency=GBP&amount=120'
curl --location 'http://localhost:8080/v1/conversion-history?transactionDate=2024-07-30&transactionId='
```
