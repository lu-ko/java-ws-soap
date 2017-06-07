# Hotels & Package Tours

## About:

Simple Java Gradle project working with database and exposing SOAP web service interface. The goal of this project is to implement a service that manages hotels and packages (package tours).

**Created:** Aug 2014

**Last update:** Jun 2017

**Keywords:** [Hibernate 5.2.10.Final](http://hibernate.org/orm/), [Spring MVC 4.3.7.RELEASE](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html), [Spring Data 1.11.3.RELEASE](http://projects.spring.io/spring-data/), [Spring WS 2.4.0.RELEASE](http://projects.spring.io/spring-ws/), [Thymeleaf 2.1.2](http://www.thymeleaf.org/), [HyperSQL Database Engine (HSQLDB) 2.4.0](http://hsqldb.org/), [IBM WAS Liberty Profile](https://developer.ibm.com/wasdev/), [Gradle](http://gradle.org/), [Maven](https://maven.apache.org/), [TestNG](http://testng.org/), for more details please see [gradle-dependencies.txt](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/gradle-dependencies.txt)

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


## Setup

Next steps are relevant for [Eclipse IDE](http://www.eclipse.org/downloads/packages/) with [IBM WAS Liberty Profile Adapter](https://developer.ibm.com/wasdev/), [Gradle plugin](https://marketplace.eclipse.org/content/gradle-integration-eclipse-0), [Maven plugin](http://www.eclipse.org/m2e/) and [TestNG plugin](http://testng.org/doc/eclipse.html).


### Build module ```hpt-schema```

1. Import Maven project [hpt-schema](https://github.com/lu-ko/java-ws-soap/tree/master/hotels-package-tours/hpt-schema) to Eclipse IDE

2. Build project:
  * ```mvn clean install```


### Build module ```hpt-core```

1. Import Gradle project [hpt-core](https://github.com/lu-ko/java-ws-soap/tree/master/hotels-package-tours/hpt-core) to Eclipse IDE

2. Install & configure [HyperSQL Database Engine (HSQLDB)](http://hsqldb.org/)

3. Open [startHsqldb.bat](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/hpt-core/src/test/resources/hsqldb/startHsqldb.bat) file and set absolute path to your HSQLDB instance

4. Start DB with [startHsqldb.bat](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/hpt-core/src/test/resources/hsqldb/startHsqldb.bat)

5. Connect to your HSQLDB instance and execute [schema_hsql.sql](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/hpt-core/src/test/resources/hsqldb/schema_hsql.sql). Three tables should be created (Destination, Hotel, Package) with no rows.

6. Build project:
  * ```gradle clean build``` (with tests) or ```gradle clean install``` (without tests)


### Deployment and first startup

1. Install & configure application server
  * E.g. [WAS Liberty Profile](https://developer.ibm.com/wasdev/)

2. Setup deployment regarding choosen application server
  * E.g. In case of WAS Liberty Profile please copy content of [server.xml.txt](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/hpt-core/src/test/resources/was/server.xml.txt) to your WAS instance:
    * ```<your-was-instance>/usr/servers/<your-server-name>/server.xml```

3. Deploy WAR to your WAS instance
  * E.g. In case of WAS Liberty Profile: ```gradle clean deployToLocalhost -Ddropins.dir="<your-was-instance>/usr/servers/<your-server-name>/dropins"```

4. Open web browser and go to URL: [http://localhost:9080/hptApp/ui](http://localhost:9080/hptApp/ui)

5. To interact with exposed web services you can use any testing tool (e.g. [SoapUi](http://www.soapui.org/)), see [http://localhost:9080/hptApp/services/HptService.wsdl](http://localhost:9080/hptApp/services/HptService.wsdl)


## Notes

* You can find built ```hpt-schema-X.Y.jar``` in ```target/``` folder of [hpt-schema](https://github.com/lu-ko/java-ws-soap/tree/master/hotels-package-tours/hpt-schema) and integrate it to your application if you want to invoke web services [hpt-core](https://github.com/lu-ko/java-ws-soap/tree/master/hotels-package-tours/hpt-core). See [hpt-schema-1.0.jar](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/hpt-core/lib/hpt-schema-1.0.jar) and [build.gradle](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/hpt-core/build.gradle) for more details.

* You can generated SQL scripts to drop and create schema via [SchemaGeneratorTest.java](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/hpt-core/src/test/java/sk/elko/hpt/core/utils/SchemaGeneratorTest.java) TestNG test.
  * Just invoke [Generate DB schema](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/hpt-core/src/test/resources/testng/generate_db_schema.xml) TestNG suite from Gradle:
    * ```gradle clean test -Dtestng.suite="src/test/resources/testng/generate_db_schema.xml"```.
  * Output scripts will be located in ```build/``` folder of [hpt-core](https://github.com/lu-ko/java-ws-soap/tree/master/hotels-package-tours/hpt-core).

## Exposed Web Services

See [WSDL](http://localhost:9080/hptApp/services/HptService.wsdl) for more details.

1. Get a list of locations / destinations (optionally search by name)
  * Operation: findDestination
  * Possible results: 
    * a) List of all destinations in the DB (if no destination.name specified)
    * b) List that contains only one destination with the specified destination.name
    * c) Empty list (if no destinations in the DB or no destination with specified destination.name)

2. Create location / destination
  * Operation: createDestination
  * Possible results: 
    * a) DB ID of created destination
    * b) Fault if any error was occured

3. Get a list of hotels (optionally search by location / destination)
  * Operation: findHotel
  * Possible results: 
    * a) List of all hotels (active+inactive) in the DB (if no destination.id specified)
    * b) List that contains all hotels (active+inactive) with the specified destination.id
    * c) Empty list (if no hotels in the DB or no hotels with specified destination.id)

4. Create new hotel
  * Operation: createHotel
  * Possible results: 
    * a) DB ID of created hotel
    * b) Fault if any error was occured

5. Activate / Deactivate hotel
  * Operation: setHotelActive
  * Possible results: 
    * a) Empty response if specified hotel.active was updated in the DB
    * b) Fault if any error was occured

