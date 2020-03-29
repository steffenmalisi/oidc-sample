# OIDC Sample

## Introduction
This is a sample showing the use of OpenID Connect (OIDC) with Spring Boot, Spring Security and Keycloak.
If you are interested in some basic information about OIDC, please refer to [my article on Medium](https://medium.com/@steffenmalisi/secure-self-contained-systems-authn-in-a-distributed-world-943ff9d0c4dd).


## How to use
In the subfolders you can find further ressources.
* [keycloak](./keycloak/README.md): a docker configuration which can be used to run keycloak on your local machine
* [oidc-spring-boot](./oidc-spring-boot/README.md): a spring boot application, which is configured to use the keycloak application as an identity provider. It protects its resources via spring security and acts as an OAUTH2 client