# Hotels & Package Tours

## About:

Simple Java Gradle project working with database and exposing SOAP web service interface. The goal of this project is to implement a service that manages hotels and packages (package tours).

**Created:** Aug 2014

**Last update:** Nov 2015

**Keywords:** [Hibernate](http://hibernate.org/orm/), [Spring MVC, Spring Data](), [Thymeleaf](), [HyperSQL Database Engine (HSQLDB)](http://hsqldb.org/), [IBM WAS Liberty Profile](https://developer.ibm.com/wasdev/), [Gradle](http://gradle.org/), [Maven](https://maven.apache.org/)

## Assignment:

### Data Model

Application lists hotels. Hotel has name, location/destination, and a flag whether this record is in active or inactive state. Every hotel can have packages. Package is defined by hotel, date of arrival and departure. Similarly as hotel, package can be either active or inactive.
Every hotel will have exactly one price which is the lowest price for all packages assigned to it. Analogously there will be the lowest price of packages attached to the location/destination.


### Description of exposed services

1.	Get a list of locations / destinations (optionally search by name)
2.	Create location / destination
3.	Get a list of hotels (optionally search by location / destination and price or range of prices)
4.	Create new hotel
5.	Activate / Deactivate hotel
6.	Create package
7.	Change package price
8.	Activate / Deactivate package
9.	Get a list of locations / destinations, which do not have any active hotels
10.	Get all active pairs of location/destination and it’s cheapest package (returns list of location/destination, name of hotel, departure, arrival, price and duration of package)


## Setup:

Next steps are relevant for [Eclipse IDE](http://www.eclipse.org/downloads/packages/) with [IBM WAS Liberty Profile Adapter](https://developer.ibm.com/wasdev/), [Gradle plugin](https://marketplace.eclipse.org/content/gradle-integration-eclipse-0), [Maven plugin](http://www.eclipse.org/m2e/) and [TestNG plugin](http://testng.org/doc/eclipse.html).

1. Import Gradle project "hpt-core" to Eclipse IDE

2. Import Maven project "hpt-schema" to Eclipse IDE

3. Install & configure your [WAS Liberty Profile] instance and copy [server.xml] to ```<your-was-instance>\usr\servers\<your-server-name>\server.xml```

2. Install & configure [HyperSQL Database Engine (HSQLDB)](http://hsqldb.org/)

3. Open [startHsqldb.bat]() file and set absolute path to your HSQLDB instance

4. Start DB with [startHsqldb.bat]()

1. ```gradle clean build```

5. ```mvn clean install```

## Notes:

* You can integrate ```hpt-schema.jar``` from [hpt-schema] to your application if you want to invoke web services [hpt-core].

* Generated SQL script to drop and create schema: ```target/createSchema.sql```

