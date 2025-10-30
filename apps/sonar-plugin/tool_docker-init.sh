#!/usr/bin/env sh

# docker-compose up --build -d

# pour avoir les logs de ce qui se passe dans le container lors de sa construction et deploiement
# Build avec logs visibles
docker-compose build --progress=plain --no-cache
# Puis lancer en détaché
docker-compose up -d
