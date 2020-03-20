FROM openjdk:8

RUN mkdir /apps
COPY ./target/multi-tenant-auth-server-0.0.1-SNAPSHOT.jar /apps/appserver.jar
COPY ./docker-entrypoint.sh /apps/docker-entrypoint.sh
RUN chmod 777 /apps/docker-entrypoint.sh

ENTRYPOINT ["/apps/docker-entrypoint.sh"]
EXPOSE 9500