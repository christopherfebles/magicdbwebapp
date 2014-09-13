# Magic the Gathering® Database Web Application Project #

### Summary ###

* This project provides an online interface to a database of Magic the Gathering® cards.
* Version: 1.0.0

### Build Instructions ###

* Database configuration
    * The project depends on the database being configured according to the [MagicDBAPI](https://github.com/christopherfebles/magicdbapi) project.
    * The project depends on the database being populated by to the [MagicDBUpdater](https://bitbucket.org/cfebles2/magicdbupdater) project.
* Configuration
    * Spring and Logback properties files are located in src/main/resources/
        * Spring configuration is imported from [MagicDBAPI](https://github.com/christopherfebles/magicdbapi).
    * Maven configuration is split between a parent_pom.xml located in [MagicDBAPI](https://github.com/christopherfebles/magicdbapi) and local, child pom.xml at the root of this project.
* Dependencies
    * Dependencies are loaded by Maven, and documented in the parent and child POM files.
    * Current Dependencies:
        * [MagicDBAPI](https://github.com/christopherfebles/magicdbapi)
        * Spring MVC
        * Jackson 2 Databind
        * Jackson 2 Annotations
        * Apache Commons
        * Apache Tomcat 7
        * JSTL
        * JQuery
        * JQuery UI
        * JUnit
        * Spring Test
        * SLF4J
        * Logback
* How to run tests
    * Unit tests now include a mock database, so they can all run independently.
    * Unit tests run as part of the Maven build.
* Deployment instructions
    * There are two methods to deploy this app: Manually, or via Maven
        * Manual
            * Package this project into a WAR, and drop into your web container of choice
            * I've only tested this app on Tomcat 7. There may be unforeseen difficulties.
            * Maven build targets:
                * clean package
        * Maven
            * The POM file is configured for auto-deploy into Tomcat 7.
            * Tomcat needs to be configured with an "admin" user, no password, with manager-script permissions.
                * If a password is desired, it can be configured in this project's POM.
            * Maven build targets:
                * clean tomcat7:redeploy
