# Franquicias API - Spring WebFlux

A reactive REST API for managing franchises, branches, and products built with Spring WebFlux and PostgreSQL.

##  Features

- Full CRUD operations for franchises, branches, and products
- Reactive programming with Spring WebFlux
- PostgreSQL database with R2DBC
- Docker & Docker Compose support
- Unit and integration tests

##  Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.2.0 |
| Spring WebFlux | reactive |
| Spring Data R2DBC | - |
| PostgreSQL | 15+ |
| Gradle | 8.5 |

##  API Endpoints

### Franchises
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/franquicias` | List all franchises |
| GET | `/api/franquicias/{id}` | Get franchise by ID |
| POST | `/api/franquicias` | Create new franchise |
| PUT | `/api/franquicias/{id}` | Update franchise name |
| DELETE | `/api/franquicias/{id}` | Delete franchise |

### Branches
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/franquicias/{id}/sucursales` | List branches by franchise |
| POST | `/api/franquicias/{id}/sucursales` | Add branch to franchise |
| PUT | `/api/franquicias/sucursales/{id}` | Update branch name |
| DELETE | `/api/franquicias/sucursales/{id}` | Delete branch |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/franquicias/sucursales/{id}/productos` | List products by branch |
| POST | `/api/franquicias/sucursales/{id}/productos` | Add product to branch |
| PUT | `/api/franquicias/productos/{id}/stock` | Update product stock |
| PUT | `/api/franquicias/productos/{id}` | Update product name |
| DELETE | `/api/franquicias/productos/{id}` | Delete product |

### Reports
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/franquicias/{id}/productos-max-stock` | Product with max stock per branch |
| GET | `/api/franquicias/{id}/completa` | Complete franchise with all branches and products |

##  Quick Start with Docker

```bash
# Clone and navigate to project
cd franquicias-webflux

# Build and start services
docker-compose up -d

# API available at http://localhost:8080

# View logs
docker-compose logs -f api

# Stop services
docker-compose down
```

##  Local Development

### Prerequisites
- Java 21+
- PostgreSQL 15+
- Gradle 8.5

### Steps

1. Create database:
   ```sql
   CREATE DATABASE franquicias;
   ```

2. Configure database connection in `application.yml`

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

##  Usage Examples

### Create a franchise
```bash
curl -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre": "McDonalds"}'
```

### Add a branch
```bash
curl -X POST http://localhost:8080/api/franquicias/1/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Downtown Branch"}'
```

### Add a product
```bash
curl -X POST http://localhost:8080/api/franquicias/sucursales/1/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Burger", "stock": 100}'
```

### Update stock
```bash
curl -X PUT http://localhost:8080/api/franquicias/productos/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock": 150}'
```

### Get product with max stock per branch
```bash
curl http://localhost:8080/api/franquicias/1/productos-max-stock
```

### Get complete franchise with all branches and products
```bash
curl http://localhost:8080/api/franquicias/1/completa
```

##  Running Tests

```bash
./gradlew test
```

##  License

MIT
