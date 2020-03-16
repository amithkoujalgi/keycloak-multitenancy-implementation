FROM ubuntu:18.04


RUN mkdir -p ~/apps/
RUN mkdir -p ~/apps/mount

## ----- Begin: Setup Kong -----
RUN apt-get update
RUN apt-get install -y apt-transport-https curl lsb-core wget nano
RUN echo "deb https://kong.bintray.com/kong-deb `lsb_release -sc` main" | tee -a /etc/apt/sources.list
RUN curl -o bintray.key https://bintray.com/user/downloadSubjectPublicKey?username=bintray
RUN apt-key add bintray.key
RUN apt-get update
RUN apt-get install -y kong git unzip

RUN cd /etc/kong/ && kong config init

COPY ./resources/kong.conf /etc/kong/kong.conf
COPY ./resources/nginx-kong.conf /usr/local/kong/nginx-kong.conf
COPY ./resources/plugins/kong-plugin-replace-url-master/kong/plugins/replace-url /usr/local/share/lua/5.1/kong/plugins/kong-plugin-replace-url
# ----- End: Setup Kong -----

# ----- Begin: Setup PostgreSQL for Kong -----
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get install -y ca-certificates
RUN wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add -
RUN sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ `lsb_release -cs`-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'
RUN apt-get update && apt-get install -y postgresql postgresql-contrib
RUN /etc/init.d/postgresql start && \
    su - postgres -c "psql -c \"CREATE USER kong with encrypted password 'kong';\"" && \
    su - postgres -c "psql -c 'CREATE DATABASE kong OWNER kong;'"  && \
    su - postgres -c "psql -c 'grant all privileges on database kong to kong;'"
RUN cd ~/apps/ && echo "/etc/init.d/postgresql start" > start-postgresql.sh && chmod 777 start-postgresql.sh
# ----- End: Setup PostgreSQL for Kong -----

COPY ./resources/entrypoint.sh /usr/local/bin/
COPY ./resources/custom_nginx.template /root/apps/

RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
EXPOSE 8000 8443 8001 5432