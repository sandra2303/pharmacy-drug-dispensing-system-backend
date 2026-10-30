# Pharmacy Drug Dispensing System - Backend

| | |
|---|---|
| **Student** | UWIMBABAZI Sandrine |
| **Student ID** | 27399 |
| **Course** | INSY 8322 Web Technology and Internet |
| **Project** | Pharmacy Drug Dispensing System (Mid-Term Project) |

A REST API built with Java, Spring Boot, Spring Data JPA and PostgreSQL. It manages customers, pharmacists, counters, drug classes, drug orders and dispensed items.

## Steps to run the API

1. Install **JDK 21** and **PostgreSQL**.
2. Create the database (in pgAdmin or psql):
   ```sql
   CREATE DATABASE pharmacy_db;
   ```
3. Open `src/main/resources/application.properties` and set your PostgreSQL username and password:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/pharmacy_db
   spring.datasource.username=postgres
   spring.datasource.password=MY_PASSWORD
   ```
4. In the project folder, run the application:
   - Windows: `.\mvnw.cmd spring-boot:run`
5. The tables are created automatically (`spring.jpa.hibernate.ddl-auto=update`). The API runs on `http://localhost:8080`.
6. Import `pharmacy-drug-dispensing-system-midterm.postman_collection.json` into Postman. It has one request per requirement, named by its ID.
7. Enter the seed data through the `POST /save` endpoints before testing the queries (see "Seed data" below).

## Project structure

```
src/main/java/com/pharmacy/dispensing
  domain       entities and the DrugOrderStatus enum
  repository   Spring Data JPA repositories (derived queries and JPQL)
  service      business rules
  controller   REST endpoints
```

## Entities

`DrugClass`, `Counter`, `Pharmacist` (many-to-one Counter, many-to-many DrugClass), `Customer`, `DrugOrder` (many-to-one Customer and Pharmacist, status enum RECEIVED / PREPARING / DISPENSED / REJECTED), `DispensedItem` (many-to-one DrugOrder).

## Endpoints

| ID | Method and path |
|----|-----------------|
| A2 | `POST /api/{path}/save`, `GET /api/{path}/all`, `GET /api/{path}/{id}`, `PUT /api/{path}/update/{id}`, `DELETE /api/{path}/delete/{id}` where `{path}` is `drug-classes`, `counters`, `pharmacists`, `customers` or `drug-orders` |
| B1 | `GET /api/customers/by-last-name?lastName=` |
| B2 | `GET /api/drug-orders/by-status?status=` |
| B3 | `GET /api/drug-orders/between?start=&end=` |
| C1 | `GET /api/pharmacists/by-drug-class?name=` |
| C2 | `GET /api/counters/free` |
| C3 | `GET /api/drug-classes/unused` |
| C4 | `GET /api/customers/of-pharmacist/{pharmacistId}` |
| D1 | `POST /api/drug-orders/save` (past date 400, same pharmacist and date 409, always starts as RECEIVED) |
| D2 | `PATCH /api/drug-orders/{id}/status?status=` |
| D3 | `PUT /api/pharmacists/{pharmacistId}/drug-classes/{drugClassId}` |
| D4 | `PATCH /api/pharmacists/{pharmacistId}/counter?counterNumber=` |
| E1 | `POST /api/dispensed-items/save?drugOrderId=` |
| E2 | `GET /api/dispensed-items/customer/{customerNumber}` |
| E3 | `DELETE /api/dispensed-items/delete/{id}` (and a drug order with dispensed items cannot be deleted) |
| F1 | `GET /api/drug-orders/stats/by-status` |
| F2 | `PATCH /api/drug-orders/cancel-day?pharmacistId=&date=` |
| Bonus | `GET /api/dashboard/summary`, `GET /api/drug-orders/page?page=0&size=5&sort=orderDate,desc` |

## Status flow of a drug order

`RECEIVED` can become `PREPARING` or `REJECTED`. `PREPARING` can become `DISPENSED` or `REJECTED`. `DISPENSED` and `REJECTED` are final.

## Seed data

Entered through the API: 4 drug classes (Antibiotics, Analgesics, Antimalarials, Vaccines), 4 counters, 3 pharmacists, 5 customers (two share the last name Mukamana) and 10 drug orders in October and November 2026 using all four statuses.
