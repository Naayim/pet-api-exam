# Pet API – Prueba técnica

API REST en Spring Boot que expone consulta y alta de mascotas, integrándose con la API pública de [Swagger Petstore](https://petstore.swagger.io/).

## Stack
- Java 17
- Spring Boot 3.2.7 (Spring Web)
- Gradle (Groovy DSL) 8.8
- `RestClient` para la integración con Petstore

> Nota: Spring Initializr ya no ofrece la versión 3.2.7, así que el proyecto se generó con la versión disponible y se ajustó a 3.2.7 en `build.gradle` y en el Gradle Wrapper.

## Estructura
```
com.example.exam
├── controller   → APIs REST
├── service      → Lógica de negocio
├── client       → Integración con APIs de terceros (Petstore)
│   └── dto      → Modelo de Petstore
├── model        → DTOs propios de la API (request/response)
└── exception    → Manejo global de errores (ProblemDetail / RFC 7807)
```

## Ejecutar
```bash
./gradlew bootRun        # Linux / macOS
gradlew.bat bootRun      # Windows
```
La API queda en `http://localhost:8080`.

En Linux/macOS, si `./gradlew` marca permiso denegado: `chmod +x gradlew`

## Endpoints

### GET /api/pet/{petId}
```bash
curl http://localhost:8080/api/pet/777
```
```json
{ "id": 777, "name": "Firulais", "status": "available" }
```

### POST /api/pet
```bash
curl -X POST http://localhost:8080/api/pet \
  -H "Content-Type: application/json" \
  -d '{"id": 777, "name": "Firulais", "status": "available"}'
```
Respuesta `201 Created`:
```json
{
  "transactionId": "a0dfc454-4013-435e-9ad1-e3496e88e254",
  "dateCreated": "2026-11-12T00:22:18.6924214",
  "status": "available",
  "name": "Firulais"
}
```
- `transactionId`: UUID v4 generado en la capa service.
- `dateCreated`: fecha y hora actual del sistema.

### Errores
| Caso | Status |
|---|---|
| Pet no existe en Petstore | `404 Not Found` |
| Falla de comunicación con Petstore | `502 Bad Gateway` |

## Tests
```bash
gradlew.bat test
```
- `PetServiceTest`: unitarios con Mockito (mapeo, UUID v4, fecha).
- `PetControllerTest`: `@WebMvcTest` (contrato HTTP y manejo de errores).
