# Gruppe I
# Bedienungsanleitung

Herzlich Willkommen bei CheSEP,

um den Server perfekt aufzusetzen, installiere Docker und führe folgende Befehle aus

 
## 1. Um initial die DB zu starten
docker-compose up -d mysql

## 2. Um danach um alle Services zu starten
docker-compose up

Es werden folgende Ports besetzt, diese müssen vor der Installation frei sein.

3307 -> MySQL

4200 -> Angular

8080 -> Spring 

