# Keycloak realm configuration

## Introduction
This folder contains a configuration file for a demo realm that is used by the [OIDC Sample](../README.md).

## How to use
1. Install Docker on your machine
2. Run the <code>docker run</code> command like below and insert the **absolute** path to this repo folder.


### Docker run command
```bash
docker run -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin \
    -e KEYCLOAK_IMPORT=/tmp/config/malis.io-realm.json -v {replace_with_repo_path}:/tmp/config -p 8080:8080 jboss/keycloak:6.0.1
```
Example with replaced path:
```bash
docker run -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin \
    -e KEYCLOAK_IMPORT=/tmp/config/malis.io-realm.json -v /Users/malis.io/dev/oidc-sample/keycloak:/tmp/config -p 8080:8080 jboss/keycloak:6.0.1
```

## Using Keycloak
After successfully running the command above you have a fully working OIDC Provider setup with a configured Realm ready for use.

If you haven't changed the port in the run command, keycloak listens on http://localhost:8080. You can log in into the Administration Console with 
```
user:admin
password:admin
```
Navigate through Keycloak to get an overview of the configured realm <code>malis.io</code>. Refer to the [official keycloak documentation](https://www.keycloak.org/docs/6.0/server_admin/index.html), if you need further assistance.

## The configured realm
There is a preconfigured realm <code>malis.io</code>, which has a configured client <code>oidc-spring-boot</code> with all redirect URLs setup ready to go, if you keep the default configuration for the [Spring Boot App](../oidc-spring-boot/README.md).
There is a preconfigured user which you can use to log in via your OIDC client:
```
user:demo@malis.io
password:demo
```

## Troubleshooting

If you get an error like the following when the keycloak docker container is starting up:

```java
Caused by: java.io.FileNotFoundException: /tmp/config/malis.io-realm.json (No such file or directory)
	at java.io.FileInputStream.open0(Native Method)
	at java.io.FileInputStream.open(FileInputStream.java:195)
	at java.io.FileInputStream.<init>(FileInputStream.java:138)
	at java.io.FileInputStream.<init>(FileInputStream.java:93)
	at org.keycloak.services.resources.KeycloakApplication.importRealms(KeycloakApplication.java:368)
	... 40 more
```

Check if you correctly replaced the {replace_with_repo_path} variable in the <code>docker run</code> command.