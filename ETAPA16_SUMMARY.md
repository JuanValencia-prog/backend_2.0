# ETAPA 16 - Soporte de imágenes en productos

## Objetivo

Agregar soporte backend real para `image` en productos, de forma que frontend pueda consumir y administrar la imagen principal del producto desde el contrato oficial del API, sin depender de normalizaciones temporales en cliente ni de cargas manuales directas sobre la base de datos.

La meta de `etapa16` es cerrar este alcance de punta a punta:

- persistencia
- requests y responses
- catálogo público
- CRUD administrativo de productos
- seed demo
- documentación
- pruebas

---

## Qué implementa etapa16

### 1. Campo `image` persistido en productos
Se agrega el atributo `image` al modelo `Product` y a la definición SQL de referencia.

Regla aplicada:

- `image` es opcional
- si llega vacío o en blanco, se normaliza a `null`
- si viene informado, se persiste como URL de imagen principal del producto

### 2. Contrato público actualizado para catálogo
Los endpoints públicos ahora devuelven `image` en el shape de producto:

- `GET /api/v1/products`
- `GET /api/v1/products/{id}`

Esto permite que frontend renderice tarjetas, listados y detalle de producto con una imagen oficial servida por el backend.

### 3. Contrato admin actualizado para productos
Los endpoints administrativos ahora aceptan y devuelven `image`:

- `POST /api/v1/admin/products`
- `PUT /api/v1/admin/products/{id}`

Con esto, el dashboard admin ya puede:

- crear productos con imagen
- editar o reemplazar la imagen principal
- recuperar el valor persistido luego de crear o actualizar

### 4. Mapeo completo entre capas
`image` queda propagado en:

- entidad JPA
- DTO de servicio
- request DTO web
- response DTO web
- mapper entity <-> DTO
- mapper web request/response

Esto evita inconsistencias entre persistencia, lógica de negocio y contrato HTTP.

### 5. Seed demo con URLs reales
El perfil `demo` ahora carga productos con imágenes públicas reales para facilitar:

- validación visual desde frontend
- demos funcionales
- smoke tests manuales
- revisión de catálogo sin datos vacíos

### 6. Pruebas de integración ajustadas
Se amplían pruebas para cubrir:

- creación de producto con `image`
- respuesta pública con `image`
- actualización admin de `image`
- presencia de `image` en seed demo
- exposición de `image` en catálogo demo

---

## Archivos principales incorporados o modificados

### Dominio / persistencia
- `src/main/java/co/edu/cesde/pps/model/Product.java`
- `src/main/resources/sql/schema.sql`
- `src/main/resources/sql/data.sql`

### DTOs y mappers
- `src/main/java/co/edu/cesde/pps/dto/ProductDTO.java`
- `src/main/java/co/edu/cesde/pps/mapper/ProductMapper.java`
- `src/main/java/co/edu/cesde/pps/web/dto/request/ProductUpsertRequest.java`
- `src/main/java/co/edu/cesde/pps/web/dto/response/ProductResponse.java`
- `src/main/java/co/edu/cesde/pps/web/mapper/WebRequestMapper.java`
- `src/main/java/co/edu/cesde/pps/web/mapper/WebResponseMapper.java`

### Servicio / seed
- `src/main/java/co/edu/cesde/pps/service/ProductService.java`
- `src/main/java/co/edu/cesde/pps/config/demo/DemoDataSeeder.java`

### Testing
- `src/test/java/co/edu/cesde/pps/Etapa11ApplicationLayerIntegrationTest.java`
- `src/test/java/co/edu/cesde/pps/Etapa12HttpIntegrationTest.java`
- `src/test/java/co/edu/cesde/pps/Etapa13AdminAuthorizationIntegrationTest.java`
- `src/test/java/co/edu/cesde/pps/DemoProfileSeedIntegrationTest.java`

### Documentación
- `BACKEND_ENDPOINTS_REFRENCE.md`
- `ETAPA16_SUMMARY.md`
- `README.md`

---

## Criterio de terminado

`etapa16` se considera terminada cuando:

- `Product` persiste `image`
- `GET /api/v1/products` devuelve `image`
- `GET /api/v1/products/{id}` devuelve `image`
- `POST /api/v1/admin/products` acepta y devuelve `image`
- `PUT /api/v1/admin/products/{id}` acepta y devuelve `image`
- el perfil `demo` siembra productos con imágenes
- frontend tiene contrato oficial actualizado
- existe documentación nueva propia de etapa16
- existe handoff operativo local en `documents_external/`

---

## Validación ejecutada

```bash
mvn -q -DskipTests compile
mvn -q -Dtest=Etapa11ApplicationLayerIntegrationTest,Etapa12HttpIntegrationTest,Etapa13AdminAuthorizationIntegrationTest,DemoProfileSeedIntegrationTest test
mvn -q test
```

---

## Nota operativa de base de datos

Según la configuración actual del proyecto, `application.yml` usa:

- `ddl-auto: update`

Por tanto, en entornos donde no se sobrescriba ese valor, Hibernate puede crear la nueva columna automáticamente.

Si un entorno usa otra estrategia de DDL, la columna requerida es:

- `image VARCHAR(1000)` en la tabla `products`

---

**Fecha:** 5 de abril de 2026  
**Rama objetivo:** `etapa16`  
**Estado:** ✅ Soporte de imágenes en productos implementado, probado y documentado

