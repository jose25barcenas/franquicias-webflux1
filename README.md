# Franquicias API - Spring WebFlux

API reactiva para gestión de franquicias, sucursales y productos construida con Spring WebFlux y PostgreSQL.

## Tecnologías

- Java 21
- Spring Boot 3.2.0
- Spring WebFlux (reactivo)
- Spring Data R2DBC (acceso reactivo a PostgreSQL)
- PostgreSQL 15
- Docker & Docker Compose

## Estructura del Proyecto

```
franquicias-webflux/
├── src/main/java/com/franquicias/
│   ├── FranquiciasApplication.java
│   ├── controller/
│   │   └── FranquiciaController.java
│   ├── dto/
│   │   ├── FranquiciaRequest.java
│   │   ├── ProductoRequest.java
│   │   ├── ProductoStockRequest.java
│   │   ├── ProductoMaxStockResponse.java
│   │   └── SucursalRequest.java
│   ├── model/
│   │   ├── Franquicia.java
│   │   ├── Producto.java
│   │   └── Sucursal.java
│   ├── repository/
│   │   ├── FranquiciaRepository.java
│   │   ├── ProductoRepository.java
│   │   └── SucursalRepository.java
│   └── service/
│       └── FranquiciaService.java
├── docker-compose.yml
├── Dockerfile
└── build.gradle
```

## Endpoints

### Franquicias
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/franquicias` | Listar todas las franquicias |
| GET | `/api/franquicias/{id}` | Obtener franquicia por ID |
| POST | `/api/franquicias` | Crear nueva franquicia |
| PUT | `/api/franquicias/{id}` | Actualizar nombre de franquicia |
| DELETE | `/api/franquicias/{id}` | Eliminar franquicia |

### Sucursales
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/franquicias/{id}/sucursales` | Listar sucursales de una franquicia |
| POST | `/api/franquicias/{id}/sucursales` | Agregar sucursal a franquicia |
| PUT | `/api/franquicias/sucursales/{id}` | Actualizar nombre de sucursal |
| DELETE | `/api/franquicias/sucursales/{id}` | Eliminar sucursal |

### Productos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/franquicias/sucursales/{id}/productos` | Listar productos de sucursal |
| POST | `/api/franquicias/sucursales/{id}/productos` | Agregar producto a sucursal |
| PUT | `/api/franquicias/productos/{id}/stock` | Modificar stock de producto |
| PUT | `/api/franquicias/productos/{id}` | Actualizar nombre de producto |
| DELETE | `/api/franquicias/productos/{id}` | Eliminar producto |

### Reportes
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/franquicias/{id}/productos-max-stock` | Producto con más stock por sucursal |

## Despliegue Local con Docker

### Requisitos
- Docker 20.10+
- Docker Compose 2.0+

### Pasos

1. **Clonar o entrar al directorio del proyecto:**
   ```bash
   cd franquicias-webflux
   ```

2. **Construir y levantar los servicios:**
   ```bash
   docker-compose up -d
   ```

3. **Verificar que los contenedores están corriendo:**
   ```bash
   docker-compose ps
   ```

4. **Ver logs de la aplicación:**
   ```bash
   docker-compose logs -f api
   ```

5. **La API estará disponible en:** `http://localhost:8080`

## Despliegue Local sin Docker

### Requisitos
- Java 21+
- PostgreSQL 15
- Gradle 8.5 (o usar el wrapper)

### Pasos

1. **Crear la base de datos:**
   ```sql
   CREATE DATABASE franquicias;
   ```

2. **Configurar variables de entorno o modificar `application.yml` con tu conexión.**

3. **Ejecutar la aplicación:**
   ```bash
   ./gradlew bootRun
   ```

## Ejemplos de Uso

### Crear una franquicia
```bash
curl -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre": "McDonalds"}'
```

### Agregar una sucursal
```bash
curl -X POST http://localhost:8080/api/franquicias/1/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Sucursal Centro"}'
```

### Agregar un producto
```bash
curl -X POST http://localhost:8080/api/franquicias/sucursales/1/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Hamburguesa", "stock": 100}'
```

### Modificar stock
```bash
curl -X PUT http://localhost:8080/api/franquicias/productos/1/stock \
  -H "Content-Type: application/json" \
  -d '{"stock": 150}'
```

### Ver producto con más stock por sucursal
```bash
curl http://localhost:8080/api/franquicias/1/productos-max-stock
```

## Detener los Servicios

```bash
docker-compose down
```

Para eliminar también los datos persistidos:
```bash
docker-compose down -v
```

## Licencia

MIT
