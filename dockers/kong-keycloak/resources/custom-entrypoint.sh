#!/usr/bin/env bash

echo "Starting Postgres..."

sudo -u postgres /usr/bin/initdb /var/lib/pgsql/data/
sudo -u postgres /usr/bin/pg_ctl start \
       -D /var/lib/pgsql/data -s -o "-p 5432" -w -t 300
