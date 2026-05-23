# Sistema de Reservas en una Aerolinea - Proyecto con Microservicios

## Descripción General

Este proyecto es un **sistema de reservas de aerolínea como parte del proyecto final del proyecto final del Bootcamp de Senior a Arquitecto - Programando En Java**, desarrollado con **Java + Spring Boot**.

El objetivo es simular cómo se construyen sistemas backend modernos en empresas:

* Arquitectura de microservicios
* Comunicación basada en eventos
* Procesamiento de pagos desacoplado (Strategy Pattern)
* Transacciones distribuidas (Saga Pattern)
* Seguridad con JWT y control de roles

> ⚠️ Este proyecto es **100% práctico**. Aquí no hay teoría innecesaria: construyes un sistema real de principio a fin.

---

## Alcance del MVP (2 semanas)

El proyecto está diseñado para enfocarse en **4 flujos principales**:

### Incluido

* 🔍 Búsqueda de vuelos
* 🧾 Reserva de vuelos
* 💳 Procesamiento de pagos
* 🛫 Check-in online

### Fuera de alcance

* Programas de fidelización
* Gestión de equipaje
* Analítica avanzada
* Arquitecturas avanzadas (DDD / Hexagonal)

---

## Arquitectura

El sistema está basado en **microservicios independientes**, donde cada servicio tiene su propia responsabilidad.

### Servicios

| Servicio             | Responsabilidad                   |
| -------------------- | --------------------------------- |
| Flight Service       | Gestión de vuelos e inventario    |
| Booking Service      | Gestión de reservas               |
| Payment Service      | Procesamiento de pagos            |
| Check-in Service     | Check-in y tarjeta de embarque    |
| Notification Service | Notificaciones basadas en eventos |

---

## Flujo Principal

```text id="m2a9fd"
Buscar vuelo → Reservar → Pagar → Check-in → Notificar
```

---

## Stack Tecnológico

* Java 17+
* Spring Boot
* Spring Data JPA
* Spring Security (JWT)
* H2 (desarrollo) / PostgreSQL (producción)
* Docker & Docker Compose
* GitHub Actions (CI/CD)

---

## 📁 Estructura del Proyecto

```text id="cd3nqp"
airline-system/
├── flight-service/
├── booking-service/
├── payment-service/
├── checkin-service/
├── notification-service/
└── docker-compose.yml
```

---

## Cómo ejecutar el proyecto

### 1. Clonar repositorio

```bash id="t5rw0j"
git clone <tu-repositorio>
cd airline-system
```

### 2. Ejecutar con Docker

```bash id="h8k4pd"
docker-compose up --build
```

### 3. Ejecutar en local (perfil dev)

```bash id="f7n0sa"
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Autenticación

El sistema usa **JWT (JSON Web Tokens)** para autenticación stateless.

### Roles

* `PASSENGER` → puede reservar y hacer check-in de sus vuelos
* `AGENT` → puede gestionar reservas
* `ADMIN` → acceso completo

---

## Testing

* Tests unitarios (TDD)
* Tests de integración (MockMvc)
* Cobertura mínima recomendada: **70%**

Ejecutar tests:

```bash id="m2pr9k"
mvn test
```

---

## Arquitectura basada en eventos

El sistema usa **Spring Events** para desacoplar servicios.

### Eventos principales

* `BookingCreatedEvent`
* `PaymentProcessedEvent`
* `CheckInCompletedEvent`

---

## Transacciones distribuidas (Saga Pattern)

El flujo de reserva sigue el patrón Saga:

```text id="6z7g2p"
Crear reserva
   ↓
Bloquear asientos
   ↓
Procesar pago
   ↓
