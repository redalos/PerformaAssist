# Manus SaaS Backend

Minimal Spring Boot project. Use Maven to build:

```bash
mvn spring-boot:run
```

Default port: 8081

### Testing

Run the application and check the health endpoint:
```bash
# Start the Spring Boot app
cd backend
mvn spring-boot:run
```
Then in another terminal:
```bash
curl http://localhost:8081/api/health
```
The endpoint should return `OK`.
