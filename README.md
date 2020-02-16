# paamelding

How to start the paamelding application
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/paamelding-1.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:5000/swagger`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`


TODO 
---
- Riktig logikk for slack-oppdatering
- Hente inn kamper fra nif?
- Historiske hendelser (paginering?)
- Frontend :D :D 
- Https?


- Slack? Asynkront basert på endringer? 