Confirmar reserva
```

Si el pago falla:

```text id="6a2qwv"
Liberar asientos (acción compensatoria)
```

---

## Flujo de trabajo (GitHub)

Se utiliza:

* **Issues** → User Stories
* **Epics** → funcionalidades grandes
* **GitHub Projects** → tablero Kanban

---

## Definition of Done

Una funcionalidad se considera terminada cuando:

* El código compila
* Los tests pasan
* Se cumplen los criterios de aceptación
* No hay estados inconsistentes
* Ha sido revisada

---

## Objetivo Final

Al completar este proyecto serás capaz de:

* Entender cómo se construyen sistemas backend reales
* Trabajar con microservicios y eventos
* Implementar patrones de arquitectura usados en producción
* Tener un proyecto sólido para tu portfolio

### REQUESTS PRUEBA
##
GET http://localhost:8080/api/v1/flights/search?origin=MAD&destination=BCN&date=2026-05-20
Authorization: Basic admin bootcamp2026


###
GET http://localhost:8080/api/v1/flights/search?origin=MAD&destination=BCN&date=2026-05-20
Authorization: Basic admin bootcamp2026


###
POST http://localhost:8080/api/v1/flights/FR098/reserve?
quantity=500
Authorization: Basic admin bootcamp2026


###
POST http://localhost:8082/api/v1/bookings
Content-Type: application/json
Authorization: Basic admin bootcamp2026

{
"flightNumber": "IB3040",
"passengerId": 1,
"seats": 1
}
# TRAS IMPLEMENTAR APIGETWAY & IAM ESTO NO DEJA ENTRAR COMO NO SEA POR APIGETWAY QUE ES EL 8080
GET http://localhost:8080/api/v1/flights/search?origin=MAD&destination=BCN

HTTP/1.1 401 Unauthorized
WWW-Authenticate: Bearer

GET http://localhost:8081/api/v1/flights/search?origin=MAD&destination=BCN

HTTP/1.1 403
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
Content-Type: application/json;charset=ISO-8859-1
Content-Length: 108
Date: Sun, 17 May 2026 17:36:56 GMT

{
"error": "Missing X-Gateway-Token",
"message": "This microservice requires requests from API Gateway only"
}

### Ruta feliz AUTH
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
"username": "usuario_admin",
"password": "1234"
}
HTTP/1.1 200 OK
transfer-encoding: chunked


{
"token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL21pLWFwaS5jb20iLCJzdWIiOiJ1c3VhcmlvX2FkbWluIiwiaWQiOjIsImV4cCI6MTc3OTA0MDA0NywiaWF0IjoxNzc5MDM5Njg3LCJyb2xlcyI6WyJBRE1JTiJdfQ.27w7V4xxG-ri_sd6NqeLVCVz1rYmKcN0AsRDpYiIDHg"
}

GET http://localhost:8080/api/v1/flights/search?origin=MAD&destination=BCN
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL21pLWFwaS5jb20iLCJzdWIiOiJ1c3VhcmlvX2FkbWluIiwiaWQiOjIsImV4cCI6MTc3OTA0MDA0NywiaWF0IjoxNzc5MDM5Njg3LCJyb2xlcyI6WyJBRE1JTiJdfQ.27w7V4xxG-ri_sd6NqeLVCVz1rYmKcN0AsRDpYiIDHg


[
{
"id": 3,
"flightNumber": "FR098",
"origin": "MAD",
"destination": "BCN",
"departureTime": "2026-05-20T08:00:00",
"price": 50,
"availableSeats": 180
}
]

###
POST http://localhost:8080/api/v1/bookings
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL21pLWFwaS5jb20iLCJzdWIiOiJ1c3VhcmlvX2FkbWluIiwiaWQiOjIsImV4cCI6MTc3OTEyOTkwNCwiaWF0IjoxNzc5MDQzNTA0LCJyb2xlcyI6WyJBRE1JTiJdfQ.CmJC76haECpYcgCSMWA2bFGk03FPyGkzEO90_hUrBoM
Content-Type: application/json

{
"flightNumber": "IB3040",
"passengerId": 1,
"passengerEmail": "test@test.com",
"seats": 2
}

###
POST http://localhost:8080/api/v1/payments
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL21pLWFwaS5jb20iLCJzdWIiOiJ1c3VhcmlvX2FkbWluIiwiaWQiOjIsImV4cCI6MTc3OTEyOTkwNCwiaWF0IjoxNzc5MDQzNTA0LCJyb2xlcyI6WyJBRE1JTiJdfQ.CmJC76haECpYcgCSMWA2bFGk03FPyGkzEO90_hUrBoM
Content-Type: application/json

{
"bookingId": "7c80b0f3-8532-4678-88dd-20c7d58212e1",
"userEmail": "usuario_admin@airline.com",
"amount": 150.00
}
###
POST http://localhost:8080/api/v1/bookings/65b03ba1-08b8-4f61-b0cd-f3cb37f02aa1/pay
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL21pLWFwaS5jb20iLCJzdWIiOiJ1c3VhcmlvX2FkbWluIiwiaWQiOjIsImV4cCI6MTc3OTEyOTkwNCwiaWF0IjoxNzc5MDQzNTA0LCJyb2xlcyI6WyJBRE1JTiJdfQ.CmJC76haECpYcgCSMWA2bFGk03FPyGkzEO90_hUrBoM
Content-Type: application/json

{
"userEmail": "test@test.com"
}
###
POST http://localhost:8080/api/v1/checkin/16fab3ad-d69a-4479-8862-2d16732ea777
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL21pLWFwaS5jb20iLCJzdWIiOiJ1c3VhcmlvX2FkbWluIiwiaWQiOjIsImV4cCI6MTc3OTE1MDIyMSwiaWF0IjoxNzc5MDYzODIxLCJyb2xlcyI6WyJBRE1JTiJdfQ.fEX93_ZmAtFt8bIrD9aE_FYecOY9vIXgmNez1DK_xVo
