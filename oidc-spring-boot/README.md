# Spring Boot OIDC Sample

## Introduction
This is a spring boot application, which is configured to use Keycloak as an identity provider (IDP).
It protects its resources via spring security and acts as an OAUTH2 client.
The default configuration of the app uses a local address for the IDP. Keycloak is shipped as docker configuration within this repo.
Refer to [keycloak docker](../keycloak/README.md).

## How to run the application

1. Startup the Keycloak Docker Container as described [here](../keycloak/README.md)
1. Navigate to this folder of the repository
2. Run <code>./gradlew clean bootRun</code>

Alternatively import the project into your favorite IDE and run the Main Class as an Spring Boot Application.
The project is built via gradle, so every major IDE should be supported. 

The default configuration is listed in [application.yml](src/main/resources/application.yml). Change it according
to your needs.

## How to use the application

If you have not changed the default configuration the application listens on port <code>8887</code>.

1. Navigate to [localhost:8887](http://localhost:8887).
You must then login at the IDP. The already setup credentials can be found [here](../keycloak/README.md).
2. After a successful login you see protected the main page with further details on what happened in the background.
3. Read the information on that page, follow the links to the official OIDC specs and discover the contents of your tokens.

## Troubleshooting

If you get the exception at startup:

```java
Caused by: java.net.ConnectException: Connection refused (Connection refused)
	at java.base/java.net.PlainSocketImpl.socketConnect(Native Method) ~[na:na]
	at java.base/java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:399) ~[na:na]
	at java.base/java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:242) ~[na:na]
	at java.base/java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:224) ~[na:na]
	at java.base/java.net.Socket.connect(Socket.java:591) ~[na:na]
	at java.base/java.net.Socket.connect(Socket.java:540) ~[na:na]
	at java.base/sun.net.NetworkClient.doConnect(NetworkClient.java:182) ~[na:na]
```

make sure that the IDP is available at the configured port (8080, if you did not change the default configuration)

## Further information regarding spring boot and OIDC

### Deprecated variant of using OAUTH2:
[This tutorial](https://spring.io/guides/tutorials/spring-boot-oauth2/) uses <code>org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure</code> which was set to maintenance mode
as stated [here](https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/htmlsingle/) (Search for maintenance mode).
Hint what i also experienced compared to the current Spring Security 5 solution: One drawback of using @EnableOAuth2Sso was that you could not use just the issuer-uri in your configuration.

### Spring OAUTH2 and OIDC features matrix
Please refer to the official Spring features matrix and frequently asked questions to the roadmap of Spring OAUTH2 and OIDC features:
https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix.

