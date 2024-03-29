#!/bin/bash

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE USER $FERRET_USER WITH PASSWORD '$FERRET_PASS';
    CREATE DATABASE $FERRET_DB;
    GRANT ALL PRIVILEGES ON DATABASE $FERRET_DB TO $FERRET_USER;
EOSQL
