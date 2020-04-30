# There is an Architecture of my practise
![](MICROSERVICES_WITH_SPRING_BOOT_AND_SPRING_CLOUD.jpg)

1. HTTPS is used for external communication, while plain text HTTP is used inside the system landscape.
2. The local OAuth 2.0 authorization server will be accessed externally through the edge server.
3. Both the edge server and the product composite microservice will validate access tokens as signed JWT tokens.
4. The edge server and the product composite microservice will get the authorization server's public keys from its jwk-set endpoint, and use them to validate the signature of the JWT-based access tokens