6. Create package
  * Operation: createPackage
  * Possible results: 
    * a) DB ID of created package
    * b) Fault if any error was occured

7. Change package price
  * Operation: setPackagePrice
  * Possible results: 
    * a) Empty response if specified package.price was updated in the DB
    * b) Fault if any error was occured

8. Activate / Deactivate package
  * Operation: setPackageActive
  * Possible results: 
    * a) Empty response if specified package.active was updated in the DB
    * b) Fault if any error was occured

9. Get a list of locations / destinations, which do not have any active hotels
  * Operation: destinationsNoActiveHotels
  * Possible results: 
    * b) List that contains all destinations with no active hotels or with no hotels
    * c) Empty list (if no suitable destination exists in the DB)


## FAQ

**Q1:** Why must be destination.name and hotel.nam unique?

**A1:** Because entities have no other attributes (except DB ID) that could be used to differs (e.g. code, address, country).


**Q2:** Why responses contains also ID of entities from DB?

**A2:** Because entities does not have any other unique identification (e.g. code).


**Q3:** How can I setup hotel to the specific destination?

**A3:** You have to create hotel (4.WS) with specified hotel.destination.id.


**Q4:** How can I setup package to the specific hotel?

**A4:** You have to create package (6.WS) with specified package.hotel.id.


**Q5:** Why findHotel (3.WS) returns also inactive hotels?

**A5:** Because user should know ID of all hotels and packages. Other WS (e.g. activation) expect ID as request parameter.


**Q6:** Why hotel.lowestPrice and destination.lowestPrice do not respect active/inactive attribute?

**A6:** Because data model specification tells:
  * "EVERY hotel will have exactly one price which is the lowest price for ALL packages assigned to it."
    * EVERY means active and inactive (hotels). ALL means active and inactive (packages).
  * "ANALOGOUSLY there will be the lowest price of packages attached to the location/destination."
    * ANALOGOUSLY means the same active/inactive logic.


**Q7:** Why 10.WS is not implemented?

**A7:** Because specification is not clear. This last service should "Get all ACTIVE pairs of location/destination and it’s cheapest package" which is inconsistent with specification of attribute hotel.lowestPrice and destination.lowestPrice (see Q6 and A6). I'm afraid user would be confused if this 10.WS will return one "lowest price" package and destination/hotel will have another "lowest price" attribute. This situation should be discussed with product owner.


**Q8:** What is purpose of dependency [hpt-schema-1.0.jar](https://github.com/lu-ko/java-ws-soap/blob/master/hotels-package-tours/hpt-core/lib/hpt-schema-1.0.jar)?

**A8:** This JAR file contains XSD, WSDL and generated Java classes for exposed web services. This dependency can be used in another software to simplify integration with module [hpt-core](https://github.com/lu-ko/java-ws-soap/tree/master/hotels-package-tours/hpt-core) via web services. See [Notes](https://github.com/lu-ko/java-ws-soap/tree/master/hotels-package-tours#notes).


