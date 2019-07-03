# CAGEF API

CAGEF (Cadastro Geral de Funções) is a system to unify the registers of all volunteers and praying houses of the cities of the region of Porto Ferreira - SP. The user interface is [CAGEF UI](https://github.com/elbiocaetano/cagef-ui).

## Getting Started

Follow the next steps in order to run this project on your local environment.

### Prerequisites

Install [Java JDK 8+](https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html).The JDK (Java Development Kit) is a development environment for building applications, applets, and components using the Java programming language.

The database used is [MySQL 8+](https://www.mysql.com/downloads).

To build the project ans install the dependencies you will need [Maven 3.6+](https://maven.apache.org/download.cgi).


### Installing

After installing the prerequisites, run the installation of the dependencies of the project. In the main folder of the project, run the following command

```
mvn clean install
```

Edit the file [application.properties](src/main/resources/application.properties) with the correct database configuration. In this file you can configure the property `jwt.secret` to a secret used to generate the token for your application.

To generate a build run the following command

```
mvn clean package
```
This will generate a target folder with a jar named `cagef-[version].jar`. For example: `cagef-2.0.0.jar`;

To start the server run the following command

```
java -jar cagef-[version].jar
```

The development server will start in the port 8080

## Deployment

You can use the jar generated in the previous step to deploy in your application server

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Hibernate](https://hibernate.org/) - Hibernate ORM enables developers to more easily write applications whose data outlives the application process. As an Object/Relational Mapping (ORM) framework, Hibernate is concerned with data persistence as it applies to relational databases (via JDBC).
## Authors

* **Elbio Caetano** - *Initial work* - [Elbio Caetano](https://github.com/elbiocaetano)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

