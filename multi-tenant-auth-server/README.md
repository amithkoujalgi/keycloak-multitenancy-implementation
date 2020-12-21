Build and start from docker:

```
docker run -it \
    -e KC_ADMIN_ENDPOINT=http://192.168.1.109:8080/auth \
    -e KC_AUTH_HOST=192.168.1.109 \
    -e KC_AUTH_PORT=8000 \
    multi-tenant-auth-server:1.0
```

Start and setup tenants in Keycloak. Use the JSONs to setup the realms.


Add the following entries to **/etc/hosts** file:

```
127.0.0.1 master.localhost
127.0.0.1 test.localhost
```

Then go to http://master.localhost:9090/login or http://test.localhost:9090/login to login with the respective tenants.

References:

https://medium.com/keycloak/secure-spring-boot-2-using-keycloak-f755bc255b68

https://blog.ineat-conseil.fr/2018/11/securisez-vos-apis-spring-avec-keycloak-5-mise-en-place-dune-authentification-multi-domaines/

https://github.com/ineat/spring-keycloak-multitenant/tree/master/realm-export

https://github.com/vimalKeshu/movie-app/tree/spring-boot-2-kc-multitenancy/src

https://issues.redhat.com/browse/KEYCLOAK-4139?focusedCommentId=13592918&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#comment-13592918