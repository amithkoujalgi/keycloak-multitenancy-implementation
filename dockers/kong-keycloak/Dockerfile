FROM jboss/base-jdk:8

ENV KEYCLOAK_VERSION 7.0.0
ENV JDBC_POSTGRES_VERSION 42.2.5
ENV JDBC_MYSQL_VERSION 5.1.46
ENV JDBC_MARIADB_VERSION 2.2.3
ENV JDBC_MSSQL_VERSION 7.4.1.jre8

ENV LAUNCH_JBOSS_IN_BACKGROUND 1
ENV PROXY_ADDRESS_FORWARDING false
ENV JBOSS_HOME /opt/jboss/keycloak
ENV LANG en_US.UTF-8

ARG GIT_REPO
ARG GIT_BRANCH
ARG KEYCLOAK_DIST=https://downloads.jboss.org/keycloak/$KEYCLOAK_VERSION/keycloak-$KEYCLOAK_VERSION.tar.gz

USER root

RUN yum update -y && yum install -y epel-release git && yum install -y jq openssl which && yum clean all

ADD tools /opt/jboss/tools
RUN /opt/jboss/tools/build-keycloak.sh


# POSTGRES
#
#RUN yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
#RUN /usr/pgsql-12/bin/postgresql-12-setup initdb
#RUN systemctl enable postgresql-12 && \
#   systemctl start postgresql-12

RUN yum install -y postgresql-server sudo
RUN sudo -u postgres /usr/bin/initdb /var/lib/pgsql/data/
RUN sudo -u postgres /usr/bin/pg_ctl start \
       -D /var/lib/pgsql/data -s -o "-p 5432" -w -t 300

# KONG

RUN yum install -y wget && \
    wget -O kong-1.3.0.rhel7.amd64.rpm https://bintray.com/kong/kong-rpm/download_file?file_path=rhel/7/kong-1.3.0.rhel7.amd64.rpm && \
    yum install -y kong-1.3.0.rhel7.amd64.rpm --nogpgcheck

RUN cd /etc/kong/ && kong config init

COPY ./resources/kong.conf /etc/kong/kong.conf
COPY ./resources/nginx-kong.conf /usr/local/kong/nginx-kong.conf
COPY ./resources/plugins/kong-plugin-replace-url-master/kong/plugins/replace-url /usr/local/share/lua/5.1/kong/plugins/kong-plugin-replace-url

COPY ./resources/custom-entrypoint.sh /root/apps/custom-entrypoint.sh
COPY ./resources/custom_nginx.template /root/apps/
COPY ./resources/application.keystore /root/apps/

#USER 1000

EXPOSE 8080
EXPOSE 8443

ENTRYPOINT [ "/opt/jboss/tools/docker-entrypoint.sh" ]

CMD ["-b", "0.0.0.0"]