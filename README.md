Addressbook
====================


This address book is a Java EE 7 webapp based on the vaadin framework that allows contacts to be added to a webform. The goal behind writing this application was to demonstrate a proof of concept of an EE 7 application getting deployed to OSE 2.2 via a CI/CD pipeline using a JBoss EAP 6.1.1 cartridge but also a custom Mongo V3 cartridge. This app can also run on a plain old Jboss EAP 6.4 container with a standalone Mongo V3 database. This application does not use any persistence frameworks. 



Building the application from the command line
-------------------
Set the URL to the database in the mongodb.properties file under the resources folder then run the Maven build as follows. No credentials for the database connection is required at this point in time, a feature to be implemented later. If using OSE, then the URL or loopback IP (if no scaling) to the Mongo 3 Gear should be set.

mvn clean package

The target folder will contain the addressbook-2.0.war file.

Deploying to JBoss EAP 6.4 Container
--------------------

This application may be deployed to a JBoss EAP 6.4 container either via the Admin console or via CLI. Check the product documentation for how-to.

