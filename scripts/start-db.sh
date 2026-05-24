#!/bin/bash

CONTAINER_NAME="tracker"
ORACLE_PASSWORD="cheese"
IMAGE="gvenzl/oracle-free"

if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    echo "Container exists, starting..."
    docker start "$CONTAINER_NAME"
else
    echo "Container not found, creating new one..."
    docker run -d \
        --name "$CONTAINER_NAME" \
        -p 1521:1521 \
        -e ORACLE_PASSWORD="$ORACLE_PASSWORD" \
        "$IMAGE"
fi